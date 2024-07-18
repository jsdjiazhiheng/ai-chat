package cn.com.chat.chat.chain.generation.vision.spark;

import cn.com.chat.chat.chain.auth.spark.SparkAccessTokenService;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.enums.model.SparkModelEnums;
import cn.com.chat.chat.chain.generation.text.spark.listener.SparkSyncChatListener;
import cn.com.chat.chat.chain.generation.vision.VisionChatService;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.request.spark.SparkRequestHeader;
import cn.com.chat.chat.chain.request.spark.SparkRequestMessage;
import cn.com.chat.chat.chain.request.spark.SparkRequestPayload;
import cn.com.chat.chat.chain.request.spark.text.SparkTextChatParameter;
import cn.com.chat.chat.chain.request.spark.text.SparkTextRequest;
import cn.com.chat.chat.chain.request.spark.text.SparkTextRequestParameter;
import cn.com.chat.chat.chain.response.base.vision.VisionResult;
import cn.com.chat.chat.chain.response.spark.text.SparkTextResponse;
import cn.com.chat.chat.chain.service.MessageService;
import cn.com.chat.chat.chain.utils.ChatLogUtils;
import cn.com.chat.chat.chain.utils.ImageUtils;
import cn.com.chat.common.core.utils.StringUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-07-16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SparkVisionChatService implements VisionChatService {

    private final SparkAccessTokenService accessTokenService;
    private final MessageService messageService;
    private final OkHttpClient client;

    @Override
    public VisionResult blockCompletion(String model, String system, List<MessageItem> history, String content, List<String> images) {
        SparkModelEnums enums = SparkModelEnums.getByModel(model);

        String url = Objects.requireNonNull(enums).getUrl();
        String domain = enums.getDomain();

        String authUrl = accessTokenService.getAuthUrl(url, true);

        SparkTextRequest sparkTextRequest = buildRequest(domain, system, history, content, images);

        SparkSyncChatListener listener = new SparkSyncChatListener(sparkTextRequest);

        Request request = new Request.Builder().url(authUrl).build();

        client.newWebSocket(request, listener);

        try {
            listener.getDownLatch().await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        SparkTextResponse response = listener.getResponse();

        VisionResult result = VisionResult.builder()
            .model(TextChatType.SPARK.name())
            .version(model)
            .content(response.getPayload().getChoices().getText().get(0).getContent())
            .totalTokens(response.getPayload().getUsage().getText().getTotalTokens().longValue())
            .response(JsonUtils.toJsonString(response))
            .build();

        ChatLogUtils.printResultLog(this.getClass(), result);

        return result;
    }

    @Override
    public void streamCompletion(String model, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {
        VisionChatService.super.streamCompletion(model, sseEmitter, system, history, message);
    }

    private SparkTextRequest buildRequest(String domain, String system, List<MessageItem> history, String content, List<String> images) {

        SparkTextChatParameter chat = SparkTextChatParameter.builder().domain(domain).build();

        SparkRequestMessage message;
        if (CollUtil.isNotEmpty(images)) {
            List<MessageItem> messages = new ArrayList<>();
            images.forEach(item -> messages.add(MessageItem.buildUser(ImageUtils.urlToBase64(item, false), "image")));
            messages.add(MessageItem.buildUser(content));
            message = SparkRequestMessage.builder().text(messages).build();
        } else {
            List<MessageItem> messages = new ArrayList<>();
            for (MessageItem item : history) {
                if (StringUtils.isNotBlank(item.getImages())) {
                    messages.clear();
                    List<String> list = StringUtils.splitList(item.getImages(), ",");
                    list.forEach(image -> messages.add(MessageItem.buildUser(ImageUtils.urlToBase64(image, false), "image")));
                    messages.add(MessageItem.buildUser(item.getContent()));
                } else {
                    messages.add(MessageItem.buildMessage(item.getRole(), item.getContent()));
                }
            }
            message = SparkRequestMessage.builder().text(messages).build();
        }

        SparkTextRequest request = SparkTextRequest.builder()
            .header(SparkRequestHeader.builder().appId(accessTokenService.getAppid()).build())
            .parameter(SparkTextRequestParameter.builder().chat(chat).build())
            .payload(SparkRequestPayload.builder().message(message).build())
            .build();

        ChatLogUtils.printRequestLog(this.getClass(), request);

        return request;
    }

}
