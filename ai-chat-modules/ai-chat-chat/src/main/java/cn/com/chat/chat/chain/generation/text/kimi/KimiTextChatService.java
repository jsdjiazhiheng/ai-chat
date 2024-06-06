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
import cn.com.chat.chat.chain.utils.HttpUtils;
import cn.com.chat.chat.chain.utils.MessageUtils;
import cn.com.chat.chat.domain.bo.ChatMessageBo;
import cn.com.chat.chat.domain.vo.ChatMessageVo;
import cn.com.chat.chat.service.IChatMessageService;
import cn.com.chat.common.core.utils.StringUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import cn.hutool.core.lang.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * TODO
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
    private final IChatMessageService chatMessageService;

    @Override
    public TextResult blockCompletion(String model, String system, List<MessageItem> history, String content) {

        KimiTextRequest kimiTextRequest = buildRequest(model, system, history, content);

        HttpHeaders header = getHeader();

        HttpEntity<KimiTextRequest> entity = new HttpEntity<>(kimiTextRequest, header);

        KimiCompletionResult object = HttpUtils.getRestTemplate().postForObject(KimiApis.COMPLETION_TEXT, entity, KimiCompletionResult.class);

        String text = Objects.requireNonNull(object).getChoices().get(0).getMessage().getContent();

        TextResult result = TextResult.builder()
            .model(TextChatType.KIMI.name())
            .version(model)
            .content(text)
            .totalTokens(Long.valueOf(object.getUsage().getTotalTokens()))
            .finishReason(object.getChoices().get(0).getFinishReason())
            .response(JsonUtils.toJsonString(object))
            .build();

        log.info("KimiTextChatService -> 返回结果 ： {}", result);

        return result;
    }

    @Override
    public void streamCompletion(String model, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {
        CountDownLatch countDownLatch = new CountDownLatch(1);

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
            HttpUtils.getWebClient().post()
                .uri(KimiApis.COMPLETION_TEXT)
                .header("Authorization", "Bearer " + accessTokenService.getAccessToken())
                .bodyValue(kimiTextRequest)
                .retrieve()
                .bodyToFlux(String.class)
                .onErrorResume(WebClientResponseException.class, error -> {
                    HttpStatusCode statusCode = error.getStatusCode();
                    String res = error.getResponseBodyAsString();
                    log.error("Kimi AI API error: {} {}", statusCode, res);
                    countDownLatch.countDown();
                    return Flux.error(new RuntimeException(res));
                })
                .subscribe(response -> {
                    log.info("KimiTextChatService -> 返回结果 ： {}", response);
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
                                    countDownLatch.countDown();
                                    sseEmitter.send("[END]");
                                } else {
                                    builder.append(content);
                                    sseEmitter.send(messageVo);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
        }, error -> log.error("Kimi AI API error: {}", error.getMessage()));

        try {
            countDownLatch.await();
            ChatMessageBo messageBo = MessageUtils.buildTextChatMessage(message.getChatId(), messageId, message.getMessageId(), result, message.getUserId());
            chatMessageService.insertByBo(messageBo);
            chatMessageService.updateStatusByMessageId(message.getMessageId(), 2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private KimiTextRequest buildRequest(String model, String system, List<MessageItem> history, String content) {

        List<MessageItem> messageItems = MessageItem.buildMessageList(system, history, content);

        KimiTextRequest kimiTextRequest = KimiTextRequest.builder()
            .model(model)
            .messages(messageItems)
            .build();

        log.info("KimiTextChatService -> 请求参数 ： {}", JsonUtils.toJsonString(kimiTextRequest));

        return kimiTextRequest;
    }

    private HttpHeaders getHeader() {
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + accessTokenService.getAccessToken());
        return header;
    }

}
