package cn.com.chat.chat.chain.request.base.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

/**
 * 图片数据
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-06-09
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ImageData implements Serializable {

    private String url;

}
