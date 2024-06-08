package cn.com.chat.chat.chain.request.openai.image;

import cn.com.chat.chat.chain.request.base.image.ImageRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * TODO
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-06-09
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class OpenAiImageRequest extends ImageRequest {

    private String model;

    @Min(value = 1, message = "图片数量不能小于1")
    @Max(value = 10, message = "图片数量不能大于10")
    private Integer n;

    /**
     * 质量 默认standard
     */
    private String quality;

    /**
     * 响应格式
     * url or b64_json
     * 默认url
     */
    @JsonProperty("response_format")
    private String responseFormat;

    /**
     * 图片大小
     * 默认1024x1024
     * dall-e-2 1024x1024 or 512x512 or 256x256
     * dall-e-3 1024x1024 or 1792x1024 or 1024x1792
     */
    private String size;

    /**
     * 风格
     * 默认vivid
     * vivid or natural or dall-e-3
     */
    private String style;

    private String user;

}
