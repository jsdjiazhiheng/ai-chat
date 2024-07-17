package cn.com.chat.chat.chain.generation.vision;

import cn.com.chat.chat.chain.enums.VisionChatType;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.response.base.vision.VisionResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
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
@Primary
@RequiredArgsConstructor
public class VisionChatServiceWrapper implements VisionChatService {

    private final VisionChatServiceFactory factory;

    @Override
    public VisionResult blockCompletion(VisionChatType chatType, String system, List<MessageItem> history, String content, List<String> images, Boolean netWork) {
        VisionChatService service = factory.getVisionChatService(chatType);
        String model = factory.getModel(chatType);
        return service.blockCompletion(model, system, history, content, images);
    }

    @Override
    public void streamCompletion(VisionChatType chatType, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {
        VisionChatService service = factory.getVisionChatService(chatType);
        String model = factory.getModel(chatType);
        service.streamCompletion(model, sseEmitter, system, history, message);
    }

}
