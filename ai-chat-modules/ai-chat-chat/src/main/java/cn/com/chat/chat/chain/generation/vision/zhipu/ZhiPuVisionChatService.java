package cn.com.chat.chat.chain.generation.vision.zhipu;

import cn.com.chat.chat.chain.apis.ZhiPuApis;
import cn.com.chat.chat.chain.auth.zhipu.ZhiPuAccessTokenService;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.generation.vision.VisionChatService;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.request.base.vision.VisionMessage;
import cn.com.chat.chat.chain.request.zhipu.vision.ZhiPuVisionRequest;
import cn.com.chat.chat.chain.response.base.vision.VisionResult;
import cn.com.chat.chat.chain.response.zhipu.text.ZhiPuCompletionResult;
import cn.com.chat.chat.chain.service.MessageService;
import cn.com.chat.chat.chain.utils.ChatLogUtils;
import cn.com.chat.common.http.utils.HttpUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
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
public class ZhiPuVisionChatService implements VisionChatService {

    private final ZhiPuAccessTokenService accessTokenService;
    private final MessageService messageService;

    @Override
    public VisionResult blockCompletion(String model, String system, List<MessageItem> history, String content, List<String> images) {

        ZhiPuVisionRequest request = buildRequest(model, system, history, content, images);

        Map<String, String> header = getHeader();

        String response = HttpUtils.doPostJson(ZhiPuApis.CHAT_API, request, header);

        ChatLogUtils.printResponseLog(this.getClass(), response);

        ZhiPuCompletionResult object = JsonUtils.parseObject(response, ZhiPuCompletionResult.class);

        String text = Objects.requireNonNull(object).getChoices().get(0).getMessage().getContent();

        VisionResult result = VisionResult.builder()
            .model(TextChatType.ZHIPU.name())
            .version(model)
            .content(text)
            .totalTokens(Long.valueOf(object.getUsage().getTotalTokens()))
            .finishReason(object.getChoices().get(0).getFinishReason())
            .response(JsonUtils.toJsonString(object))
            .build();

        ChatLogUtils.printResultLog(this.getClass(), result);

        return result;
    }

    @Override
    public void streamCompletion(String model, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {
        VisionChatService.super.streamCompletion(model, sseEmitter, system, history, message);
    }

    private ZhiPuVisionRequest buildRequest(String model, String system, List<MessageItem> history, String content, List<String> images) {

        List<VisionMessage> messageItems = VisionMessage.buildMessageList(system, null, content, images);

        ZhiPuVisionRequest request = ZhiPuVisionRequest.builder()
            .model(model)
            .messages(messageItems)
            .build();

        ChatLogUtils.printRequestLog(this.getClass(), request);

        return request;
    }

    private Map<String, String> getHeader() {
        return Map.of("Authorization", "Bearer " + accessTokenService.getAccessToken());
    }

}
