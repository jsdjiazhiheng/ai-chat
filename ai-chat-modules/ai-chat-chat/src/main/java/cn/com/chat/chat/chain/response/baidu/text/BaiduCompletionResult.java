package cn.com.chat.chat.chain.response.baidu.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import cn.com.chat.chat.chain.response.base.text.TextCompletionResult;
import cn.com.chat.chat.chain.response.base.Usage;

/**
 * 百度文本对话响应内容
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-04
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class BaiduCompletionResult extends TextCompletionResult {

    /**
     * 表示当前子句的序号。只有在流式接口模式下会返回该字段
     */
    @JsonProperty("sentence_id")
    private Integer sentenceId;

    /**
     * 表示当前子句是否是最后一句。只有在流式接口模式下会返回该字段
     */
    @JsonProperty("is_end")
    private Boolean isEnd;

    /**
     * 当前生成的结果是否被截断
     */
    @JsonProperty("is_truncated")
    private Boolean isTruncated;

    /**
     * 输出内容标识
     * normal：输出内容完全由大模型生成，未触发截断、替换
     * stop：输出结果命中入参stop中指定的字段后被截断
     * length：达到了最大的token数，根据EB返回结果is_truncated来截断
     * content_filter：输出内容被截断、兜底、替换为**等
     * function_call：调用了funtion call功能
     */
    @JsonProperty("finish_reason")
    private String finishReason;

    /**
     * 对话返回结果
     */
    private String result;

    /**
     * 表示用户输入是否存在安全风险，是否关闭当前会话，清理历史会话信息
     */
    @JsonProperty("need_clear_history")
    private Boolean needClearHistory;

    /**
     * 是否正常返回 0正常
     */
    private Integer flag;

    /**
     * 当need_clear_history为true时，此字段会告知第几轮对话有敏感信息，如果是当前问题，ban_round=-1
     */
    @JsonProperty("ban_round")
    private Integer banRound;

    /**
     * token统计信息
     */
    private Usage usage;

    /**
     * 搜索数据，当请求参数enable_citation或enable_trace为true，并且触发搜索时，会返回该字段
     */
    @JsonProperty("search_info")
    private BaiduTextSearchInfo searchInfo;

}
