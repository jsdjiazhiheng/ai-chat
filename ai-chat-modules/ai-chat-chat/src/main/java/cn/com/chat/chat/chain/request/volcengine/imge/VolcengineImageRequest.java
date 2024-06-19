package cn.com.chat.chat.chain.request.volcengine.imge;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 火山引擎图片请求参数
 *
 * @author JiaZH
 * @date 2024-06-18
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class VolcengineImageRequest implements Serializable {

    /**
     * 算法名称，取固定值为high_aes_general_v14
     */
    @Builder.Default
    @JSONField(name = "req_key")
    private String reqKey = "high_aes_general_v14";

    /**
     * 模型版本名称 取固定值为 general_v1.4
     */
    @Builder.Default
    @JSONField(name = "model_version")
    private String modelVersion = "general_v1.4";

    /**
     * 提示词
     */
    private String prompt;

    /**
     * 源图片，仅支持单图输入
     */
    @JSONField(name = "binary_data_base64")
    private String binaryDataBase64;

    /**
     * 源图片，仅支持单图输入
     */
    @JSONField(name = "image_urls")
    private String imageUrls;

    /**
     * 随机种子，-1为不随机种子
     */
    private Integer seed;

    /**
     * 影响文本描述的程度
     * 默认值：3.0 取值范围[1, 30]
     */
    @Min(1)
    @Max(30)
    private Double scale;

    /**
     * 生成图像的步数
     * 默认值：25 取值范围[1-50]
     */
    @Min(1)
    @Max(50)
    @JSONField(name = "ddim_steps")
    private Integer ddimSteps;

    /**
     * 生成图像的宽
     * 默认值：512，取值范围[128-768]
     */
    @Min(128)
    @Max(768)
    private Integer width;

    /**
     * 生成图像的高
     * 默认值：512，取值范围[128-768]
     */
    @Min(128)
    @Max(768)
    private Integer height;

    /**
     * 开启中文prompt扩写
     * 默认值：True
     */
    @JSONField(name = "use_rephraser")
    private Boolean useRephraser;

    /**
     * 开启predict_tags
     * 默认值：True
     */
    @JSONField(name = "use_predict_tags")
    private Boolean usePredictTags;

    /**
     * 水印信息
     */
    @JSONField(name = "logo_info")
    VolcengineLogoInfo logoInfo;

}
