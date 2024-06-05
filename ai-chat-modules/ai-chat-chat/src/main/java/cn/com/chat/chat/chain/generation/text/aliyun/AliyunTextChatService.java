package cn.com.chat.chat.chain.generation.text.aliyun;

import cn.com.chat.chat.chain.apis.AliyunApis;
import cn.com.chat.chat.chain.auth.aliyun.AliyunAccessTokenService;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.generation.text.TextChatService;
import cn.com.chat.chat.chain.request.aliyun.text.AliyunTextInput;
import cn.com.chat.chat.chain.request.aliyun.text.AliyunTextParameter;
import cn.com.chat.chat.chain.request.aliyun.text.AliyunTextRequest;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.response.aliyun.text.AliyunCompletionResult;
import cn.com.chat.chat.chain.response.base.text.TextResult;
import cn.com.chat.chat.chain.utils.HttpUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Objects;

/**
 * 阿里云文本聊天
 *
 * @author JiaZH
 * @date 2024-06-05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliyunTextChatService implements TextChatService {

    private final AliyunAccessTokenService aliyunAccessTokenService;


    @Override
    public TextResult blockCompletion(String model, String system, List<MessageItem> history, String content) {

        AliyunTextRequest request = buildRequest(model, system, history, content);

        HttpHeaders header = getHeader();

        HttpEntity<AliyunTextRequest> entity = new HttpEntity<>(request, header);

        String response = HttpUtils.getRestTemplate().postForObject(AliyunApis.QWEN_API, entity, String.class);

        log.info("AliyunTextChatService -> 请求结果 ： {}", response);

        AliyunCompletionResult completionResult = JsonUtils.parseObject(response, AliyunCompletionResult.class);

        String text = Objects.requireNonNull(completionResult).getOutput().getChoices().get(0).getMessage().getContent();

        TextResult result = TextResult.builder()
            .model(TextChatType.ALIYUN.name())
            .version(model)
            .content(text)
            .totalTokens(completionResult.getUsage().getTotalTokens().longValue())
            .finishReason(completionResult.getOutput().getChoices().get(0).getFinishReason())
            .response(JsonUtils.toJsonString(completionResult))
            .build();

        log.info("AliyunTextChatService -> 返回结果 ： {}", result);

        return result;

    }

    @Override
    public void streamCompletion(String model, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {

    }

    private AliyunTextRequest buildRequest(String model, String system, List<MessageItem> history, String content) {
        return AliyunTextRequest.builder()
            .model(model)
            .input(AliyunTextInput.builder()
                .messages(MessageItem.buildMessageList(system, history, content))
                .build())
            .parameter(AliyunTextParameter.builder().resultFormat("message").build())
            .build();
    }

    private HttpHeaders getHeader() {
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + aliyunAccessTokenService.getAccessToken());
        return header;
    }

}
