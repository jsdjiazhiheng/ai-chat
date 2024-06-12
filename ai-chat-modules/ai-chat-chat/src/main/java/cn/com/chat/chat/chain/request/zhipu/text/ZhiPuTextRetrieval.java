package cn.com.chat.chat.chain.request.zhipu.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

/**
 * TODO
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-15
 */
@Data
@Builder
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ZhiPuTextRetrieval implements Serializable {

    /**
     * 知识库ID
     */
    @JsonProperty("knowledge_id")
    private String knowledgeId;

    /**
     * 请求模型时的知识库模板
     */
    @JsonProperty("prompt_template")
    private String promptTemplate;

}
