package cn.com.chat.chat.chain.request.baidu.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;
import cn.com.chat.chat.chain.request.base.image.ImageRequest;

/**
 * 百度图像请求参数
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-21
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class BaiduImageRequest extends ImageRequest {

    @JsonProperty("negative_prompt")
    private String negativePrompt;

    @Builder.Default
    private String size = "1024x1024";

    @Min(1)
    @Max(4)
    @Builder.Default
    private Integer n = 1;

    @Min(10)
    @Max(50)
    @Builder.Default
    private Integer steps = 20;

    @Builder.Default
    @JsonProperty("sampler_index")
    private String samplerIndex = "Euler a";

    @Min(0)
    @Max(4294967295L)
    private Long seed;

    @Max(30)
    @Min(0)
    @Builder.Default
    @JsonProperty("cfg_scale")
    private Float cfgScale = 5f;

    @Builder.Default
    private String style = "Base";

    @JsonProperty("user_id")
    private String userId;

}
