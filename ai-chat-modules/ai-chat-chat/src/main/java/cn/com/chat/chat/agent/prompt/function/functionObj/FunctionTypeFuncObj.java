package cn.com.chat.chat.agent.prompt.function.functionObj;

import cn.com.chat.chat.agent.core.function.FunctionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-05-11
 */
@Data
public class FunctionTypeFuncObj {

    @JsonPropertyDescription("type of function required to complete task, AI or TOOL")
    @JsonProperty(required = true)
    private FunctionType functionType;

}
