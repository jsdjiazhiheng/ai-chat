package cn.com.chat.chat.chain.response.aliyun.vision;

import cn.com.chat.chat.chain.request.aliyun.vision.AliyunMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-07-17
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class AliyunVisionChoice implements Serializable {

    private Integer index;

    private AliyunMessage message;

    @JsonProperty("finish_reason")
    private String finishReason;

}
