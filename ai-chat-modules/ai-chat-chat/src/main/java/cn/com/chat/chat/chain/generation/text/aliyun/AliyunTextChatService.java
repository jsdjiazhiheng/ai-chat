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
    private final IChatMessageService chatMessageService;


    @Override
    public TextResult blockCompletion(String model, String system, List<MessageItem> history, String content) {

        AliyunTextRequest request = buildRequest(model, system, history, content);

        HttpHeaders header = getHeader();

        HttpEntity<AliyunTextRequest> entity = new HttpEntity<>(request, header);

        String response = HttpUtils.getRestTemplate().postForObject(AliyunApis.QWEN_API, entity, String.class);

        log.info("AliyunTextChatService -> 请求结果 ： {}", response);

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

        log.info("AliyunTextChatService -> 返回结果 ： {}", result);

        return result;

    }

    @Override
    public void streamCompletion(String model, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        AliyunTextRequest request = buildRequest(model, system, history, message.getContent());

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
            HttpUtils.getWebClient().post()
                .uri(AliyunApis.QWEN_API)
                .header("Authorization", "Bearer " + accessTokenService.getAccessToken())
                .header("X-DashScope-SSE", "enable")
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(String.class)
                .onErrorResume(WebClientResponseException.class, error -> {
                    HttpStatusCode statusCode = error.getStatusCode();
                    String res = error.getResponseBodyAsString();
                    log.error("Aliyun AI API error: {} {}", statusCode, res);
                    countDownLatch.countDown();
                    return Flux.error(new RuntimeException(res));
                }).subscribe(response -> {
                    log.info("AliyunTextChatService -> 返回结果 ： {}", response);
                    if (!"[DONE]".equals(response)) {
                        AliyunCompletionResult object = JsonUtils.parseObject(response, AliyunCompletionResult.class);
                        if (object != null) {
                            String content = object.getOutput().getChoices().get(0).getMessage().getContent();
                            String finishReason = object.getOutput().getChoices().get(0).getFinishReason();
                            try {

                                MessageUtils.handleResult(result, content, finishReason, object.getUsage());

                                ChatMessageVo messageVo = MessageUtils.buildTextMessage(message.getChatId(), messageId, message.getMessageId(), result);

                                if (StringUtils.isNotEmpty(finishReason)) {
                                    object.getOutput().getChoices().get(0).getMessage().setContent(builder.toString());
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
        }, error -> log.error("Aliyun AI API error: {}", error.getMessage()), () -> log.info("emitter completed"));

        try {
            countDownLatch.await();
            ChatMessageBo messageBo = MessageUtils.buildTextChatMessage(message.getChatId(), messageId, message.getMessageId(), result, message.getUserId());
            chatMessageService.insertByBo(messageBo);
            chatMessageService.updateStatusByMessageId(message.getMessageId(), 2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private AliyunTextRequest buildRequest(String model, String system, List<MessageItem> history, String content) {
        AliyunTextRequest request = AliyunTextRequest.builder()
            .model(model)
            .input(AliyunTextInput.builder()
                .messages(MessageItem.buildMessageList(system, history, content))
                .build())
            .parameter(AliyunTextParameter.builder().resultFormat("message").build())
            .build();

        log.info("AliyunTextChatService -> 请求参数 ： {}", JsonUtils.toJsonString(request));

        return request;
    }

    private HttpHeaders getHeader() {
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + accessTokenService.getAccessToken());
        return header;
    }

}
