package cn.com.chat.chat.service.impl;

import cn.com.chat.chat.chain.enums.ImageChatType;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.function.service.ICompletionService;
import cn.com.chat.chat.chain.generation.image.ImageChatService;
import cn.com.chat.chat.chain.generation.text.TextChatService;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.response.base.image.ImageResult;
import cn.com.chat.chat.chain.response.base.text.TextResult;
import cn.com.chat.chat.chain.utils.ImageUtils;
import cn.com.chat.chat.domain.bo.ChatMessageBo;
import cn.com.chat.chat.domain.vo.ChatMessageVo;
import cn.com.chat.chat.domain.vo.MessageVO;
import cn.com.chat.chat.enums.ContentTypeEnums;
import cn.com.chat.chat.service.IAssistantService;
import cn.com.chat.chat.service.IBlockService;
import cn.com.chat.chat.service.IChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BlockServiceImpl implements IBlockService {

    private final TextChatService textChatService;
    private final ImageChatService imageChatService;
    private final ICompletionService completionService;
    private final IChatMessageService chatMessageService;
    private final IAssistantService assistantService;


    @Override
    public MessageVO textChat(TextChatType type, Long chatId, String content) {

        String system = assistantService.getSystemPromptByModel(type.name());

        List<MessageItem> history = chatMessageService.listChatHistory(chatId);

        boolean useNet = completionService.functionSearch(content);

        TextResult textResult = textChatService.blockCompletion(type, system, history, content, useNet);

        ChatMessageBo userMessage = chatMessageService.insertUserMessage(chatId, ContentTypeEnums.TEXT.name(), textResult.getModel(), textResult.getVersion(), content, 2L);

        ChatMessageBo assistantMessage = chatMessageService.insertAssistantMessage(chatId, userMessage.getMessageId(), ContentTypeEnums.TEXT.name(), textResult);

        MessageVO messageVO = new MessageVO();
        messageVO.setUserMessage(chatMessageService.queryById(userMessage.getId()));
        messageVO.setAssistantMessage(chatMessageService.queryById(assistantMessage.getId()));

        return messageVO;
    }

    @Override
    public MessageVO imageChat(ImageChatType type, Long chatId, String content) {
        ImageResult result = imageChatService.blockGenImage(type, content);

        ChatMessageBo userMessage = chatMessageService.insertUserMessage(chatId, ContentTypeEnums.IMAGE.name(), result.getModel(), result.getVersion(), content, 2L);

        ChatMessageBo assistantMessage = chatMessageService.insertAssistantMessage(chatId, userMessage.getMessageId(), ContentTypeEnums.IMAGE.name(), result);

        MessageVO messageVO = new MessageVO();
        messageVO.setUserMessage(chatMessageService.queryById(userMessage.getId()));
        ChatMessageVo messageVo = chatMessageService.queryById(assistantMessage.getId());

        List<String> list = result.getData().stream().map(ImageUtils::getImageUrl).toList();

        messageVo.setImageList(list);
        messageVO.setAssistantMessage(messageVo);

        return messageVO;
    }

}
