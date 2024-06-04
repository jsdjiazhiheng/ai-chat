package cn.com.chat.chat.domain;

import cn.com.chat.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 搜索引擎对象 gpt_search_engine
 *
 * @author JiaZH
 * @date 2024-05-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gpt_search_engine")
public class SearchEngine extends TenantEntity {

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
     * 值
     */
    private String value;

    /**
     * 是否使用
     */
    private Long used;

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
