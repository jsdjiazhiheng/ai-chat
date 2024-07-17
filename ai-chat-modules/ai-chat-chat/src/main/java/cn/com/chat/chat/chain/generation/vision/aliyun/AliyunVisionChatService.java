package cn.com.chat.chat.chain.generation.vision.aliyun;

import cn.com.chat.chat.chain.apis.AliyunApis;
import cn.com.chat.chat.chain.auth.aliyun.AliyunAccessTokenService;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.generation.vision.VisionChatService;
import cn.com.chat.chat.chain.request.aliyun.text.AliyunTextParameter;
import cn.com.chat.chat.chain.request.aliyun.vision.AliyunMessage;
import cn.com.chat.chat.chain.request.aliyun.vision.AliyunVisionInput;
import cn.com.chat.chat.chain.request.aliyun.vision.AliyunVisionRequest;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.response.aliyun.text.AliyunCompletionResult;
import cn.com.chat.chat.chain.response.aliyun.vision.AliyunVisionResult;
import cn.com.chat.chat.chain.response.base.vision.VisionResult;
import cn.com.chat.chat.chain.service.MessageService;
import cn.com.chat.chat.chain.utils.ChatLogUtils;
import cn.com.chat.common.http.utils.HttpUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
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
public class AliyunVisionChatService implements VisionChatService {

    private final AliyunAccessTokenService accessTokenService;
    private final MessageService messageService;

    @Override
    public VisionResult blockCompletion(String model, String system, List<MessageItem> history, String content, List<String> images) {

        AliyunVisionRequest request = buildRequest(model, system, history, content, images);

        Map<String, String> header = getHeader(false);

        String response = HttpUtils.doPostJson(AliyunApis.QWEN_AIGC_API, request, header);

        ChatLogUtils.printResponseLog(this.getClass(), response);

        AliyunVisionResult completionResult = JsonUtils.parseObject(response, AliyunVisionResult.class);

        String text = Objects.requireNonNull(completionResult).getOutput().getChoices().get(0).getMessage().getContent().get(0).getText();

        long totalTokens = completionResult.getUsage().getInputTokens() + completionResult.getUsage().getOutputTokens();

        VisionResult result = VisionResult.builder()
            .model(TextChatType.ALIYUN.name())
            .version(model)
            .content(text)
            .totalTokens(totalTokens)
            .finishReason(completionResult.getOutput().getChoices().get(0).getFinishReason())
            .response(JsonUtils.toJsonString(completionResult))
            .build();

        ChatLogUtils.printResultLog(this.getClass(), result);

        return result;
    }

    @Override
    public void streamCompletion(String model, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {
        VisionChatService.super.streamCompletion(model, sseEmitter, system, history, message);
    }

    private AliyunVisionRequest buildRequest(String model, String system, List<MessageItem> history, String content, List<String> images) {
        AliyunVisionRequest request = AliyunVisionRequest.builder()
            .model(model)
            .input(AliyunVisionInput.builder()
                .messages(AliyunMessage.buildMessageList(system, history, content, images))
                .build())
            .parameter(AliyunTextParameter.builder().resultFormat("text").build())
            .build();

        ChatLogUtils.printRequestLog(this.getClass(), request);

        return request;
    }

    private Map<String, String> getHeader(boolean isSse) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessTokenService.getAccessToken());
        if (isSse) {
            headers.put("X-DashScope-SSE", "enable");
        }
        return headers;
    }

}
