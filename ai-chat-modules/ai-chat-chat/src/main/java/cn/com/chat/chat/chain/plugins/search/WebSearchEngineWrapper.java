package cn.com.chat.chat.chain.plugins.search;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-05-06
 */
@Slf4j
@Service
@Primary
@AllArgsConstructor
public class WebSearchEngineWrapper implements WebSearchEngine {

    private final WebSearchEngineFactory webSearchEngineFactory;

    @Override
    public String search(String searchWord) {
        return webSearchEngineFactory.getEngine().search(searchWord);
    }

    @Override
    public List<String> searchList(String searchWord) {
        return webSearchEngineFactory.getEngine().searchList(searchWord);
    }


}
