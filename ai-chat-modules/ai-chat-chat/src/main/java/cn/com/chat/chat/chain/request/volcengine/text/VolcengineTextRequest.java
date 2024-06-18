package cn.com.chat.chat.chain.request.volcengine.text;

import cn.com.chat.chat.chain.request.base.text.TextRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-06-18
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class VolcengineTextRequest extends TextRequest {

    /**
     * 模型推理接入点，需要在火山方舟中创建并获取
     */
    private String model;

    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty;

    /**
     * 修改指定 token 在模型输出内容中出现的概率
     */
    @JsonProperty("logit_bias")
    private Map<String, String> logitBias;

    /**
     * 是否返回输出 tokens 的 logprobs
     */
    private Boolean logprobs;

    /**
     * 指定每个 token 位置最有可能返回的token数量
     */
    @JsonProperty("top_logprobs")
    private Integer topLogprobs;

    /**
     * 模型最大输出 token 数
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    /**
     * 用于指定模型在生成响应时应停止的词语
     */
    private String stop;

    /**
     * 流式响应参数
     */
    @JsonProperty("stream_options")
    private VolcengineStreamOption streamOptions;

}
