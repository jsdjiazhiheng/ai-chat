package cn.com.chat.chat.chain.response.aliyun.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 阿里云图片生成使用量
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-06-09
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class AliyunImageUsage implements Serializable {

    @JsonProperty("image_count")
    private Integer imageCount;

}
