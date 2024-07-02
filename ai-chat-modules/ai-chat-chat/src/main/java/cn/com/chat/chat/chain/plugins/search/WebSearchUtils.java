package cn.com.chat.chat.chain.plugins.search;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import cn.com.chat.common.http.utils.HttpUtils;
import cn.hutool.core.util.StrUtil;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.DefaultJavaScriptErrorListener;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * web搜索
 *
 * @author JiaZH
 * @date 2024-05-07
 */
@Slf4j
public class WebSearchUtils {

    public static String search(String url, String resultId, String resultClass) {
        String resultText = "";

        HtmlPage page = getPage(url);

        if (Objects.isNull(page)) {
            return resultText;
        }
        String html = page.asXml();

        Document document = Jsoup.parse(html);
        Element elementById = document.getElementById(resultId);
        if (Objects.isNull(elementById)) {
            return resultText;
        }

        Elements elements = elementById.getElementsByClass(resultClass);

        for (Element element : elements) {
            Elements byTag = element.getElementsByTag("a");
            for (Element elementByTag : byTag) {
                String href = elementByTag.attr("href");
                if (StrUtil.isNotBlank(href)) {
                    log.info("加载网页内容：{}", href);
                    resultText = loadHtmlText(href);
                    if (StrUtil.isNotBlank(resultText)) {
                        break;
                    }
                }
            }
        }
        return resultText;
    }

    public static List<String> searchList(String url, String resultId, String resultClass) {
        List<String> list = new ArrayList<>();

        String html = HttpUtils.doGet(url);

        if (StrUtil.isBlank(html)) {
            return list;
        }

        Document document = Jsoup.parse(html);
        Element elementById = document.getElementById(resultId);
        if (Objects.isNull(elementById)) {
            return list;
        }
        Elements elements = elementById.getElementsByClass(resultClass);

        for (Element element : elements) {
            Elements byTag = element.getElementsByTag("a");
            if (!byTag.isEmpty()) {
                String href = byTag.get(0).attr("href");
                log.info("加载网页内容：{}", href);

                String resultText = loadHtmlText(href);

                if (StrUtil.isNotBlank(resultText)) {
                    list.add(resultText);
                }

                if (list.size() >= 10) {
                    break;
                }
            }
        }
        return list;
    }

    public static String loadHtmlText(String url) {
        String resultText = "";

        HtmlPage page = getPage(url);

        if (Objects.isNull(page)) {
            return resultText;
        }
        String body = page.getBody().asNormalizedText();
        if (StrUtil.isNotBlank(body)) {
            resultText = body;
        }
        return resultText;
    }

    private static HtmlPage getPage(String url) {
        HtmlPage page = null;
        try (WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
            setOptions(webClient);
            page = webClient.getPage(url);
        } catch (Exception e) {
            log.error("加载网页文本出错：", e);
        }
        return page;
    }

    /* 去除htmlunit日志 */
    static {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<Logger> loggerList = loggerContext.getLoggerList();
        loggerList.forEach(logger -> logger.setLevel(Level.INFO));
    }


    private static void setOptions(WebClient webClient) {
        webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX
        webClient.getOptions().setTimeout(1000);
        webClient.setJavaScriptErrorListener(new CustomJavaScriptErrorListener());
    }

    private static class CustomJavaScriptErrorListener extends DefaultJavaScriptErrorListener {

        @Override
        public void scriptException(HtmlPage page, ScriptException scriptException) {
        }

        @Override
        public void timeoutError(HtmlPage page, long allowedTime, long executionTime) {
        }

        @Override
        public void malformedScriptURL(HtmlPage page, String url, MalformedURLException exception) {
        }

        @Override
        public void loadScriptError(HtmlPage page, URL scriptUrl, Exception exception) {
        }

        @Override
        public void warn(String message, String sourceName, int line, String lineSource, int lineOffset) {
        }
    }

}
