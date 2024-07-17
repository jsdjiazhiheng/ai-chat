package cn.com.chat.chat.chain.request.zhipu.vision;

import cn.com.chat.chat.chain.request.base.vision.VisionRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-07-17
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ZhiPuVisionRequest extends VisionRequest {

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
     * 终端用户的唯一ID
     */
    @JsonProperty("user_id")
    private String userId;

}
