package cn.com.chat.chat.domain;

import cn.com.chat.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * AI助手对象 gpt_assistant
 *
 * @author JiaZH
 * @date 2024-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gpt_assistant")
public class Assistant extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 助手名称
     */
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * 所属模型
     */
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
    private String systemPrompt;

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
