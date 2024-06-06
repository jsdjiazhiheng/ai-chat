package cn.com.chat.chat.chain.generation.text.deepseek;

import cn.com.chat.chat.chain.response.deepseek.text.DeepSeekCompletionResult;
import cn.hutool.core.lang.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.com.chat.chat.chain.apis.DeepSeekApis;
import cn.com.chat.chat.chain.auth.deepseek.DeepSeekAccessTokenService;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.generation.text.TextChatService;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.request.deepseek.text.DeepSeekTextRequest;
import cn.com.chat.chat.chain.response.base.text.TextResult;
import cn.com.chat.chat.chain.utils.HttpUtils;
import cn.com.chat.chat.chain.utils.MessageUtils;
import cn.com.chat.chat.domain.bo.ChatMessageBo;
import cn.com.chat.chat.domain.vo.ChatMessageVo;
import cn.com.chat.chat.service.IChatMessageService;
import cn.com.chat.common.core.utils.StringUtils;
import cn.com.chat.common.json.utils.JsonUtils;
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
 * @date 2024-05-09
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DeepSeekTextChatService implements TextChatService {

    private final DeepSeekAccessTokenService accessTokenService;
    private final IChatMessageService chatMessageService;

    @Override
    public TextResult blockCompletion(String model, String system, List<MessageItem> history, String content) {

        DeepSeekTextRequest request = buildRequest(model, system, history, content);

        HttpHeaders header = getHeader();

        HttpEntity<DeepSeekTextRequest> entity = new HttpEntity<>(request, header);

        DeepSeekCompletionResult object = HttpUtils.getRestTemplate().postForObject(DeepSeekApis.API, entity, DeepSeekCompletionResult.class);

        String text = Objects.requireNonNull(object).getChoices().get(0).getMessage().getContent();

        TextResult result = TextResult.builder()
            .model(TextChatType.DEEPSEEK.name())
            .version(model)
            .content(text)
            .totalTokens(Long.valueOf(object.getUsage().getTotalTokens()))
            .finishReason(object.getChoices().get(0).getFinishReason())
            .response(JsonUtils.toJsonString(object))
            .build();

        log.info("DeepSeekTextChatService -> 返回结果 ： {}", result);

        return result;
    }


    @Override
    public void streamCompletion(String model, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        DeepSeekTextRequest request = buildRequest(model, system, history, message.getContent());
        request.setStream(true);

        Flux<DeepSeekTextRequest> flux = Flux.create(emitter -> {
            emitter.next(request);
            emitter.complete();
        });

        StringBuilder builder = new StringBuilder();

        TextResult result = TextResult.builder()
            .model(TextChatType.DEEPSEEK.name())
            .version(model)
            .build();

        String messageId = UUID.fastUUID().toString();

        flux.subscribe(consumer -> {
            HttpUtils.getWebClient().post()
                .uri(DeepSeekApis.API)
                .header("Authorization", "Bearer " + accessTokenService.getAccessToken())
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(String.class)
                .onErrorResume(WebClientResponseException.class, error -> {
                    HttpStatusCode statusCode = error.getStatusCode();
                    String res = error.getResponseBodyAsString();
                    log.error("DeepSeek AI API error: {} {}", statusCode, res);
                    countDownLatch.countDown();
                    return Flux.error(new RuntimeException(res));
                })
                .subscribe(response -> {
                    log.info("DeepSeekTextChatService -> 返回结果 ： {}", response);
                    if (!"[DONE]".equals(response)) {
                        DeepSeekCompletionResult object = JsonUtils.parseObject(response, DeepSeekCompletionResult.class);
                        if (object != null) {
                            String finishReason = object.getChoices().get(0).getFinishReason();
                            String content = object.getChoices().get(0).getMessage().getContent();
                            try {

                                MessageUtils.handleResult(result, content, finishReason, object.getUsage());

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
        }, error -> log.error("DeepSeek AI API error: {}", error.getMessage()));

        try {
            countDownLatch.await();
            ChatMessageBo messageBo = MessageUtils.buildTextChatMessage(message.getChatId(), messageId, message.getMessageId(), result, message.getUserId());
            chatMessageService.insertByBo(messageBo);
            chatMessageService.updateStatusByMessageId(message.getMessageId(), 2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private DeepSeekTextRequest buildRequest(String model, String system, List<MessageItem> history, String content) {

        List<MessageItem> messageItems = MessageItem.buildMessageList(system, history, content);

        DeepSeekTextRequest deepSeekTextRequest = DeepSeekTextRequest.builder()
            .model(model)
            .messages(messageItems)
            .build();

        log.info("DeepSeekTextChatService -> 请求参数 ： {}", JsonUtils.toJsonString(deepSeekTextRequest));

        return deepSeekTextRequest;
    }

    private HttpHeaders getHeader() {
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + accessTokenService.getAccessToken());
        return header;
    }

}
