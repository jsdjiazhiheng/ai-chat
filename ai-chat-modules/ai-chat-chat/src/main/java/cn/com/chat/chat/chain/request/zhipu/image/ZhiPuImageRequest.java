package cn.com.chat.chat.chain.request.zhipu.image;

import cn.com.chat.chat.chain.request.base.image.ImageRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 智谱图像请求参数
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ZhiPuImageRequest extends ImageRequest {

    private String model;

    @JsonProperty("user_id")
    private String userId;

}
