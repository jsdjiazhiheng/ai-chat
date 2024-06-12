package cn.com.chat.chat.chain.response.spark.text;

import cn.com.chat.chat.chain.response.spark.SparkResponsePayload;
import cn.com.chat.chat.chain.response.spark.SparkResponsePayloadUsage;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 讯飞文本响应主体
 *
 * @author JiaZH
 * @date 2024-06-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class SparkTextResponsePayload extends SparkResponsePayload {

    private SparkResponsePayloadUsage usage;

}
