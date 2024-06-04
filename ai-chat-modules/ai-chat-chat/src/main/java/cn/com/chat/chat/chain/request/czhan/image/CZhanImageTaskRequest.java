package cn.com.chat.chat.chain.request.czhan.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CZhanImageTaskRequest implements Serializable {

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 是否返回预览图
     */
    @Builder.Default
    private Boolean preview = false;

    /**
     * 结果图片格式
     * 枚举值:
     * png
     * jpeg
     */
    @Builder.Default
    private String imgFormat = "jpeg";

    /**
     * 结果图片质量
     */
    @Min(1)
    @Max(100)
    @Builder.Default
    private Integer imgQuality = 100;

    /**
     * 等待直到任务完成再返回
     */
    @Builder.Default
    private Boolean waitUtilEnd = true;

}
