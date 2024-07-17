package cn.com.chat.chat.chain.generation.vision.volcengine;

import cn.com.chat.chat.chain.generation.vision.VisionChatService;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.response.base.vision.VisionResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

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

    @Override
    public VisionResult blockCompletion(String model, String system, List<MessageItem> history, String content, List<String> images) {
        return VisionChatService.super.blockCompletion(model, system, history, content, images);
    }

    @Override
    public void streamCompletion(String model, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {
        VisionChatService.super.streamCompletion(model, sseEmitter, system, history, message);
    }
}
