package cn.com.chat.chat.chain.request.aliyun.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

/**
 * TODO
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-06-09
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class AliyunImageInput implements Serializable {

    /**
     * 描述画面的提示词信息
     */
    private String prompt;

    /**
     * 画面中不想出现的内容描述词信息
     */
    @JsonProperty("negative_prompt")
    private String negativePrompt;

    /**
     * 输入参考图像的URL
     */
    @JsonProperty("ref_img")
    private String refImg;

}
