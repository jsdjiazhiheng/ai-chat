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
 * 对话对象 gpt_chat
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gpt_chat")
public class Chat extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容类型：text：文字 image : 图片
     */
    private String contentType;

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
