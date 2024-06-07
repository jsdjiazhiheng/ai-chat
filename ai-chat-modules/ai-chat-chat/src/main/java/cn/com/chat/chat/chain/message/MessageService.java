package cn.com.chat.chat.chain.message;

import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.response.base.text.TextResult;

/**
 * 消息服务
 *
 * @author JiaZH
 * @date 2024-06-07
 */
public interface MessageService {

    void saveSuccessMessage(StreamMessage message, String messageId, TextResult result);

    void saveFailMessage(StreamMessage message, String messageId, String errorMessage);

}
