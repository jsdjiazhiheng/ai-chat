package cn.com.chat.chat.chain.response.nolipix;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 画图结果
 *
 * @author JiaZH
 * @date 2024-06-14
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class NoLiPixImageResult implements Serializable {

    private String uid;

    /**
     * 图片地址
     */
    private String cdn;

    /**
     * 图片地址列表
     */
    @JsonProperty("img_urls")
    private List<String> imgUrls;

    /**
     * 图片是否通过了敏感检测
     */
    private Boolean safe;

    /**
     * 没有通过敏感检测的原因
     */
    private String reason;

    @JsonProperty("create_time")
    private Long createTime;

    @JsonProperty("start_time")
    private Long startTime;

    @JsonProperty("end_time")
    private Long endTime;

    /**
     * 任务总生成时长
     */
    private Long duration;

}
