package cn.com.chat.chat.chain.utils;

import cn.com.chat.common.web.builder.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * HttpUtils
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-25
 */
public class HttpUtils {

    private static final RestTemplate REST_TEMPLATE = RestTemplateBuilder.builder().build();
    private static final WebClient WEB_CLIENT = WebClient.builder()
        .defaultHeader("content-type", "application/json")
        .build();

    public static RestTemplate getRestTemplate() {
        return REST_TEMPLATE;
    }

    public static WebClient getWebClient() {
        return WEB_CLIENT;
    }

}
