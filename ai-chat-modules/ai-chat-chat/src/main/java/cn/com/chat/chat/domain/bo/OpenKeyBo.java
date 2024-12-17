package cn.com.chat.chat.domain.bo;

import cn.com.chat.chat.domain.OpenKey;
import cn.com.chat.common.core.validate.AddGroup;
import cn.com.chat.common.core.validate.EditGroup;
import cn.com.chat.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 模型配置业务对象 gpt_open_key
 *
 * @author JiaZH
 * @date 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = OpenKey.class, reverseConvertGenerate = false)
public class OpenKeyBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 模型类型（1-文本，2-图像，3-视觉，4-语音）
     */
    @NotNull(message = "模型类型（1-文本，2-图像，3-视觉，4-语音）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long type;

    /**
     * 模型值
     */
    @NotBlank(message = "模型值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String value;

    /**
     * 模型名称
     */
    @NotBlank(message = "模型名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * AppID
     */
    private String appId;

    /**
     * AppKey
     */
    private String appKey;

    /**
     * App密钥
     */
    @NotBlank(message = "App密钥不能为空", groups = { AddGroup.class, EditGroup.class })
    private String appSecret;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sort;


}
