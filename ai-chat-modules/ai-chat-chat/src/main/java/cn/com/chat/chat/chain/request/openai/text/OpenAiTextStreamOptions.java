package cn.com.chat.chat.chain.request.openai.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
public class OpenAiTextStreamOptions implements Serializable {

    /**
     * 如果设置，则会在消息之前流式传输一个额外的块。此区块上的字段显示整个请求的令牌使用情况统计信息，并且该字段将始终为空数组
     */
    @JsonProperty("include_usage")
    private Boolean includeUsage;

}
