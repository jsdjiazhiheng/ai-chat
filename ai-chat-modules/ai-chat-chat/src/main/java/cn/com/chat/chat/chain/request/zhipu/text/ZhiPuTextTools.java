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
public class ZhiPuTextTools implements Serializable {

    /**
     * 工具类型,目前支持function、retrieval、web_search
     */
    private String type;

    /**
     * 仅当工具类型为function时补充
     */
    @JsonProperty("function")
    private ZhiPuTextFunction function;

    /**
     * 仅当工具类型为retrieval时补充
     */
    @JsonProperty("retrieval")
    private ZhiPuTextRetrieval retrieval;

    /**
     * 仅当工具类型为web_search时补充，如果tools中存在类型retrieval，此时web_search不生效。
     */
    @JsonProperty("web_search")
    private ZhiPuTextWebSearch webSearch;

}
