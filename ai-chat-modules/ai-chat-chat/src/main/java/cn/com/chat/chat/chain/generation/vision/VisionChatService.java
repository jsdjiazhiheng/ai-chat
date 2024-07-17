package cn.com.chat.chat.chain.generation.vision;

import cn.com.chat.chat.chain.enums.VisionChatType;
import cn.com.chat.chat.chain.generation.GenerationService;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.response.base.vision.VisionResult;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-06-25
 */
public interface VisionChatService extends GenerationService {

    default VisionResult blockCompletion(VisionChatType chatType, String system, List<MessageItem> history, String content, List<String> images, Boolean netWork) {
        return null;
    }

    default void streamCompletion(VisionChatType chatType, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {

    }

    default VisionResult blockCompletion(String model, String system, List<MessageItem> history, String content, List<String> images) {
        return null;
    }

    default void streamCompletion(String model, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {

    }

}
