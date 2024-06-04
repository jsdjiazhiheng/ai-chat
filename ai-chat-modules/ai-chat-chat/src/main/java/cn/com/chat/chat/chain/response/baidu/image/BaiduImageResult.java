package cn.com.chat.chat.chain.response.baidu.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.com.chat.chat.chain.response.base.Usage;

import java.io.Serializable;
import java.util.List;

/**
 * 百度图像生成结果
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-21
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class BaiduImageResult implements Serializable {

    private String id;

    private String object;

    private Long created;

    private List<BaiduImageData> data;

    private Usage usage;

}
