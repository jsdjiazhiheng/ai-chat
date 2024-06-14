package cn.com.chat.chat.chain.generation.text.spark.listener;

import cn.com.chat.chat.chain.request.spark.text.SparkTextRequest;
import cn.com.chat.chat.chain.response.spark.text.SparkTextResponse;
import cn.com.chat.chat.chain.response.spark.text.SparkTextResponsePayload;
import cn.com.chat.chat.chain.utils.ChatLogUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CountDownLatch;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-06-11
 */
@Slf4j
public class SparkSyncChatListener extends WebSocketListener {

    @Getter
    private SparkTextResponse response;
    @Getter
    private final CountDownLatch downLatch = new CountDownLatch(1);
    private final SparkTextRequest request;
    private final StringBuilder builder = new StringBuilder();

    public SparkSyncChatListener(SparkTextRequest request) {
        this.request = request;
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        downLatch.countDown();
        super.onFailure(webSocket, t, response);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        ChatLogUtils.printResponseLog(this.getClass(), text);
        SparkTextResponse response = JsonUtils.parseObject(text, SparkTextResponse.class);
        if (response != null) {
            Integer status = response.getHeader().getStatus();
            SparkTextResponsePayload payload = response.getPayload();
            String content = payload.getChoices().getText().get(0).getContent();
            builder.append(content);

            if (status != null && status == 2) {
                response.getPayload().getChoices().getText().get(0).setContent(builder.toString());
                this.response = response;
                downLatch.countDown();
                webSocket.close(1000, "结束");
            }

        }
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        String json = JsonUtils.toJsonString(request);
        if (json != null) {
            webSocket.send(json);
        }
    }

}
