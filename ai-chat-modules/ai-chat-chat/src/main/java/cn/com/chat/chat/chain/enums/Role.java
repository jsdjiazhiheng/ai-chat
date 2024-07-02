package cn.com.chat.chat.chain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色
 *
 * @author JiaZH
 * @date 2024-06-04
 */
@Getter
@AllArgsConstructor
public enum Role {

    /**
     * 用户
     */
    USER("user"),
    /**
     * 助手
     */
    ASSISTANT("assistant"),
    /**
     * 系统
     */
    SYSTEM("system"),
    /**
     * 工具
     */
    TOOL("tool");
    private final String name;

}
