package cn.com.chat.chat.chain.request.aliyun.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class AliyunImageParameters implements Serializable {

    /**
     * 输出图像的风格
     * 默认值：auto
     * "3d cartoon"：3D卡通
     * "anime"：动画
     * "oil painting"：油画
     * "watercolor"：水彩
     * "sketch" ：素描
     * "chinese painting"：中国画
     * "flat illustration"：扁平插画
     */
    private String style;

    /**
     * 生成图像的分辨率
     * 默认值：1024*1024
     * 1024*1024、720*1280、1280*720
     */
    private String size;

    /**
     * 生成图像的数量
     * 默认值：1
     */
    @Min(value = 1, message = "图片数量不能小于1")
    @Max(value = 4, message = "图片数量不能大于4")
    private Integer n;

    /**
     * 图片生成时候的种子值
     */
    private Integer seed;

    /**
     * 期望输出结果与垫图（参考图）的相似度
     */
    @Min(value = 0, message = "相似度不能小于0")
    @Max(value = 1, message = "相似度不能大于1")
    @JsonProperty("ref_strength")
    private Float refStrength;

    /**
     * 垫图（参考图）生图使用的生成方式
     * 默认值：repaint
     * repaint：参考内容
     * refonly：参考风格
     */
    @JsonProperty("ref_mode")
    private String refMode;

}
