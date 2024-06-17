package cn.com.chat.chat.chain.generation.text.aliyun;

import cn.com.chat.chat.chain.apis.AliyunApis;
import cn.com.chat.chat.chain.auth.aliyun.AliyunAccessTokenService;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.generation.text.TextChatService;
import cn.com.chat.chat.chain.request.aliyun.text.AliyunTextInput;
import cn.com.chat.chat.chain.request.aliyun.text.AliyunTextParameter;
import cn.com.chat.chat.chain.request.aliyun.text.AliyunTextRequest;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.response.aliyun.text.AliyunCompletionResult;
import cn.com.chat.chat.chain.response.base.text.TextResult;
import cn.com.chat.chat.chain.service.MessageService;
import cn.com.chat.chat.chain.utils.ChatLogUtils;
import cn.com.chat.chat.chain.utils.MessageUtils;
import cn.com.chat.chat.domain.vo.ChatMessageVo;
import cn.com.chat.common.core.utils.StringUtils;
import cn.com.chat.common.http.callback.OkHttpCallback;
import cn.com.chat.common.http.utils.HttpUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import cn.hutool.core.lang.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 阿里云文本聊天
 *
 * @author JiaZH
 * @date 2024-06-05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliyunTextChatService implements TextChatService {

    private final AliyunAccessTokenService accessTokenService;
    private final MessageService messageService;

    @Override
    public TextResult blockCompletion(String model, String system, List<MessageItem> history, String content) {

        AliyunTextRequest request = buildRequest(model, system, history, content);

        Map<String, String> header = getHeader(false);

        String response = HttpUtils.doPostJson(AliyunApis.QWEN_API, request, header);

        ChatLogUtils.printResponseLog(this.getClass(), response);

        AliyunCompletionResult completionResult = JsonUtils.parseObject(response, AliyunCompletionResult.class);

        String text = Objects.requireNonNull(completionResult).getOutput().getChoices().get(0).getMessage().getContent();

        TextResult result = TextResult.builder()
            .model(TextChatType.ALIYUN.name())
            .version(model)
            .content(text)
            .totalTokens(completionResult.getUsage().getTotalTokens().longValue())
            .finishReason(completionResult.getOutput().getChoices().get(0).getFinishReason())
            .response(JsonUtils.toJsonString(completionResult))
            .build();

        ChatLogUtils.printResultLog(this.getClass(), result);

        return result;

    }

    @Override
    public void streamCompletion(String model, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {

        AliyunTextRequest request = buildRequest(model, system, history, message.getContent());
        request.getParameter().setStream(true);
        request.getParameter().setIncrementalOutput(true);

        Flux<AliyunTextRequest> flux = Flux.create(emitter -> {
            emitter.next(request);
            emitter.complete();
        });

        StringBuilder builder = new StringBuilder();

        TextResult result = TextResult.builder()
            .model(TextChatType.ALIYUN.name())
            .version(model)
            .build();

        String messageId = UUID.fastUUID().toString();

        flux.subscribe(consumer -> {

            Map<String, String> header = getHeader(true);

            HttpUtils.asyncPostJson(AliyunApis.QWEN_API, consumer, header, new OkHttpCallback() {
                @Override
                public void onFailure(IOException e) {
                    messageService.saveFailMessage(message, messageId, e.getMessage());
                }

                @Override
                public void onResponse(String response) {
                    log.info("返回结果：{}", response);
                    ChatLogUtils.printResponseLog(this.getClass(), response);
                    if (!"[DONE]".equals(response)) {
                        AliyunCompletionResult object = JsonUtils.parseObject(response, AliyunCompletionResult.class);
                        if (object != null) {
                            String content = object.getOutput().getText();
                            String finishReason = object.getOutput().getFinishReason();
                            try {

                                MessageUtils.handleResult(result, StringUtils.remove(content, builder.toString()), finishReason, object.getUsage());

                                ChatMessageVo messageVo = MessageUtils.buildTextMessage(message.getChatId(), messageId, message.getMessageId(), result);

                                if (StringUtils.isNotEmpty(finishReason) && !StringUtils.equals(finishReason, "null")) {
                                    object.getOutput().setText(builder.toString());
                                    result.setContent(builder.toString());
                                    result.setResponse(JsonUtils.toJsonString(object));

                                    sseEmitter.send("[END]");

                                    messageService.saveSuccessMessage(message, messageId, result);

                                } else {
                                    builder.append(content);
                                    sseEmitter.send(messageVo);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            });
        }, error -> log.error("Aliyun AI API error: {}", error.getMessage()), () -> log.info("emitter completed"));

    }

    private AliyunTextRequest buildRequest(String model, String system, List<MessageItem> history, String content) {
        AliyunTextRequest request = AliyunTextRequest.builder()
            .model(model)
            .input(AliyunTextInput.builder()
                .messages(MessageItem.buildMessageList(system, null, content))
                .build())
            .parameter(AliyunTextParameter.builder().resultFormat("text").build())
            .build();

        ChatLogUtils.printRequestLog(this.getClass(), request);

        return request;
    }

    private Map<String, String> getHeader(boolean isSse) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessTokenService.getAccessToken());
        if (isSse) {
            headers.put("X-DashScope-SSE", "enable");
        }
        return headers;
    }

}
