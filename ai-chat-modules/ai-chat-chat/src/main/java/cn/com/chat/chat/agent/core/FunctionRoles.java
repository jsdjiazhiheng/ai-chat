package cn.com.chat.chat.agent.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-05-11
 */
@Getter
@AllArgsConstructor
public enum FunctionRoles {

    FUNCTION("任务决策者", "决策"),
    AI_FUNCTION("任务决策者", "决策"),
    TOOL_FUNCTION("任务决策者", "决策"),
    ;

    private final String role;

    private final String stepName;

}
