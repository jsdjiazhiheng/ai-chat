package cn.com.chat.chat.service;

import cn.com.chat.chat.chain.enums.ImageChatType;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.enums.VisionChatType;
import cn.com.chat.chat.domain.vo.MessageVO;

import java.util.List;

/**
 * TODO
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-02
 */
public interface IBlockService {

    MessageVO textChat(String type, Long chatId, String content);

    MessageVO imageChat(String type, Long chatId, String content);

    MessageVO pictureComprehend(String type, Long chatId, String content, String images);

}
