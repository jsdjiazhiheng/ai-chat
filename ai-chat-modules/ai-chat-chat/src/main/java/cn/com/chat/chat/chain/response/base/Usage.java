package cn.com.chat.chat.chain.response.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文本对话使用token
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-01
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Usage implements Serializable {

    /**
     * 输入token
     */
    @JsonProperty("prompt_tokens")
    private Integer promptTokens;

    /**
     * 输出token
     */
    @JsonProperty("completion_tokens")
    private Integer completionTokens;

    /**
     * 输入token
     */
    @JsonProperty("input_tokens")
    private Integer inputTokens;

    /**
     * 输出token
     */
    @JsonProperty("output_tokens")
    private Integer outputTokens;

    /**
     * 总token
     */
    @JsonProperty("total_tokens")
    private Integer totalTokens;

}
