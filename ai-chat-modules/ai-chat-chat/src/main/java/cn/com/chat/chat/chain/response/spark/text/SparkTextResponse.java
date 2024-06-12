package cn.com.chat.chat.chain.response.spark.text;

import cn.com.chat.chat.chain.response.spark.SparkResponseHeader;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 讯飞文本响应
 *
 * @author JiaZH
 * @date 2024-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class SparkTextResponse implements Serializable {

    SparkResponseHeader header;

    SparkTextResponsePayload payload;

}
