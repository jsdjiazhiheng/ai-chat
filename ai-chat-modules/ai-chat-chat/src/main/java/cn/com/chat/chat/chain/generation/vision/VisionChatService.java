package cn.com.chat.chat.chain.generation.vision;

import cn.com.chat.chat.chain.generation.GenerationService;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.response.base.vision.VisionResult;

import java.util.List;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-06-25
 */
public interface VisionChatService extends GenerationService {

    VisionResult blockCompletion(String model, String system, List<MessageItem> history, String content);

}
