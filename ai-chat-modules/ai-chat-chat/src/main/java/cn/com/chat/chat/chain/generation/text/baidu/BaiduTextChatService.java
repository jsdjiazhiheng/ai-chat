package cn.com.chat.chat.chain.generation.text.baidu;

import cn.com.chat.chat.chain.auth.baidu.BaiduAccessTokenService;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.enums.model.BaiduModelEnums;
import cn.com.chat.chat.chain.exception.baidu.BaiduTextChatException;
import cn.com.chat.chat.chain.generation.text.TextChatService;
import cn.com.chat.chat.chain.request.baidu.text.BaiduTextRequest;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.response.baidu.text.BaiduCompletionResult;
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
import cn.hutool.json.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
    private final MessageService messageService;

    @Override
    public TextResult blockCompletion(String model, String system, List<MessageItem> history, String content) {
        String url = BaiduModelEnums.getModelUrl(model);

        BaiduTextRequest request = buildRequest(system, history, content);

        String response = HttpUtils.doPostJson(accessTokenService.getUrl(url), request);

        ChatLogUtils.printResponseLog(this.getClass(), response);

        if (StringUtils.contains(response, "error_code")) {
            JSONObject object = JsonUtils.parseObject(response, JSONObject.class);
            String errorMsg = Objects.requireNonNull(object).getStr("error_msg");
            throw new BaiduTextChatException(errorMsg);
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

        ChatLogUtils.printResultLog(this.getClass(), result);

        return result;
    }

    @Override
    public void streamCompletion(String model, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {

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

            HttpUtils.asyncPostJson(accessTokenService.getUrl(url), consumer, new OkHttpCallback() {
                @Override
                public void onFailure(IOException e) {
                    messageService.saveFailMessage(message, messageId, e.getMessage());
                }

                @Override
                public void onResponse(String response) {
                    if (!"[DONE]".equals(response)) {
                        ChatLogUtils.printResponseLog(this.getClass(), response);
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

                                    if(StringUtils.isNotBlank(content)) {
                                        sseEmitter.send(messageVo);
                                    }
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
        }, error -> log.error("Baidu AI API error: {}", error.getMessage()), () -> log.info("emitter completed"));

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

        ChatLogUtils.printRequestLog(this.getClass(), request);

        return request;
    }

}
