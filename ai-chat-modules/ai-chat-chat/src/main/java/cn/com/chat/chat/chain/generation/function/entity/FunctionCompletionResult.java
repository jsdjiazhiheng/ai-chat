package cn.com.chat.chat.chain.generation.function.entity;

import lombok.Data;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-05-11
 */
@Data
public class FunctionCompletionResult {

    private String type;

    private String name;

    private Object arguments;

}
