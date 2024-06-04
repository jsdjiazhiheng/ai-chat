package cn.com.chat.chat.chain.response.czhan.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 触站画图任务
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CZhanImageTask implements Serializable {

    /**
     * 绘画任务ID
     */
    private String paintingSign;

    /**
     * 任务限制数
     */
    private Integer taskLimitCount;

    /**
     * 本次扣减积分
     */
    private Integer used;

    /**
     * 账户当前余额
     */
    private String balance;

    /**
     * 预计消耗积分量
     */
    private Integer estimateUsed;

    /**
     * 参数需要消耗积分明细
     */
    private List<CZhanImageConsumeDetail> consumeDetail;

}
