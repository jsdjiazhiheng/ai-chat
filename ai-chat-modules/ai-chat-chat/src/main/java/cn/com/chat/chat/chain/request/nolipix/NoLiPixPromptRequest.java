package cn.com.chat.chat.chain.request.nolipix;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 提示词预处理请求参数
 *
 * @author JiaZH
 * @date 2024-06-13
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class NoLiPixPromptRequest implements Serializable {

    /**
     * 用户输入的文本
     */
    private String text;

    /**
     * 是否启用 prompt 推荐
     */
    @JsonProperty("enable_recommend")
    private Boolean enableRecommend;

}
