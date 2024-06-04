package cn.com.chat.chat.domain.bo;

import cn.com.chat.chat.domain.Chat;
import cn.com.chat.common.mybatis.core.domain.BaseEntity;
import cn.com.chat.common.core.validate.AddGroup;
import cn.com.chat.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 对话业务对象 gpt_chat
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Chat.class, reverseConvertGenerate = false)
public class ChatBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 内容类型：text：文字 image : 图片
     */
    @NotBlank(message = "内容类型：text：文字 image : 图片不能为空", groups = { AddGroup.class, EditGroup.class })
    private String contentType;


}
