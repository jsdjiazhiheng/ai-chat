package cn.com.chat.chat.chain.request.zhipu.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
public class ZhiPuTextWebSearch implements Serializable {

    /**
     * 是否启用搜索，默认启用搜索
     */
    @Builder.Default
    private Boolean enable = true;

    /**
     * 强制搜索自定义关键内容，此时模型会根据自定义搜索关键内容返回的结果作为背景知识来回答用户发起的对话。
     */
    @JsonProperty("search_query")
    private String searchQuery;

    /**
     * 获取详细的网页搜索来源信息，包括来源网站的图标、标题、链接、来源名称以及引用的文本内容。默认为关闭。
     */
    @Builder.Default
    @JsonProperty("search_result")
    private Boolean searchResult = false;

}
