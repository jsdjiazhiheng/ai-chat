package cn.com.chat.chat.agent.core.function;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 能力类型枚举
 *
 * @author JiaZH
 * @date 2024-05-11
 */
@AllArgsConstructor
@Getter
public enum FunctionType {

    /**
     * 工具
     */
    TOOL("工具"),
    /**
     * AI
     */
    AI("AI"),
    ;

    private final String type;

}
