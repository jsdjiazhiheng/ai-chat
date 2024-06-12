package cn.com.chat.chat.chain.request.spark.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 讯飞星火图像生成请求参数
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
public class SparkImageRequestParameter implements Serializable {

    private SparkImageChatParameter chat;

}
