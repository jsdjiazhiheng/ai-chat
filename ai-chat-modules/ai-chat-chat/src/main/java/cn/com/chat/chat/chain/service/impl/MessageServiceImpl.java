package cn.com.chat.chat.chain.service.impl;

import cn.com.chat.chat.chain.enums.MessageStatus;
import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.response.base.text.TextResult;
import cn.com.chat.chat.chain.service.MessageService;
import cn.com.chat.chat.chain.utils.MessageUtils;
import cn.com.chat.chat.domain.bo.ChatMessageBo;
import cn.com.chat.chat.service.IChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 消息服务实现
 *
 * @author JiaZH
 * @date 2024-06-07
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final IChatMessageService chatMessageService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSuccessMessage(StreamMessage message, String messageId, TextResult result) {
        ChatMessageBo messageBo = MessageUtils.buildTextChatMessage(message.getChatId(), messageId, message.getMessageId(), result, message.getUserId(), message.getTenantId(), message.getDeptId());
        chatMessageService.insertByBo(messageBo);
        chatMessageService.updateStatusByMessageId(message.getMessageId(), MessageStatus.SUCCESS.getStatus());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveFailMessage(StreamMessage message, String messageId, String errorMessage) {
        chatMessageService.updateStatusByMessageId(message.getMessageId(), MessageStatus.FAIL.getStatus());
    }

}
