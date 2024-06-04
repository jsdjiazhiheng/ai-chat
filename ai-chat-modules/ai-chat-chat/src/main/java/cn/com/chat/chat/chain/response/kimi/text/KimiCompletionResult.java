package cn.com.chat.chat.chain.response.kimi.text;

import cn.com.chat.chat.chain.response.base.Usage;
import cn.com.chat.chat.chain.response.base.text.TextCompletionResult;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Kimi文本对话响应参数
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class KimiCompletionResult extends TextCompletionResult implements Serializable {

    private String model;

    private List<KimiTextChoice> choices;

    private Usage usage;

}
