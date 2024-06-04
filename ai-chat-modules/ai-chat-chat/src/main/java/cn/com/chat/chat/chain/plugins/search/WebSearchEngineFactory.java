package cn.com.chat.chat.chain.plugins.search;

import cn.com.chat.chat.chain.plugins.search.engine.BaiduWebSearchEngine;
import cn.com.chat.chat.chain.plugins.search.engine.BingWebSearchEngine;
import cn.com.chat.chat.chain.plugins.search.engine.ToutiaoWebSearchEngine;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.com.chat.chat.service.ISearchEngineService;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-05-06
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class WebSearchEngineFactory {

    private final BaiduWebSearchEngine baiduWebSearchEngine;
    private final BingWebSearchEngine bingWebSearchEngine;
    private final ToutiaoWebSearchEngine toutiaoWebSearchEngine;
    private final ISearchEngineService searchEngineService;

    public WebSearchEngine getEngine() {
        String engine = searchEngineService.getDefaultSearchEngine();
        log.info("使用搜索引擎：{}", engine);
        if (StrUtil.isNotBlank(engine)) {
            if ("baidu".equalsIgnoreCase(engine)) {
                return baiduWebSearchEngine;
            } else if ("bing".equalsIgnoreCase(engine)) {
                return bingWebSearchEngine;
            } else if ("toutiao".equalsIgnoreCase(engine)) {
                return toutiaoWebSearchEngine;
            }
        }
        return null;
    }

    /*@PostConstruct
    public void init() {
        WebSearchUtils.init();
    }*/

}
