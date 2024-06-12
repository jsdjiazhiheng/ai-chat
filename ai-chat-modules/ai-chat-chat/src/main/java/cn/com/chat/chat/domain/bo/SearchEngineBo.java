package cn.com.chat.chat.domain.bo;

import cn.com.chat.chat.domain.SearchEngine;
import cn.com.chat.common.core.validate.AddGroup;
import cn.com.chat.common.core.validate.EditGroup;
import cn.com.chat.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 搜索引擎业务对象 gpt_search_engine
 *
 * @author JiaZH
 * @date 2024-05-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SearchEngine.class, reverseConvertGenerate = false)
public class SearchEngineBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 值
     */
    @NotBlank(message = "值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String value;

    /**
     * 是否使用
     */
    @NotNull(message = "是否使用不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long used;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户Id
     */
    private Long userId;


}
