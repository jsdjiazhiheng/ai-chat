package cn.com.chat.chat.chain.response.base.text;

import cn.com.chat.chat.chain.request.base.text.MessageItem;
import com.fasterxml.jackson.annotation.JsonAlias;
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
 * @date 2024-05-01
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class TextChoice implements Serializable {

    private Integer index;

    @JsonProperty("message")
    @JsonAlias({"message", "delta"})
    private MessageItem message;

    @JsonProperty("finish_reason")
    private String finishReason;

}
