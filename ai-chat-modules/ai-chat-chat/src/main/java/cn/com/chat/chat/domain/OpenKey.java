package cn.com.chat.chat.domain;

import cn.com.chat.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 模型配置对象 gpt_open_key
 *
 * @author JiaZH
 * @date 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gpt_open_key")
public class OpenKey extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 模型类型（1-文本，2-图像，3-视觉，4-语音）
     */
    private Long type;

    /**
     * 模型值
     */
    private String value;

    /**
     * 模型名称
     */
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
    private String appSecret;

    /**
     * 排序
     */
    private Long sort;

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
