package cn.com.chat.chat.chain.service;

import cn.com.chat.chat.config.OpenAiConfig;
import cn.com.chat.common.http.builder.OkHttpClientBuilder;
import cn.com.chat.common.http.utils.HttpUtils;
import lombok.AllArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-06-08
 */
@Service
@AllArgsConstructor
public class HttpService {

    private final OpenAiConfig config;

    private OkHttpClient proxyHttpClient() {
        OkHttpClientBuilder builder = OkHttpClientBuilder.builder()
            .connectTimeout(60)
            .readTimeout(60)
            .writeTimeout(60)
            .maxIdleConnections(200)
            .keepAliveDuration(300);
        if (config.getProxyEnable()) {
            builder.proxy(config.getProxyHost(), config.getProxyPort());
        }
        return builder.build();
    }

    public void setProxyHttpUtils() {
        HttpUtils.setCustomClient(proxyHttpClient());
    }

    public void clearProxyHttpUtils() {
        HttpUtils.clearCustomClient();
    }

}
