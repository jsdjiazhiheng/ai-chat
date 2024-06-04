package cn.com.chat.chat.chain.response.baidu.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 百度搜索数据
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-04
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class BaiduTextSearchInfo implements Serializable {

    @JsonProperty("search_results")
    private List<BaiduTextSearchResult> searchResults;

}
