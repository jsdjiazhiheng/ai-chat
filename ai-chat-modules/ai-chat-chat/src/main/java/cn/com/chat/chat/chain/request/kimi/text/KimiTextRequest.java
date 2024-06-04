package cn.com.chat.chat.chain.request.kimi.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;
import cn.com.chat.chat.chain.request.base.text.TextRequest;

/**
 * Kimi文本对话请求参数
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-01
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class KimiTextRequest extends TextRequest {

    /**
     * Model ID
     */
    @NonNull
    private String model;

    /**
     * 使用什么采样温度
     */
    @Min(message = "最小值不能小于0", value = 0)
    @Max(message = "最大值不能大于1", value = 1)
    @Builder.Default
    private Float temperature = 0.3f;

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

    /**
     * 为每条输入消息生成多少个结果
     */
    @Min(message = "最小值不能小于1", value = 1)
    @Max(message = "最大值不能大于5", value = 5)
    @Builder.Default
    private Integer n = 1;

}
