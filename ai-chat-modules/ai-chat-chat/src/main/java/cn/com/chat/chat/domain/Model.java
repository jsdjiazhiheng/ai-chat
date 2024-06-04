package cn.com.chat.chat.domain;

import cn.com.chat.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 模型信息对象 gpt_model
 *
 * @author JiaZH
 * @date 2024-05-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gpt_model")
public class Model extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 图标
     */
    private Long icon;

    /**
     * 模型值
     */
    private String value;

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
