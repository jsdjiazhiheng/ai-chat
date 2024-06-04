package cn.com.chat.chat.chain.generation.function.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-05-11
 */
@Data
@AllArgsConstructor
public class FunctionParameters {

    private String type = "object";
    private Map<String,FunctionParametersFieldValue> properties;

}
