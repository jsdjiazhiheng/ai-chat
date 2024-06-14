package cn.com.chat.chat.chain.request.nolipix;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 画图参数
 *
 * @author JiaZH
 * @date 2024-06-13
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class NoLiPixImageParams implements Serializable {

    /**
     * 生成模型
     * general 通用大模型
     * anime 动漫模型
     * art 艺术模型
     * real 写实模型
     * 3danime 3D 动漫模型
     * basic 画宇宙基础模型，不再推荐使用，推荐使用 `general` 模型代替
     */
    private String model;

    /**
     * 提示词
     * 当任务为 img2img.sd 或 txt2img.sd 时，此字段必填。
     * 内容必须为英文，中文内容需要先调用 提示词预处理接口翻译为英文
     */
    private String text;

    /**
     * 源图地址
     * 当任务为 img2img.sd 时，此参数必填。宽高不能超过 1024
     */
    private String url;

    /**
     * 宽
     */
    @Max(1024)
    @Min(512)
    private Integer w;

    /**
     * 高
     */
    @Max(1024)
    @Min(512)
    private Integer h;

    /**
     * 相似度
     * 当任务类型为 img2img时，此字段有意义
     */
    @Max(1)
    @Min(0)
    private Double fidelity;

    /**
     * 种子
     */
    private Integer seed;

    /**
     * 采样器
     * k_euler 默认
     * ddim
     * plms
     * klms
     * solver
     * k_euler_a
     */
    private String sampler;

    /**
     * 采样步数
     * 默认值 20
     */
    @Min(5)
    @Max(100)
    @JsonProperty("num_steps")
    private Integer numSteps;

    /**
     * 扣题程度
     */
    @Builder.Default
    @JsonProperty("guidance_scale")
    private Double guidanceScale = 7.5;

    /**
     * 负面词
     */
    @JsonProperty("negative_prompt")
    private String negativePrompt;

    /**
     * 「生成同款」时会用到的参数
     */
    private List<NoLiPixImageVariation> variations;

    /**
     * mask 图片地址
     * task 为 img2img.inpainting 时必传，图片中透明度不为 0 的像素，在 url 中会被擦除
     */
    @JsonProperty("mask_url")
    private String maskUrl;

    /**
     * 图像融合强度
     */
    @Max(1)
    @Min(0)
    private Double strength;

    /**
     * 是否自动生成prompt,仅在task等于img2img.sd时有效, 万能相似图使用
     */
    @JsonProperty("auto_prompt")
    private Boolean autoPrompt;

    /**
     * 优化参数
     */
    @JsonProperty("tome_info")
    private NoLiPixImageTomeInfo tomeInfo;

    /**
     * 是否禁用 EasyNegative 效果
     * 默认值: false
     */
    @JsonProperty("disable_easy_negative")
    private Boolean disableEasyNegative;

}
