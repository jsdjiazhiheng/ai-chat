package cn.com.chat.chat.domain.bo;

import cn.com.chat.chat.domain.ChatMessage;
import lombok.Builder;
import cn.com.chat.common.mybatis.core.domain.BaseEntity;
import cn.com.chat.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 对话消息业务对象 gpt_chat_message
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ChatMessage.class, reverseConvertGenerate = false)
public class ChatMessageBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 对话ID
     */
    private Long chatId;

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 回复消息ID
     */
    private String parentMessageId;

    /**
     * 模型
     */
    private String model;

    /**
     * 模型版本
     */
    private String modelVersion;

    /**
     * 角色
     */
    private String role;

    /**
     * 文本内容
     */
    private String content;

    /**
     * 图片内容
     */
    private String images;

    /**
     * 内容类型：text：文字 image : 图片
     */
    private String contentType;

    /**
     * 结束原因
     */
    private String finishReason;

    /**
     * 状态
     */
    private Long status;

    /**
     * 使用token
     */
    private Long totalTokens;

    /**
     * 响应全文
     */
    private String response;

    /**
     * 用户Id
     */
    private Long userId;

}
