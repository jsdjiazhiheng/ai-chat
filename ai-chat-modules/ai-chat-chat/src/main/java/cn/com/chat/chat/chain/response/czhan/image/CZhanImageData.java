package cn.com.chat.chat.chain.response.czhan.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 触站AI图像数据
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CZhanImageData implements Serializable {

    private String imageUrl;

    private Integer audit;

}
