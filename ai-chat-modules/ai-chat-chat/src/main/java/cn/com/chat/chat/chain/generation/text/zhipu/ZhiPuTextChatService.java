package cn.com.chat.chat.chain.generation.text.zhipu;

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
import cn.com.chat.chat.chain.service.MessageService;
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
import java.util.*;

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
    private final MessageService messageService;

    @Override
    public TextResult blockCompletion(String model, String system, List<MessageItem> history, String content) {

        ZhiPuTextRequest request = buildRequest(model, system, history, content);

        Map<String, String> header = getHeader();

        String response = HttpUtils.doPostJson(ZhiPuApis.CHAT_API, request, header);

        ZhiPuCompletionResult object = JsonUtils.parseObject(response, ZhiPuCompletionResult.class);

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

        ZhiPuTextRequest zhiPuTextRequest = buildRequest(model, system, history, message.getContent());
        zhiPuTextRequest.setStream(true);

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

            Map<String, String> header = getHeader();

            HttpUtils.asyncPostJson(ZhiPuApis.CHAT_API, consumer, header, new OkHttpCallback() {
                @Override
                public void onFailure(IOException e) {
                    messageService.saveFailMessage(message, messageId, e.getMessage());
                }

                @Override
                public void onResponse(String response) {
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
        }, error -> log.error("ZhiPu AI API error: {}", error.getMessage()));

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

        ZhiPuTextRequest request = ZhiPuTextRequest.builder()
            .model(model)
            .messages(messageItems)
            .tools(toolsList)
            .build();

        log.info("ZhiPuTextChatService -> 请求参数 ： {}", JsonUtils.toJsonString(request));

        return request;
    }

    private Map<String, String> getHeader() {
        return Map.of("Authorization", "Bearer " + accessTokenService.getAccessToken());
    }

}
