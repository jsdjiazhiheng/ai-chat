package cn.com.chat.chat.chain.request.spark.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 讯飞星火 文本聊天参数
 *
 * @author JiaZH
 * @date 2024-06-11
 */
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class SparkTextChatParameter implements Serializable {

    /**
     * 指定访问的领域
     */
    private String domain;

    /**
     * 核采样阈值。用于决定结果随机性,取值越高随机性越强即相同的问题得到的不同答案的可能性越高
     * 非必传,取值为[0,1],默认为0.5
     */
    private Double temperature;

    /**
     * 模型回答的tokens的最大长度
     * 非必传,取值为[1,4096],默认为2048
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    /**
     * 从k个候选中随机选择⼀个（⾮等概率）
     * 非必传,取值为[1,6],默认为4
     */
    @JsonProperty("top_k")
    private Integer topK;

    /**
     * 内容审核的严格程度，strict表示严格审核策略；moderate表示中等审核策略；default表示默认的审核程度
     */
    private String auditing;

}
