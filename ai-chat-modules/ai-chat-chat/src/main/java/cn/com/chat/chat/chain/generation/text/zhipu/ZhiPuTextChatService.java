package cn.com.chat.chat.chain.generation.text.zhipu;

import cn.hutool.core.lang.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.com.chat.chat.chain.apis.ZhiPuApis;
import cn.com.chat.chat.chain.auth.zhipu.ZhiPuAccessTokenService;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.generation.text.TextChatService;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.request.zhipu.text.ZhiPuTextRequest;
import cn.com.chat.chat.chain.request.zhipu.text.ZhiPuTextTools;
import cn.com.chat.chat.chain.request.zhipu.text.ZhiPuTextWebSearch;
import cn.com.chat.chat.chain.response.base.text.TextResult;
import cn.com.chat.chat.chain.response.zhipu.text.ZhiPuCompletionResult;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * TODO
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-15
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ZhiPuTextChatService implements TextChatService {

    private final ZhiPuAccessTokenService accessTokenService;
    private final IChatMessageService chatMessageService;

    @Override
    public TextResult blockCompletion(String model, String system, List<MessageItem> history, String content) {

        ZhiPuTextRequest zhiPuTextRequest = buildRequest(model, system, history, content);

        log.info("ZhiPuTextChatService -> 请求参数 ： {}", JsonUtils.toJsonString(zhiPuTextRequest));

        HttpHeaders header = getHeader();

        HttpEntity<ZhiPuTextRequest> entity = new HttpEntity<>(zhiPuTextRequest, header);

        ZhiPuCompletionResult object = HttpUtils.getRestTemplate().postForObject(ZhiPuApis.CHAT_API, entity, ZhiPuCompletionResult.class);

        String text = Objects.requireNonNull(object).getChoices().get(0).getMessage().getContent();

        TextResult result = TextResult.builder()
            .model(TextChatType.ZHIPU.name())
            .version(model)
            .content(text)
            .totalTokens(Long.valueOf(object.getUsage().getTotalTokens()))
            .finishReason(object.getChoices().get(0).getFinishReason())
            .response(JsonUtils.toJsonString(object))
            .build();

        log.info("ZhiPuTextChatService -> 返回结果 ： {}", result);

        return result;
    }

    @Override
    public void streamCompletion(String model, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        ZhiPuTextRequest zhiPuTextRequest = buildRequest(model, system, history, message.getContent());
        zhiPuTextRequest.setStream(true);

        log.info("ZhiPuTextChatService -> 请求参数 ： {}", JsonUtils.toJsonString(zhiPuTextRequest));

        Flux<ZhiPuTextRequest> flux = Flux.create(emitter -> {
            emitter.next(zhiPuTextRequest);
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
                .uri(ZhiPuApis.CHAT_API)
                .header("Authorization", "Bearer " + accessTokenService.getAccessToken())
                .bodyValue(zhiPuTextRequest)
                .retrieve()
                .bodyToFlux(String.class)
                .onErrorResume(WebClientResponseException.class, error -> {
                    HttpStatusCode statusCode = error.getStatusCode();
                    String res = error.getResponseBodyAsString();
                    log.error("ZhiPu AI API error: {} {}", statusCode, res);
                    countDownLatch.countDown();
                    return Flux.error(new RuntimeException(res));
                })
                .subscribe(response -> {
                    log.info("ZhiPuTextChatService -> 返回结果 ： {}", response);
                    if (!"[DONE]".equals(response)) {
                        ZhiPuCompletionResult object = JsonUtils.parseObject(response, ZhiPuCompletionResult.class);
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
                                    builder.append(MessageUtils.handleContent(content));
                                    sseEmitter.send(messageVo);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
        }, error -> log.error("ZhiPu AI API error: {}", error.getMessage()));

        try {
            countDownLatch.await();
            ChatMessageBo messageBo = MessageUtils.buildTextChatMessage(message.getChatId(), messageId, message.getMessageId(), result, message.getUserId());
            chatMessageService.insertByBo(messageBo);
            chatMessageService.updateStatusByMessageId(message.getMessageId(), 2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private ZhiPuTextRequest buildRequest(String model, String system, List<MessageItem> history, String content) {

        List<MessageItem> messageItems = MessageItem.buildMessageList(system, history, content);

        List<ZhiPuTextTools> toolsList = new ArrayList<>();
        ZhiPuTextTools tools = new ZhiPuTextTools();
        tools.setType("web_search");
        ZhiPuTextWebSearch webSearch = ZhiPuTextWebSearch.builder()
            .enable(true)
            .searchQuery(content)
            .searchResult(false)
            .build();
        tools.setWebSearch(webSearch);
        toolsList.add(tools);

        return ZhiPuTextRequest.builder()
            .model(model)
            .messages(messageItems)
            .tools(toolsList)
            .build();
    }

    private HttpHeaders getHeader() {
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + accessTokenService.getAccessToken());
        return header;
    }

}
