package cn.com.chat.chat.domain.bo;

import cn.com.chat.chat.domain.Assistant;
import cn.com.chat.common.core.validate.AddGroup;
import cn.com.chat.common.core.validate.EditGroup;
import cn.com.chat.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AI助手业务对象 gpt_assistant
 *
 * @author JiaZH
 * @date 2024-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Assistant.class, reverseConvertGenerate = false)
public class AssistantBo extends BaseEntity {

    /**
     * 主键
     */
    @NotBlank(message = "主键不能为空", groups = { EditGroup.class })
    private String id;

    /**
     * 助手名称
     */
    @NotBlank(message = "助手名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * 所属模型
     */
    @NotBlank(message = "所属模型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String model;

    /**
     * 描述
     */
    private String description;

    /**
     * 招呼语
     */
    private String greet;

    /**
     * 系统提示
     */
    @NotBlank(message = "系统提示不能为空", groups = { AddGroup.class, EditGroup.class })
    private String systemPrompt;


}
