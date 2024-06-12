package cn.com.chat.chat.domain;

import cn.com.chat.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 对话消息对象 gpt_chat_message
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gpt_chat_message")
public class ChatMessage extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
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
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 乐观锁
     */
    @Version
    private Long version;

    /**
     * 删除标识
     */
    @TableLogic
    private Long delFlag;


}
