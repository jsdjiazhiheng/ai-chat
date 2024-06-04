package cn.com.chat.chat.chain.response.deepseek.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import cn.com.chat.chat.chain.response.base.text.TextChoice;
import cn.com.chat.chat.chain.response.base.text.TextCompletionResult;
import cn.com.chat.chat.chain.response.base.Usage;

import java.util.List;

/**
 * DeepSeek文本对话响应内容
 *
 * @author JiaZH
 * @date 2024-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class DeepSeekCompletionResult extends TextCompletionResult {

    private String model;

    private List<TextChoice> choices;

    private Usage usage;

}
