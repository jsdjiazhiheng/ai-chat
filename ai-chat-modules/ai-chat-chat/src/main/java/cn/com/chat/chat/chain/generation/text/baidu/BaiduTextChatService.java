package cn.com.chat.chat.chain.generation.text.baidu;

import cn.com.chat.chat.chain.exception.baidu.BaiduTextChatException;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.com.chat.chat.chain.auth.baidu.BaiduAccessTokenService;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.enums.model.BaiduModelEnums;
import cn.com.chat.chat.chain.generation.text.TextChatService;
import cn.com.chat.chat.chain.request.baidu.text.BaiduTextRequest;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.response.baidu.text.BaiduCompletionResult;
import cn.com.chat.chat.chain.response.base.text.TextResult;
import cn.com.chat.chat.chain.utils.HttpUtils;
import cn.com.chat.chat.chain.utils.MessageUtils;
import cn.com.chat.chat.domain.bo.ChatMessageBo;
import cn.com.chat.chat.domain.vo.ChatMessageVo;
import cn.com.chat.chat.service.IChatMessageService;
import cn.com.chat.common.core.utils.StringUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import org.springframework.http.HttpEntity;
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
 * @date 2024-05-02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BaiduTextChatService implements TextChatService {

    private final BaiduAccessTokenService accessTokenService;
    private final IChatMessageService chatMessageService;

    @Override
    public TextResult blockCompletion(String model, String system, List<MessageItem> history, String content) {
        String url = BaiduModelEnums.getModelUrl(model);

        BaiduTextRequest request = buildRequest(system, history, content);

        HttpEntity<BaiduTextRequest> entity = new HttpEntity<>(request);

        String response = HttpUtils.getRestTemplate().postForObject(accessTokenService.getUrl(url), entity, String.class);

        log.info("BaiduTextChatService -> 请求结果 ： {}", response);

        if(StringUtils.contains(response, "error_code")) {
            JSONObject object = JsonUtils.parseObject(response, JSONObject.class);
            Integer errorCode = object.getInt("error_code");
            String errorMsg = object.getStr("error_msg");
            throw new BaiduTextChatException("", "");
        }

        BaiduCompletionResult completionResult = JsonUtils.parseObject(response, BaiduCompletionResult.class);

        String text = Objects.requireNonNull(completionResult).getResult();

        TextResult result = TextResult.builder()
            .model(TextChatType.BAIDU.name())
            .version(model)
            .content(text)
            .totalTokens(Long.valueOf(completionResult.getUsage().getTotalTokens()))
            .finishReason(completionResult.getFinishReason())
            .response(JsonUtils.toJsonString(completionResult))
            .build();

        log.info("BaiduTextChatService -> 返回结果 ： {}", result);

        return result;
    }

    @Override
    public void streamCompletion(String model, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        String url = BaiduModelEnums.getModelUrl(model);

        BaiduTextRequest request = buildRequest(system, history, message.getContent());
        request.setStream(true);

        Flux<BaiduTextRequest> flux = Flux.create(emitter -> {
            emitter.next(request);
            emitter.complete();
        });

        StringBuilder builder = new StringBuilder();

        TextResult result = TextResult.builder()
            .model(TextChatType.BAIDU.name())
            .version(model)
            .build();

        String messageId = UUID.fastUUID().toString();

        flux.subscribe(consumer -> {
            HttpUtils.getWebClient().post()
                .uri(accessTokenService.getUrl(url))
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(String.class)
                .onErrorResume(WebClientResponseException.class, error -> {
                    HttpStatusCode statusCode = error.getStatusCode();
                    String res = error.getResponseBodyAsString();
                    log.error("Baidu AI API error: {} {}", statusCode, res);
                    countDownLatch.countDown();
                    return Flux.error(new RuntimeException(res));
                }).subscribe(response -> {
                    log.info("BaiduTextChatService -> 返回结果 ： {}", response);
                    if (!"[DONE]".equals(response)) {
                        BaiduCompletionResult object = JsonUtils.parseObject(response, BaiduCompletionResult.class);
                        if (object != null) {
                            Boolean isEnd = object.getIsEnd();
                            String content = object.getResult();
                            String finishReason = object.getFinishReason();
                            try {

                                MessageUtils.handleResult(result, content, finishReason, object.getUsage());

                                ChatMessageVo messageVo = MessageUtils.buildTextMessage(message.getChatId(), messageId, message.getMessageId(), result);

                                if (isEnd) {
                                    object.setResult(builder.toString());
                                    result.setContent(builder.toString());
                                    result.setResponse(JsonUtils.toJsonString(object));
                                    sseEmitter.send("[END]");
                                    countDownLatch.countDown();
                                } else {
                                    builder.append(MessageUtils.handleContent(content));
                                    sseEmitter.send(messageVo);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
        }, error -> log.error("Baidu AI API error: {}", error.getMessage()), () -> log.info("emitter completed"));

        try {
            countDownLatch.await();
            ChatMessageBo messageBo = MessageUtils.buildTextChatMessage(message.getChatId(), messageId, message.getMessageId(), result, message.getUserId());
            chatMessageService.insertByBo(messageBo);
            chatMessageService.updateStatusByMessageId(message.getMessageId(), 2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private BaiduTextRequest buildRequest(String system, List<MessageItem> history, String content) {

        List<MessageItem> messageItems = MessageItem.buildMessageList(null, history, content);

        BaiduTextRequest request = BaiduTextRequest
            .builder()
            .messages(messageItems)
            .build();

        if (StringUtils.isNotBlank(system)) {
            request.setSystem(system);
        }

        log.info("BaiduTextChatService -> 请求参数 ： {}", JsonUtils.toJsonString(request));

        return request;
    }

}