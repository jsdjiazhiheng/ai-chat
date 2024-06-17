package cn.com.chat.chat.chain.request.aliyun.text;

import cn.com.chat.chat.chain.request.base.text.TextTool;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 阿里云文本参数
 *
 * @author JiaZH
 * @date 2024-06-05
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class AliyunTextParameter implements Serializable {

    /**
     * 用于指定返回结果的格式，默认为text
     * 取值范围：text、message
     */
    @JsonProperty("result_format")
    private String resultFormat;

    /**
     * 生成时使用的随机数种子
     */
    private Integer seed;

    /**
     * 用于限制模型生成token的数量，表示生成token个数的上限
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    /**
     * 生成时，核采样方法的概率阈值
     */
    @JsonProperty("top_p")
    private Float topP;

    /**
     * 生成时，采样候选集的大小
     */
    @JsonProperty("top_k")
    private Float topK;

    /**
     * 用于控制模型生成时连续序列中的重复度
     */
    @JsonProperty("repetition_penalty")
    private Float repetitionPenalty;

    /**
     * 用户控制模型生成时整个序列中的重复度
     * 取值范围 [-2.0, 2.0]
     */
    @JsonProperty("presence_penalty")
    private Float presencePenalty;

    /**
     * 用于控制随机性和多样性的程度
     * 取值范围：[0, 2)，不建议取值为0，无意义
     */
    @JsonProperty("temperature")
    private Float temperature;

    /**
     * stop参数用于实现内容生成过程的精确控制
     */
    private String stop;

    /**
     * 是否启用互联网搜索
     */
    @JsonProperty("enable_search")
    private Boolean enableSearch;

    /**
     * 用于控制是否使用流式输出
     */
    private Boolean stream;

    /**
     * 控制在流式输出模式下是否开启增量输出
     * 默认False 会包含已输出的内容
     */
    @JsonProperty("incremental_output")
    private Boolean incrementalOutput;

    /**
     * 用于指定可供模型调用的工具列表
     */
    private List<TextTool> tools;

}
