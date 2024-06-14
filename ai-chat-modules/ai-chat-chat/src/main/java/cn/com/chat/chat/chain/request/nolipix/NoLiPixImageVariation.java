package cn.com.chat.chat.chain.request.nolipix;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serializable;

/**
 * 「生成同款」参数
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
public class NoLiPixImageVariation implements Serializable {

    /**
     * 随机种子
     */
    private Integer seed;

    /**
     * 该种子对应的「强度」
     */
    @Min(0)
    @Max(1)
    private Double strength;

}
