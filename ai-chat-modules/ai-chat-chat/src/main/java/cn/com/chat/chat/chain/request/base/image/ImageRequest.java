package cn.com.chat.chat.chain.request.base.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 图像对话请求参数
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-21
 */
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ImageRequest implements Serializable {

    /**
     * 描述词条
     */
    @NotNull
    private String prompt;

}
