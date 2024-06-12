package cn.com.chat.chat.chain.request.spark.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

/**
 * 讯飞星火 图片聊天参数
 *
 * @author JiaZH
 * @date 2024-06-11
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class SparkImageChatParameter implements Serializable {

    private String domain;

    @Builder.Default
    private Integer width = 512;

    @Builder.Default
    private Integer height = 512;

}
