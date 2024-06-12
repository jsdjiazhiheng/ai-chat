package cn.com.chat.chat.chain.response.aliyun.image;

import cn.com.chat.chat.chain.response.base.image.ImageData;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

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
public class AliyunImageOutput implements Serializable {

    @JsonProperty("task_id")
    private String taskId;

    @JsonProperty("task_status")
    private String taskStatus;

    private String code;

    private String message;

    private List<ImageData> results;

    @JsonProperty("task_metrics")
    private AliyunImageTaskMetrics taskMetrics;

}
