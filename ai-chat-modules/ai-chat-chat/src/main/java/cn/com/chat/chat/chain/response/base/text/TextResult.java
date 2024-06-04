package cn.com.chat.chat.chain.response.base.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文本响应结果
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class TextResult implements Serializable {

    private String content;

    private String model;

    private String version;

    private Long totalTokens;

    private String finishReason;

    private String response;

}
