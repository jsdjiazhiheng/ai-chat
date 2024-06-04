package cn.com.chat.chat.chain.plugins.search.engine;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.com.chat.chat.chain.plugins.search.WebSearchEngine;
import cn.com.chat.chat.chain.plugins.search.WebSearchUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 百度网页搜索
 *
 * @author JiaZH
 * @date 2024-04-30
 */
@Slf4j
@Component
@AllArgsConstructor
public class BaiduWebSearchEngine implements WebSearchEngine {

    final String URL = "https://www.baidu.com/s?wd={}";
    final String RESULT_ID = "content_left";
    final String RESULT_CLASS = "result";

    @Override
    public String search(String searchWord) {
        String resultText = "";
        try {
            String _url = StrUtil.format(URL, searchWord);
            resultText = WebSearchUtils.search(_url, RESULT_ID, RESULT_CLASS);
        } catch (Exception e) {
            log.error("百度网页搜索异常", e);
        }
        return resultText;
    }

    @Override
    public List<String> searchList(String searchWord) {
        List<String> list = new ArrayList<>();
        try {
            String _url = StrUtil.format(URL, searchWord);
            list = WebSearchUtils.searchList(_url, RESULT_ID, RESULT_CLASS);
        } catch (Exception e) {
            log.error("百度网页搜索异常", e);
        }
        return list;
    }

}
