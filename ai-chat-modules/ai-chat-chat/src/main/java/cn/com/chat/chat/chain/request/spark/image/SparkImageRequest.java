package cn.com.chat.chat.chain.request.spark.image;

import cn.com.chat.chat.chain.request.spark.SparkRequestHeader;
import cn.com.chat.chat.chain.request.spark.SparkRequestPayload;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 讯飞星火图像生成请求
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
public class SparkImageRequest implements Serializable {

    private SparkRequestHeader header;

    private SparkImageRequestParameter parameter;

    private SparkRequestPayload payload;

}
