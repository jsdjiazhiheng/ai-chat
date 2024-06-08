package cn.com.chat.chat.chain.response.openai.text;

import cn.com.chat.chat.chain.response.base.Usage;
import cn.com.chat.chat.chain.response.base.text.TextChoice;
import cn.com.chat.chat.chain.response.base.text.TextCompletionResult;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * TODO
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-06-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiCompletionResult extends TextCompletionResult {

    private String model;

    private List<TextChoice> choices;

    /**
     * 此指纹表示模型运行时使用的后端配置。
     */
    @JsonProperty("system_fingerprint")
    private String systemFingerprint;

    private Usage usage;

}
