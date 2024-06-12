package cn.com.chat.chat.chain.response.openai.image;

import cn.com.chat.chat.chain.response.base.image.ImageData;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * OpenAI Image Result
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-06-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiImageResult implements Serializable {

    private String created;

    private List<ImageData> data;

}
