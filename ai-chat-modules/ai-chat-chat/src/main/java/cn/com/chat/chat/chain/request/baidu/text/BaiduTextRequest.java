package cn.com.chat.chat.chain.request.baidu.text;

import cn.com.chat.chat.chain.request.base.text.TextRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 百度文本对话请求参数
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-04
 */
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class BaiduTextRequest extends TextRequest {

    /**
     * 使用什么采样温度
     */
    @Builder.Default
    private Float temperature = 0.8f;

    /**
     * 另一种采样方法，即模型考虑概率质量为 top_p 的标记的结果
     */
    @JsonProperty("top_p")
    @Builder.Default
    private Float topP = 0.8f;

    /**
     * 模型人设，主要用于人设设定
     */
    private String system;

    /**
     * 是否强制关闭实时搜索功能，默认false，表示不关闭
     */
    @JsonProperty("disable_search")
    private Boolean disableSearch;

    /**
     * 是否返回搜索溯源信息
     */
    @JsonProperty("enable_trace")
    private Boolean enableTrace;

    /**
     * 指定响应内容的格式 默认为text
     * 可选值：
     * json_object：以json格式返回，可能出现不满足效果情况
     * text：以文本格式返回
     */
    @JsonProperty("response_format")
    private String responseFormat;

    /**
     * 表示最终用户的唯一标识符
     */
    @JsonProperty("user_id")
    private String userId;


}
