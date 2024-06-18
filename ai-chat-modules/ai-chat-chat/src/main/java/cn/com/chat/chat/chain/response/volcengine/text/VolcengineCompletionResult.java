package cn.com.chat.chat.chain.response.volcengine.text;

import cn.com.chat.chat.chain.response.base.Usage;
import cn.com.chat.chat.chain.response.base.text.TextChoice;
import cn.com.chat.chat.chain.response.base.text.TextCompletionResult;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-06-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class VolcengineCompletionResult extends TextCompletionResult implements Serializable {

    private String model;

    private List<TextChoice> choices;

    private Usage usage;

}
