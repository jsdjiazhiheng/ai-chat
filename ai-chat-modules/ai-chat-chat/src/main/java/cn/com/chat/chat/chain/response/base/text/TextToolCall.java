package cn.com.chat.chat.chain.response.base.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * TODO
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-15
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class TextToolCall implements Serializable {

    private String id;

    private String type;

    private TextToolFunction function;

}
