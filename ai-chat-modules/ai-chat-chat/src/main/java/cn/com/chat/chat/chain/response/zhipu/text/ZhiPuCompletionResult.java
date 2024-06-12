package cn.com.chat.chat.chain.response.zhipu.text;

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
 * 智谱文本响应
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ZhiPuCompletionResult extends TextCompletionResult {

    private String model;

    private List<TextChoice> choices;

    private Usage usage;

    @JsonProperty("web_search")
    private ZhiPuWebSearchResult webSearch;

}
