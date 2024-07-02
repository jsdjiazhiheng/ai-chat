package cn.com.chat.chat.chain.plugins.search;

import java.util.List;

/**
 * 搜索引擎结果
 *
 * @author JiaZH
 * @date 2024-04-30
 */
public interface WebSearchEngine {

    /**
     * 搜索
     * @param searchWord 搜索词
     * @return 结果
     */
    String search(String searchWord);

    /**
     * 搜索列表
     * @param searchWord 搜索词
     * @return 结果列表
     */
    List<String> searchList(String searchWord);

}
