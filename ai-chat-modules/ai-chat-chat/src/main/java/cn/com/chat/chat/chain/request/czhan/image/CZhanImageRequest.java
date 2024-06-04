package cn.com.chat.chat.chain.request.czhan.image;

import cn.com.chat.chat.chain.request.base.image.ImageRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 触站AI图像请求参数
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
public class CZhanImageRequest extends ImageRequest {

    /**
     * 负面词条
     */
    private String negativePrompt;

    @NonNull
    @Builder.Default
    private Integer modelStyleId = 919;


    /**
     * 生成图片宽度 默认值: 512
     * >= 64
     * <= 2048
     */
    @Builder.Default
    private Integer width = 1024;

    /**
     * 生成图片高度  默认值: 512
     * >= 64
     * <= 2048
     */
    @Builder.Default
    private Integer height = 1024;

    /**
     * 绘图步数
     * >= 1
     * <= 50
     */
    private Integer steps;

    /**
     * 高清化倍率
     * 仅在文生图模式有效，取值范围为1~3之间
     */
    private Integer hrScale;

    /**
     * 高清处理步数 （不建议少于15，会严重影响画面生成效果）
     * <= 30
     */
    private Integer hrSteps;

    /**
     * ai算法放大倍率
     * <= 3
     */
    private Integer upscale;

    /**
     * 随机种子
     */
    private Integer seed;

    /**
     * 单次批量生成数量 默认值: 1
     * <= 6
     */
    @Builder.Default
    private Integer batchSize = 1;

    /**
     * 脸部修复开关
     * 开启需要单独扣减2积分
     */
    private Boolean faceFix;

    /**
     * 引导系数
     * 取值1~30
     */
    private Integer cfgScale;

    /**
     * 预测模拟积分消耗
     */
    private Boolean predictConsume;

}
