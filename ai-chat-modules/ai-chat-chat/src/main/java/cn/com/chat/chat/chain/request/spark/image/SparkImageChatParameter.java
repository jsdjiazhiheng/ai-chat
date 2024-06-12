package cn.com.chat.chat.chain.request.spark.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 讯飞星火 图片聊天参数
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
public class SparkImageChatParameter implements Serializable {

    private String domain;

    private Integer width;

    private Integer height;

}
