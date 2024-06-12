package cn.com.chat.chat.chain.plugins.search.engine;

import cn.com.chat.chat.chain.plugins.search.WebSearchEngine;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 今日头条搜索
 *
 * @author JiaZH
 * @date 2024-05-09
 */
@Slf4j
@Component
@AllArgsConstructor
public class ToutiaoWebSearchEngine implements WebSearchEngine {

    final String URL = "https://so.toutiao.com/search?dvpf=pc&source=input&keyword={}";
    final String RESULT_ID = "main";
    final String RESULT_CLASS = "s-result-list";

    @Override
    public String search(String searchWord) {
        String resultText = "";
        try {
            //resultText = search(searchWord, URL, RESULT_ID, RESULT_CLASS);
        } catch (Exception e) {
            log.error("今日头条网页搜索异常", e);
        }
        return resultText;
    }

    @Override
    public List<String> searchList(String searchWord) {
        List<String> list = new ArrayList<>();
        try {
            //list = searchList(searchWord, URL, RESULT_ID, RESULT_CLASS);
        } catch (Exception e) {
            log.error("今日头条网页搜索异常", e);
        }
        return list;
    }

}
