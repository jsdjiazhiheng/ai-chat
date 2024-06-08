package cn.com.chat.chat.chain.request.openai.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * TODO
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-06-08
 */
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class OpenAiTextResponseFormat implements Serializable {

    /**
     * 类型
     * text or json_object
     */
    @Builder.Default
    private String type = "text";

}
