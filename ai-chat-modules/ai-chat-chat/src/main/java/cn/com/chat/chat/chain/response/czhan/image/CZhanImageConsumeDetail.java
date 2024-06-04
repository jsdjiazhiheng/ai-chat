package cn.com.chat.chat.chain.response.czhan.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 参数消耗积分明细
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CZhanImageConsumeDetail implements Serializable {

    /**
     * 积分量
     */
    private Integer count;

    /**
     * 积分扣减计算说明
     */
    private String desc;

}
