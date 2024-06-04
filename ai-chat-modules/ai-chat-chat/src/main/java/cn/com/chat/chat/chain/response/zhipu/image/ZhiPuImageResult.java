package cn.com.chat.chat.chain.response.zhipu.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 智谱图像生成结果
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ZhiPuImageResult implements Serializable {

    private String created;

    private List<ZhiPuImageData> data;

}
