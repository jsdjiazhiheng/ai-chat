package cn.com.chat.chat.chain.response.aliyun.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * TODO
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-06-09
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class AliyunImageResult {

    @JsonProperty("request_id")
    private String requestId;

    private AliyunImageOutput output;

    private AliyunImageUsage usage;

}
