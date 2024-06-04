package cn.com.chat.chat.service;

import cn.com.chat.chat.chain.enums.ImageChatType;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.domain.vo.MessageVO;

/**
 * TODO
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-02
 */
public interface IMessageService {

    MessageVO textChat(TextChatType type, Long chatId, String content);

    MessageVO imageChat(ImageChatType type, Long chatId, String content);

}
