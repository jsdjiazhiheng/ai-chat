package cn.com.chat.chat.chain.generation.text.volcengine;

import cn.com.chat.chat.chain.apis.VolcengineApis;
import cn.com.chat.chat.chain.auth.volcengine.VolcengineAccessTokenService;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.enums.model.VolcengineModelEnums;
import cn.com.chat.chat.chain.generation.text.TextChatService;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.request.volcengine.text.VolcengineStreamOption;
import cn.com.chat.chat.chain.request.volcengine.text.VolcengineTextRequest;
import cn.com.chat.chat.chain.response.base.Usage;
import cn.com.chat.chat.chain.response.base.text.TextChoice;
import cn.com.chat.chat.chain.response.base.text.TextResult;
import cn.com.chat.chat.chain.response.volcengine.text.VolcengineCompletionResult;
import cn.com.chat.chat.chain.service.MessageService;
import cn.com.chat.chat.chain.utils.ChatLogUtils;
import cn.com.chat.chat.chain.utils.MessageUtils;
import cn.com.chat.chat.domain.vo.ChatMessageVo;
import cn.com.chat.common.core.utils.StringUtils;
import cn.com.chat.common.http.callback.OkHttpCallback;
import cn.com.chat.common.http.utils.HttpUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 豆包文本服务
 *
 * @author JiaZH
 * @date 2024-06-18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VolcengineTextChatService implements TextChatService {

    private final VolcengineAccessTokenService accessTokenService;
    private final MessageService messageService;

    @Override
    public TextResult blockCompletion(String model, String system, List<MessageItem> history, String content) {

        String point = VolcengineModelEnums.getPoint(model);

        Assert.notBlank(point, "未找到对应的接入推理点");

        VolcengineTextRequest request = buildRequest(point, system, history, content);

        String response = HttpUtils.doPostJson(VolcengineApis.TEXT_API, request, getHeader());

        ChatLogUtils.printResponseLog(this.getClass(), response);

        VolcengineCompletionResult object = JsonUtils.parseObject(response, VolcengineCompletionResult.class);

        String text = Objects.requireNonNull(object).getChoices().get(0).getMessage().getContent();

        TextResult result = TextResult.builder()
            .model(TextChatType.VOLCENGINE.name())
            .version(model)
            .content(text)
            .totalTokens(Long.valueOf(object.getUsage().getTotalTokens()))
            .finishReason(object.getChoices().get(0).getFinishReason())
            .response(JsonUtils.toJsonString(object))
            .build();

        ChatLogUtils.printResultLog(this.getClass(), result);

        return result;
    }

    @Override
    public void streamCompletion(String model, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {

        String point = VolcengineModelEnums.getPoint(model);

        Assert.notBlank(point, "未找到对应的接入推理点");

        VolcengineTextRequest request = buildRequest(point, system, history, message.getContent());
        request.setStream(true);
        request.setStreamOptions(VolcengineStreamOption.builder().includeUsage(true).build());

        Flux<VolcengineTextRequest> flux = Flux.create(emitter -> {
            emitter.next(request);
            emitter.complete();
        });

        StringBuilder builder = new StringBuilder();

        TextResult result = TextResult.builder()
            .model(TextChatType.VOLCENGINE.name())
            .version(model)
            .build();

        String messageId = UUID.fastUUID().toString();

        AtomicBoolean isEnd = new AtomicBoolean(false);

        flux.subscribe(consumer -> {

            Map<String, String> header = getHeader();

            HttpUtils.asyncPostJson(VolcengineApis.TEXT_API, consumer, header, new OkHttpCallback() {
                @Override
                public void onFailure(IOException e) {
                    messageService.saveFailMessage(message, messageId, e.getMessage());
                }

                @Override
                public void onResponse(String response) {
                    if (!"[DONE]".equals(response)) {
                        ChatLogUtils.printResponseLog(this.getClass(), response);
                        VolcengineCompletionResult object = JsonUtils.parseObject(response, VolcengineCompletionResult.class);
                        if (object != null) {
                            String finishReason = "";
                            String content = "";
                            if (!object.getChoices().isEmpty()) {
                                finishReason = object.getChoices().get(0).getFinishReason();
                                content = object.getChoices().get(0).getMessage().getContent();
                            }
                            Usage usage = object.getUsage();
                            try {

                                MessageUtils.handleResult(result, content, finishReason, usage);

                                ChatMessageVo messageVo = MessageUtils.buildTextMessage(message.getChatId(), messageId, message.getMessageId(), result);

                                if (StringUtils.isNotEmpty(finishReason) || isEnd.get()) {
                                    if (!isEnd.get()) {
                                        if (StringUtils.isNotBlank(content)) {
                                            sseEmitter.send(messageVo);
                                        }
                                        sseEmitter.send("[END]");
                                        isEnd.set(true);
                                    } else {
                                        object.getChoices().add(new TextChoice(0, MessageItem.buildAssistant(builder.toString()), finishReason));
                                        result.setContent(builder.toString());
                                        result.setResponse(JsonUtils.toJsonString(object));

                                        messageService.saveSuccessMessage(message, messageId, result);
                                    }
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
        }, error -> log.error("Volcengine AI API error: {}", error.getMessage()));
    }

    private VolcengineTextRequest buildRequest(String point, String system, List<MessageItem> history, String content) {
        VolcengineTextRequest request = VolcengineTextRequest.builder()
            .model(point)
            .messages(MessageItem.buildMessageList(system, history, content))
            .build();

        ChatLogUtils.printRequestLog(this.getClass(), request);

        return request;
    }

    private Map<String, String> getHeader() {
        return Map.of("Authorization", "Bearer " + accessTokenService.getAccessToken());
    }

}
