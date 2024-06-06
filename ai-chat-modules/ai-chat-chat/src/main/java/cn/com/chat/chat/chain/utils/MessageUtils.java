package cn.com.chat.chat.chain.utils;

import cn.com.chat.chat.chain.enums.Role;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.response.base.Usage;
import cn.com.chat.chat.chain.response.base.image.ImageResult;
import cn.com.chat.chat.chain.response.base.text.TextResult;
import cn.com.chat.chat.domain.bo.ChatMessageBo;
import cn.com.chat.chat.domain.vo.ChatMessageVo;
import cn.com.chat.chat.enums.ContentTypeEnums;
import cn.com.chat.common.json.utils.JsonUtils;

import java.util.Objects;

/**
 * 消息工具类
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-23
 */
public class MessageUtils {

    public static ChatMessageVo buildTextMessage(Long chatId, String messageId, String parentMessageId, TextResult textResult) {
        ChatMessageVo messageVo = new ChatMessageVo();
        messageVo.setChatId(chatId);
        messageVo.setMessageId(messageId);
        messageVo.setParentMessageId(parentMessageId);
        messageVo.setModel(textResult.getModel());
        messageVo.setModelVersion(textResult.getVersion());
        messageVo.setRole(Role.ASSISTANT.getName());
        messageVo.setContent(textResult.getContent());
        messageVo.setContentType(ContentTypeEnums.TEXT.name());
        messageVo.setFinishReason(textResult.getFinishReason());
        messageVo.setStatus(2L);
        messageVo.setTotalTokens(textResult.getTotalTokens());
        return messageVo;
    }

    public static ChatMessageBo buildTextChatMessage(Long chatId, String messageId, String parentMessageId, TextResult textResult, Long userId) {
        return ChatMessageBo.builder()
            .chatId(chatId)
            .messageId(messageId)
            .parentMessageId(parentMessageId)
            .model(textResult.getModel())
            .modelVersion(textResult.getVersion())
            .role(Role.ASSISTANT.getName())
            .content(textResult.getContent())
            .contentType(ContentTypeEnums.TEXT.name())
            .status(2L)
            .totalTokens(textResult.getTotalTokens())
            .finishReason(textResult.getFinishReason())
            .response(textResult.getResponse())
            .userId(userId)
            .build();
    }

    public static ChatMessageVo buildImageMessage(Long chatId, String messageId, String parentMessageId, ImageResult imageResult) {
        ChatMessageVo messageVo = new ChatMessageVo();
        messageVo.setChatId(chatId);
        messageVo.setMessageId(messageId);
        messageVo.setParentMessageId(parentMessageId);
        messageVo.setModel(imageResult.getModel());
        messageVo.setModelVersion(imageResult.getVersion());
        messageVo.setRole(Role.ASSISTANT.getName());
        messageVo.setImageList(imageResult.getData().stream().map(ImageUtils::getImageUrl).toList());
        messageVo.setContentType(ContentTypeEnums.IMAGE.name());
        messageVo.setStatus(2L);
        messageVo.setTotalTokens(imageResult.getTotalTokens());
        return messageVo;
    }

    public static ChatMessageBo buildImageChatMessage(Long chatId, String messageId, String parentMessageId, ImageResult imageResult, Long userId) {
        return ChatMessageBo.builder()
            .chatId(chatId)
            .messageId(messageId)
            .parentMessageId(parentMessageId)
            .model(imageResult.getModel())
            .modelVersion(imageResult.getVersion())
            .role(Role.ASSISTANT.getName())
            .images(JsonUtils.toJsonString(imageResult.getData()))
            .contentType(ContentTypeEnums.IMAGE.name())
            .status(2L)
            .totalTokens(imageResult.getTotalTokens())
            .response(imageResult.getResponse())
            .userId(userId)
            .build();
    }

    public static void handleResult(TextResult result, String content, String finishReason, Usage usage) {
        result.setContent(content);
        result.setFinishReason(finishReason);
        if (Objects.nonNull(usage)) {
            result.setTotalTokens(Long.valueOf(usage.getTotalTokens()));
        }
    }

}
