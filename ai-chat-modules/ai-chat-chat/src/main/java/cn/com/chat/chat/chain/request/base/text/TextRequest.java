package cn.com.chat.chat.chain.request.base.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * 文本对话请求参数
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-01
 */
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class TextRequest implements Serializable {

    /**
     * 包含迄今为止对话的消息列表
     */
    @NonNull
    protected List<MessageItem> messages;

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
