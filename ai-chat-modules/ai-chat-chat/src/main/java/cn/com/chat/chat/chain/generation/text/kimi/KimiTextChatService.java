package cn.com.chat.chat.chain.generation.text.kimi;

import cn.com.chat.chat.chain.apis.KimiApis;
import cn.com.chat.chat.chain.auth.kimi.KimiAccessTokenService;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.generation.text.TextChatService;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.request.kimi.text.KimiTextRequest;
import cn.com.chat.chat.chain.response.base.Usage;
import cn.com.chat.chat.chain.response.base.text.TextResult;
import cn.com.chat.chat.chain.response.kimi.text.KimiCompletionResult;
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
 * Kimi文本服务
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-01
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class KimiTextChatService implements TextChatService {

    private final KimiAccessTokenService accessTokenService;
    private final MessageService messageService;

    @Override
    public TextResult blockCompletion(String model, String system, List<MessageItem> history, String content) {

        KimiTextRequest request = buildRequest(model, system, history, content);

        Map<String, String> header = getHeader();

        String response = HttpUtils.doPostJson(KimiApis.COMPLETION_TEXT, request, header);

        ChatLogUtils.printResponseLog(this.getClass(), response);

        KimiCompletionResult object = JsonUtils.parseObject(response, KimiCompletionResult.class);

        String text = Objects.requireNonNull(object).getChoices().get(0).getMessage().getContent();

        TextResult result = TextResult.builder()
            .model(TextChatType.KIMI.name())
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

        KimiTextRequest kimiTextRequest = buildRequest(model, system, history, message.getContent());
        kimiTextRequest.setStream(true);

        Flux<KimiTextRequest> flux = Flux.create(emitter -> {
            emitter.next(kimiTextRequest);
            emitter.complete();
        });

        StringBuilder builder = new StringBuilder();

        TextResult result = TextResult.builder()
            .model(TextChatType.KIMI.name())
            .version(model)
            .build();

        String messageId = UUID.fastUUID().toString();

        flux.subscribe(consumer -> {

            Map<String, String> header = getHeader();

            HttpUtils.asyncPostJson(KimiApis.COMPLETION_TEXT, consumer, header, new OkHttpCallback() {
                @Override
                public void onFailure(IOException e) {
                    messageService.saveFailMessage(message, messageId, e.getMessage());
                }

                @Override
                public void onResponse(String response) {
                    ChatLogUtils.printResponseLog(this.getClass(), response);
                    if (!"[DONE]".equals(response)) {
                        KimiCompletionResult object = JsonUtils.parseObject(response, KimiCompletionResult.class);
                        if (object != null) {
                            String finishReason = object.getChoices().get(0).getFinishReason();
                            String content = object.getChoices().get(0).getMessage().getContent();
                            Usage usage = object.getChoices().get(0).getUsage();
                            try {

                                MessageUtils.handleResult(result, content, finishReason, usage);

                                ChatMessageVo messageVo = MessageUtils.buildTextMessage(message.getChatId(), messageId, message.getMessageId(), result);

                                if (StringUtils.isNotEmpty(finishReason)) {
                                    object.getChoices().get(0).setMessage(MessageItem.buildAssistant(builder.toString()));
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
        }, error -> log.error("Kimi AI API error: {}", error.getMessage()));
    }

    private KimiTextRequest buildRequest(String model, String system, List<MessageItem> history, String content) {

        List<MessageItem> messageItems = MessageItem.buildMessageList(system, history, content);

        KimiTextRequest request = KimiTextRequest.builder()
            .model(model)
            .messages(messageItems)
            .build();

        ChatLogUtils.printRequestLog(this.getClass(), request);

        return request;
    }

    private Map<String, String> getHeader() {
        return Map.of("Authorization", "Bearer " + accessTokenService.getAccessToken());
    }

}
