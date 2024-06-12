package cn.com.chat.chat.domain.bo;

import cn.com.chat.chat.domain.Model;
import cn.com.chat.common.core.validate.AddGroup;
import cn.com.chat.common.core.validate.EditGroup;
import cn.com.chat.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 模型信息业务对象 gpt_model
 *
 * @author JiaZH
 * @date 2024-05-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Model.class, reverseConvertGenerate = false)
public class ModelBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String name;

    /**
     * 图标
     */
    @NotNull(message = "图标不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long icon;

    /**
     * 模型值
     */
    @NotBlank(message = "模型值不能为空", groups = {AddGroup.class, EditGroup.class})
    private String value;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户Id
     */
    private Long userId;


}
