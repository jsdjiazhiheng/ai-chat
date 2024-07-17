package cn.com.chat.chat.chain.request.base.vision;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-07-17
 */
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class VisionRequest implements Serializable {

    /**
     * 包含迄今为止对话的消息列表
     */
    @NotNull
    protected List<VisionMessage> messages;

    /**
     * 使用什么采样温度
     */
    protected Float temperature;

    /**
     * 另一种采样方法，即模型考虑概率质量为 top_p 的标记的结果
     */
    @JsonProperty("top_p")
    protected Float topP;

    /**
     * 是否流式返回
     */
    @Builder.Default
    protected Boolean stream = false;

}
