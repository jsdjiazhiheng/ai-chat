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

    /**
     * 文本聊天
     * @param textChatType 文本聊天类型
     * @param system 系统提示词
     * @param history 历史消息
     * @param content 消息
     * @param netWork 是否联网
     * @return 文本聊天结果
     */
    default TextResult blockCompletion(TextChatType textChatType, String system, List<MessageItem> history, String content, Boolean netWork) {
        return null;
    }

    /**
     * 文本聊天
     * @param textChatType 文本聊天类型
     * @param sseEmitter sseEmitter
     * @param system 系统提示词
     * @param history 历史消息
     * @param message 消息
     */
    default void streamCompletion(TextChatType textChatType, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {

    }

    /**
     * 文本聊天
     * @param model 模型
     * @param system 系统提示词
     * @param history 历史消息
     * @param content 消息
     * @return 文本聊天结果
     */
    default TextResult blockCompletion(String model, String system, List<MessageItem> history, String content) {
        return null;
    }

    /**
     * 文本聊天
     * @param model 模型
     * @param sseEmitter sseEmitter
     * @param system 系统提示词
     * @param history 历史消息
     * @param message 消息
     */
    default void streamCompletion(String model, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {

    }

}
