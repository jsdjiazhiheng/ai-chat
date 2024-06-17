package cn.com.chat.chat.chain.generation.text.spark.listener;

import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.request.spark.text.SparkTextRequest;
import cn.com.chat.chat.chain.response.base.Usage;
import cn.com.chat.chat.chain.response.base.text.TextResult;
import cn.com.chat.chat.chain.response.spark.text.SparkTextResponse;
import cn.com.chat.chat.chain.response.spark.text.SparkTextResponsePayload;
import cn.com.chat.chat.chain.service.MessageService;
import cn.com.chat.chat.chain.utils.ChatLogUtils;
import cn.com.chat.chat.chain.utils.MessageUtils;
import cn.com.chat.chat.domain.vo.ChatMessageVo;
import cn.com.chat.common.json.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Objects;

/**
 * 星火sse聊天监听
 *
 * @author JiaZH
 * @date 2024-06-11
 */
@Slf4j
public class SparkSseChatListener extends WebSocketListener {

    private final SseEmitter sseEmitter;
    private final SparkTextRequest request;
    private final String messageId;
    private final TextResult result;
    private final StreamMessage message;
    private final MessageService messageService;
    private final StringBuilder builder = new StringBuilder();

    public SparkSseChatListener(SseEmitter sseEmitter, SparkTextRequest sparkTextRequest, String messageId, TextResult result, StreamMessage message, MessageService messageService) {
        this.sseEmitter = sseEmitter;
        this.request = sparkTextRequest;
        this.messageId = messageId;
        this.result = result;
        this.message = message;
        this.messageService = messageService;
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        String json = JsonUtils.toJsonString(request);
        if (json != null) {
            webSocket.send(json);
        }
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        ChatLogUtils.printResponseLog(this.getClass(), text);
        SparkTextResponse response = JsonUtils.parseObject(text, SparkTextResponse.class);
        if (response != null) {
            Integer code = response.getHeader().getCode();

            if (code == 0) {
                Integer status = response.getHeader().getStatus();
                SparkTextResponsePayload payload = response.getPayload();
                String content = payload.getChoices().getText().get(0).getContent();

                Usage usage = Objects.nonNull(payload.getUsage()) ? payload.getUsage().getText() : null;

                MessageUtils.handleResult(result, content, "", usage);

                ChatMessageVo messageVo = MessageUtils.buildTextMessage(message.getChatId(), messageId, message.getMessageId(), result);

                builder.append(content);

                try {
                    if (status != null && status == 2) {
                        sseEmitter.send(messageVo);
                        sseEmitter.send("[END]");
                        webSocket.close(1000, "结束");

                        response.getPayload().getChoices().getText().get(0).setContent(builder.toString());
                        result.setContent(builder.toString());
                        result.setResponse(JsonUtils.toJsonString(response));

                        messageService.saveSuccessMessage(message, messageId, result);
                    } else {
                        sseEmitter.send(messageVo);
                    }
                } catch (Exception e) {
                    log.error("sse发送异常", e);
                }
            } else {
                try {
                    sseEmitter.send("[ERROR]");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                webSocket.close(1000, "结束");
            }
        }
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        log.error("发生了错误：", t);
        super.onFailure(webSocket, t, response);
        messageService.saveFailMessage(message, messageId, t.getMessage());
    }

}
