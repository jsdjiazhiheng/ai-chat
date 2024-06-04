package cn.com.chat.chat.chain.request.zhipu.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import cn.com.chat.chat.chain.request.base.text.TextRequest;

import java.util.List;

/**
 * 智谱文本请求参数
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-15
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ZhiPuTextRequest extends TextRequest {

    /**
     * 模型
     */
    private String model;

    /**
     * 用于区分每次请求的唯一标识
     */
    @JsonProperty("request_id")
    private String requestId;

    /**
     * do_sample 为 true 时启用采样策略，do_sample 为 false 时采样策略 temperature、top_p 将不生效。默认值为 true
     */
    @JsonProperty("do_sample")
    private Boolean doSample;

    /**
     * 可供模型调用的工具。默认开启web_search
     */
    private List<ZhiPuTextTools> tools;

    /**
     * 用于控制模型是如何选择要调用的函数，仅当工具类型为function时补充。默认为auto，当前仅支持auto
     */
    @JsonProperty("tool_choice")
    private String toolChoice;

    /**
     * 终端用户的唯一ID
     */
    @JsonProperty("user_id")
    private String userId;

}
