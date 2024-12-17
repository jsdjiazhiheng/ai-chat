package cn.com.chat.chat.chain.generation.vision.volcengine;

import cn.com.chat.chat.chain.apis.VolcengineApis;
import cn.com.chat.chat.chain.auth.volcengine.VolcengineAccessTokenService;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.enums.model.VolcengineModelEnums;
import cn.com.chat.chat.chain.generation.vision.VisionChatService;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.request.base.vision.VisionMessage;
import cn.com.chat.chat.chain.request.volcengine.text.VolcengineTextRequest;
import cn.com.chat.chat.chain.request.volcengine.vision.VolcengineVisionRequest;
import cn.com.chat.chat.chain.response.base.text.TextResult;
import cn.com.chat.chat.chain.response.base.vision.VisionResult;
import cn.com.chat.chat.chain.response.volcengine.text.VolcengineCompletionResult;
import cn.com.chat.chat.chain.service.MessageService;
import cn.com.chat.chat.chain.utils.ChatLogUtils;
import cn.com.chat.common.http.utils.HttpUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import cn.hutool.core.lang.Assert;
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
public class VolcengineVisionChatService implements VisionChatService {

    private final VolcengineAccessTokenService accessTokenService;
    private final MessageService messageService;

    @Override
    public VisionResult blockCompletion(String model, String system, List<MessageItem> history, String content, List<String> images) {

        String point = VolcengineModelEnums.getPoint(model);

        Assert.notBlank(point, "未找到对应的接入推理点");

        VolcengineVisionRequest request = buildRequest(point, system, history, content, images);

        String response = HttpUtils.doPostJson(VolcengineApis.VISION_API, request, getHeader());

        ChatLogUtils.printResponseLog(this.getClass(), response);

        VolcengineCompletionResult object = JsonUtils.parseObject(response, VolcengineCompletionResult.class);

        String text = Objects.requireNonNull(object).getChoices().get(0).getMessage().getContent();

        VisionResult result = VisionResult.builder()
            .model(TextChatType.VOLCENGINE.name())
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

    private VolcengineVisionRequest buildRequest(String point, String system, List<MessageItem> history, String content, List<String> images) {
        VolcengineVisionRequest request = VolcengineVisionRequest.builder()
            .model(point)
            .messages(VisionMessage.buildMessageList(system, history, content, images))
            .build();

        ChatLogUtils.printRequestLog(this.getClass(), request);

        return request;
    }

    private Map<String, String> getHeader() {
        return Map.of("Authorization", "Bearer " + accessTokenService.getVisionToken());
    }

}
