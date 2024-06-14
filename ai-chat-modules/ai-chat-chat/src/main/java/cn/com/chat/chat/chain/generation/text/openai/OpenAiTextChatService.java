package cn.com.chat.chat.chain.generation.text.openai;

import cn.com.chat.chat.chain.apis.OpenAiApis;
import cn.com.chat.chat.chain.auth.openai.OpenAiAccessTokenService;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.generation.text.TextChatService;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.request.openai.text.OpenAiTextRequest;
import cn.com.chat.chat.chain.response.base.text.TextResult;
import cn.com.chat.chat.chain.response.openai.text.OpenAiCompletionResult;
import cn.com.chat.chat.chain.service.HttpService;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * OpenAI文本服务
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-06-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiTextChatService implements TextChatService {

    private final OpenAiAccessTokenService accessTokenService;
    private final MessageService messageService;
    private final HttpService httpService;

    @Override
    public TextResult blockCompletion(String model, String system, List<MessageItem> history, String content) {

        OpenAiTextRequest request = buildRequest(model, system, history, content);

        Map<String, String> header = getHeader();

        String response;

        try {
            httpService.setProxyHttpUtils();

            response = HttpUtils.doPostJson(OpenAiApis.TEXT_API, request, header);

        } finally {

            httpService.clearProxyHttpUtils();

        }

        ChatLogUtils.printResponseLog(this.getClass(), response);

        OpenAiCompletionResult completionResult = JsonUtils.parseObject(response, OpenAiCompletionResult.class);

        String text = Objects.requireNonNull(completionResult).getChoices().get(0).getMessage().getContent();

        TextResult result = TextResult.builder()
            .model(TextChatType.OPENAI.name())
            .version(model)
            .content(text)
            .totalTokens(completionResult.getUsage().getTotalTokens().longValue())
            .finishReason(completionResult.getChoices().get(0).getFinishReason())
            .response(JsonUtils.toJsonString(completionResult))
            .build();

        ChatLogUtils.printResultLog(this.getClass(), result);

        return result;
    }

    @Override
    public void streamCompletion(String model, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {

        OpenAiTextRequest request = buildRequest(model, system, history, message.getContent());

        Flux<OpenAiTextRequest> flux = Flux.create(emitter -> {
            emitter.next(request);
            emitter.complete();
        });

        StringBuilder builder = new StringBuilder();

        TextResult result = TextResult.builder()
            .model(TextChatType.OPENAI.name())
            .version(model)
            .build();

        String messageId = UUID.fastUUID().toString();

        flux.subscribe(consumer -> {

            Map<String, String> header = getHeader();

            try {
                httpService.setProxyHttpUtils();

                HttpUtils.asyncPostJson(OpenAiApis.TEXT_API, consumer, header, new OkHttpCallback() {
                    @Override
                    public void onFailure(IOException e) {
                        httpService.clearProxyHttpUtils();
                        messageService.saveFailMessage(message, messageId, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        ChatLogUtils.printResponseLog(this.getClass(), response);
                        if (!"[DONE]".equals(response)) {
                            OpenAiCompletionResult object = JsonUtils.parseObject(response, OpenAiCompletionResult.class);
                            if (object != null) {
                                String content = object.getChoices().get(0).getMessage().getContent();
                                String finishReason = object.getChoices().get(0).getFinishReason();
                                try {

                                    MessageUtils.handleResult(result, content, finishReason, object.getUsage());

                                    ChatMessageVo messageVo = MessageUtils.buildTextMessage(message.getChatId(), messageId, message.getMessageId(), result);

                                    if (StringUtils.isNotEmpty(finishReason)) {
                                        object.getChoices().get(0).getMessage().setContent(builder.toString());
                                        result.setContent(builder.toString());
                                        result.setResponse(JsonUtils.toJsonString(object));

                                        sseEmitter.send("[END]");

                                        httpService.clearProxyHttpUtils();

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
            } finally {
                httpService.clearProxyHttpUtils();
            }
        }, error -> log.error("OPEN AI API error: {}", error.getMessage()), () -> log.info("emitter completed"));

    }

    private OpenAiTextRequest buildRequest(String model, String system, List<MessageItem> history, String content) {
        OpenAiTextRequest request = OpenAiTextRequest.builder()
            .model(model)
            .messages(MessageItem.buildMessageList(system, history, content))
            .build();

        ChatLogUtils.printRequestLog(this.getClass(), request);

        return request;
    }

    private Map<String, String> getHeader() {
        return Map.of("Authorization", "Bearer " + accessTokenService.getAccessToken());
    }

}
