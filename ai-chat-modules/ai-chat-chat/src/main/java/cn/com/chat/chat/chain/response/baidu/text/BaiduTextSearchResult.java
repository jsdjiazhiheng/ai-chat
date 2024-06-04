package cn.com.chat.chat.chain.response.baidu.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 百度搜索结果
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-04
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class BaiduTextSearchResult implements Serializable {

    private Integer index;

    private String url;

    private String title;

}
