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

    USER("user"),
    ASSISTANT("assistant"),
    SYSTEM("system"),
    TOOL("tool");
    private final String name;

}
