package cn.com.chat.chat.chain.response.aliyun.text;

import cn.com.chat.chat.chain.response.base.Usage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-06-05
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class AliyunCompletionResult implements Serializable {

    /**
     * 请求id
     */
    @JsonProperty("request_id")
    private String requestId;

    /**
     * 输出信息
     */
    private AliyunTextOutput output;

    /**
     * 本次调用使用的token信息
     */
    private Usage usage;

}
