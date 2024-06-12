package cn.com.chat.chat.chain.generation.text;

import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.generation.GenerationService;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.response.base.text.TextResult;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * 文本聊天服务接口
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-01
 */
public interface TextChatService extends GenerationService {

    default TextResult blockCompletion(TextChatType textChatType, String system, List<MessageItem> history, String content, Boolean netWork) {
        return null;
    }

    default void streamCompletion(TextChatType textChatType, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {

    }

    default TextResult blockCompletion(String model, String system, List<MessageItem> history, String content) {
        return null;
    }

    default void streamCompletion(String model, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {

    }

}
