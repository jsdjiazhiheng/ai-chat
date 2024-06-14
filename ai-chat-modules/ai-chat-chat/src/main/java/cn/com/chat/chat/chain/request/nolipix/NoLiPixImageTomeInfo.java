package cn.com.chat.chat.chain.request.nolipix;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serializable;

/**
 * 画图优化参数
 *
 * @author JiaZH
 * @date 2024-06-14
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class NoLiPixImageTomeInfo implements Serializable {

    /**
     * 是否启用
     * 默认值: false
     */
    private Boolean enable;

    /**
     * 优化强度
     */
    @Min(0)
    @Max(1)
    private Integer ratio;

}
