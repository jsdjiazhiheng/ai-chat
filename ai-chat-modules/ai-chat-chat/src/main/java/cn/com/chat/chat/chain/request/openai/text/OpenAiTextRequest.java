package cn.com.chat.chat.chain.request.openai.text;

import cn.com.chat.chat.chain.request.base.text.TextRequest;
import cn.com.chat.chat.chain.request.base.text.TextTool;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * OpenAI文本请求参数
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-06-08
 */
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class OpenAiTextRequest extends TextRequest {

    /**
     * Model ID
     */
    @NotNull
    private String model;

    /**
     * 惩罚值
     */
    @Min(message = "最小值不能小于-2", value = -2)
    @Max(message = "最大值不能大于2", value = 2)
    @JsonProperty("frequency_penalty")
    private Float frequencyPenalty;

    /**
     * 修改指定标记在完成中出现的可能性。
     */
    @JsonProperty("logit_bias")
    private Object logit_bias;

    /**
     * 是否返回输出令牌的对数概率。
     */
    private Boolean logprobs;

    /**
     * 介于 0 和 20 之间的整数，指定在每个令牌位置返回的最可能令牌数，每个令牌都具有关联的对数概率
     * 如果使用此参数，则必须设置为 logprobs = true
     */
    @Min(message = "最小值不能小于0", value = 0)
    @Max(message = "最大值不能大于20", value = 20)
    @JsonProperty("top_logprobs")
    private Integer top_logprobs;

    @JsonProperty("max_tokens")
    private Integer max_tokens;

    private Integer n;

    @Min(message = "最小值不能小于-2", value = -2)
    @Max(message = "最大值不能大于2", value = 2)
    @JsonProperty("presence_penalty")
    private Float presencePenalty;

    /**
     * 指定模型必须输出的格式
     * 使用 JSON 模式时，还必须指示模型通过系统或用户消息自行生成 JSON
     */
    @JsonProperty("response_format")
    private OpenAiTextResponseFormat responseFormat;

    /**
     * 种子
     */
    private Integer seed;

    /**
     * 停止 字符串 / 数组 / 空
     */
    private String stop;

    /**
     * 流式处理响应的选项
     */
    @JsonProperty("stream_options")
    private OpenAiTextStreamOptions streamOptions;

    private List<TextTool> tools;

    /*@JsonProperty("tool_choice")
    private Object tool_choice;*/

    /**
     * 是否在工具使用过程中启用并行函数调用。
     * 默认值为 true
     */
    @JsonProperty("parallel_tool_calls")
    private Boolean parallelToolCalls;

    /**
     * 代表您的最终用户的唯一标识符
     */
    private String user;

}
