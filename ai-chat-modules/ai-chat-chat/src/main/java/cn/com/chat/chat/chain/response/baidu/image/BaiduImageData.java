package cn.com.chat.chat.chain.response.baidu.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * TODO
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-21
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class BaiduImageData implements Serializable {

    private Integer index;

    private String object;

    @JsonProperty("b64_image")
    private String b64Image;

}
