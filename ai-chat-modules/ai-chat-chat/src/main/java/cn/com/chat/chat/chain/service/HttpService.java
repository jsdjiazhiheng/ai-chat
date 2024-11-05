package cn.com.chat.chat.chain.service;

import cn.com.chat.chat.config.OpenAiConfig;
import cn.com.chat.common.http.builder.OkHttpClientBuilder;
import cn.com.chat.common.http.config.HttpConfig;
import cn.com.chat.common.http.utils.HttpUtils;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class HttpService {

    private final OpenAiConfig config;
    private final HttpConfig httpConfig;

    private OkHttpClient proxyHttpClient() {
        OkHttpClientBuilder builder = OkHttpClientBuilder.builder()
            .connectTimeout(httpConfig.getConnectTimeout())
            .readTimeout(httpConfig.getReadTimeout())
            .writeTimeout(httpConfig.getWriteTimeout())
            .maxIdleConnections(httpConfig.getMaxIdleConnections())
            .keepAliveDuration(httpConfig.getKeepAliveDuration())
            .maxRequests(httpConfig.getMaxRequests())
            .maxRequestsPerHost(httpConfig.getMaxRequestsPerHost());
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
