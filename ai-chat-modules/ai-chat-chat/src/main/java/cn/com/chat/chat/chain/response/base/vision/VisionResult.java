package cn.com.chat.chat.chain.response.base.vision;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-06-25
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class VisionResult implements Serializable {

    private String content;

    private String model;

    private String version;

    private Long totalTokens;

    private String finishReason;

    private String response;

}
