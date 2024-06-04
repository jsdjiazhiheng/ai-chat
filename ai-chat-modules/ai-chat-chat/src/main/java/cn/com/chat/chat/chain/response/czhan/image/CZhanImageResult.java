package cn.com.chat.chat.chain.response.czhan.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 触站图像响应
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CZhanImageResult implements Serializable {

    private String state;

    private Float progress;

    private Integer audit;

    private String imgUrl;

    private List<CZhanImageData> images;

}
