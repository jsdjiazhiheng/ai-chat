package cn.com.chat.chat.chain.response.aliyun.vision;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-07-17
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class AliyunVisionOutput implements Serializable {

    /**
     * 当result_format设置为text时返回该字段
     */
    private String text;

    /**
     * 当result_format设置为text时返回该字段
     */
    @JsonProperty("finish_reason")
    private String finishReason;

    /**
     * 当result_format设置为message时返回该字段
     */
    private List<AliyunVisionChoice> choices;

}
