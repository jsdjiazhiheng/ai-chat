package cn.com.chat.chat.chain.plugins.search;

import java.util.List;

/**
 * 搜索引擎结果
 *
 * @author JiaZH
 * @date 2024-04-30
 */
public interface WebSearchEngine {

    String search(String searchWord);

    List<String> searchList(String searchWord);

}
