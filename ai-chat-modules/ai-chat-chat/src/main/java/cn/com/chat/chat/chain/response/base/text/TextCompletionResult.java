package cn.com.chat.chat.chain.response.base.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文本对话响应参数
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-01
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class TextCompletionResult implements Serializable {

    protected String id;

    protected String object;

    protected Long created;

}
