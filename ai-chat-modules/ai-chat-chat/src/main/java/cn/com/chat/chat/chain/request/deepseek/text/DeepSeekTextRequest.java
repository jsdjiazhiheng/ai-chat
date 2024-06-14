package cn.com.chat.chat.chain.request.deepseek.text;

import cn.com.chat.chat.chain.request.base.text.TextRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-05-09
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class DeepSeekTextRequest extends TextRequest {

    /**
     * Model ID
     */
    @NotNull
    private String model;

    /**
     * 使用什么采样温度
     */
    @Min(message = "最小值不能小于0", value = 0)
    @Max(message = "最大值不能大于2", value = 2)
    @Builder.Default
    private Float temperature = 1f;

    /**
     * 另一种采样方法，即模型考虑概率质量为 top_p 的标记的结果
     */
    @Builder.Default
    @JsonProperty("top_p")
    private Float topP = 1.0f;

    /**
     * 聊天完成时生成的最大 token 数
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens;

}
