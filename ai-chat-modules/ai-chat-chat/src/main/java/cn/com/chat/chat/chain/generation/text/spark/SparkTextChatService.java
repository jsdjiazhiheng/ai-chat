package cn.com.chat.chat.chain.generation.text.spark;

import cn.com.chat.chat.chain.auth.spark.SparkAccessTokenService;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.enums.model.SparkModelEnums;
import cn.com.chat.chat.chain.generation.text.TextChatService;
import cn.com.chat.chat.chain.generation.text.spark.listener.SparkSseChatListener;
import cn.com.chat.chat.chain.generation.text.spark.listener.SparkSyncChatListener;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.request.spark.SparkRequestHeader;
import cn.com.chat.chat.chain.request.spark.SparkRequestMessage;
import cn.com.chat.chat.chain.request.spark.SparkRequestPayload;
import cn.com.chat.chat.chain.request.spark.text.SparkTextChatParameter;
import cn.com.chat.chat.chain.request.spark.text.SparkTextRequest;
import cn.com.chat.chat.chain.request.spark.text.SparkTextRequestParameter;
import cn.com.chat.chat.chain.response.base.text.TextResult;
import cn.com.chat.chat.chain.response.spark.text.SparkTextResponse;
import cn.com.chat.chat.chain.service.MessageService;
import cn.com.chat.common.json.utils.JsonUtils;
import cn.hutool.core.lang.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;

/**
 * 讯飞星火文本聊天服务
 *
 * @author JiaZH
 * @date 2024-06-11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SparkTextChatService implements TextChatService {

    private final SparkAccessTokenService accessTokenService;
    private final MessageService messageService;
    private final OkHttpClient client;

    @Override
    public TextResult blockCompletion(String model, String system, List<MessageItem> history, String content) {

        SparkModelEnums enums = SparkModelEnums.getByModel(model);

        String url = Objects.requireNonNull(enums).getUrl();
        String domain = enums.getDomain();

        String authUrl = accessTokenService.getAuthUrl(url);

        SparkTextRequest sparkTextRequest = buildRequest(domain, system, history, content);

        SparkSyncChatListener listener = new SparkSyncChatListener(sparkTextRequest);

        Request request = new Request.Builder().url(authUrl).build();

        client.newWebSocket(request, listener);

        try {
            listener.getDownLatch().await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        SparkTextResponse response = listener.getResponse();

        TextResult result = TextResult.builder()
            .model(TextChatType.SPARK.name())
            .version(model)
            .content(response.getPayload().getChoices().getText().get(0).getContent())
            .totalTokens(response.getPayload().getUsage().getText().getTotalTokens().longValue())
            .response(JsonUtils.toJsonString(response))
            .build();

        log.info("SparkTextChatService -> 返回结果 ： {}", result);

        return result;
    }

    @Override
    public void streamCompletion(String model, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {
        SparkModelEnums enums = SparkModelEnums.getByModel(model);

        String url = Objects.requireNonNull(enums).getUrl();
        String domain = enums.getDomain();

        String authUrl = accessTokenService.getAuthUrl(url);

        SparkTextRequest sparkTextRequest = buildRequest(domain, system, history, message.getContent());

        Request request = new Request.Builder().url(authUrl).build();

        Flux<SparkTextRequest> flux = Flux.create(emitter -> {
            emitter.next(sparkTextRequest);
            emitter.complete();
        });

        TextResult result = TextResult.builder()
            .model(TextChatType.SPARK.name())
            .version(model)
            .build();

        String messageId = UUID.fastUUID().toString();

        flux.subscribe(consumer -> {

            SparkSseChatListener listener = new SparkSseChatListener(sseEmitter, sparkTextRequest, messageId, result, message, messageService);

            client.newWebSocket(request, listener);

        }, error -> log.error("Spark AI API error: {}", error.getMessage()));

    }

    private SparkTextRequest buildRequest(String domain, String system, List<MessageItem> history, String content) {

        SparkTextChatParameter chat = SparkTextChatParameter.builder().domain(domain).build();
        SparkRequestMessage message = SparkRequestMessage.builder().text(MessageItem.buildMessageList(system, history, content)).build();

        SparkTextRequest request = SparkTextRequest.builder()
            .header(SparkRequestHeader.builder().appId(accessTokenService.getAppid()).build())
            .parameter(SparkTextRequestParameter.builder().chat(chat).build())
            .payload(SparkRequestPayload.builder().message(message).build())
            .build();

        log.info("SparkTextChatService -> 请求参数 ： {}", JsonUtils.toJsonString(request));

        return request;
    }

}
