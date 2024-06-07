package cn.com.chat.common.http.config;

import cn.com.chat.common.core.factory.YmlPropertySourceFactory;
import cn.com.chat.common.http.builder.OkHttpClientBuilder;
import lombok.Data;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

/**
 * http配置
 *
 * @author JiaZH
 * @date 2024-06-07
 */
@Data
@AutoConfiguration
@PropertySource(value = "classpath:common-http.yml", factory = YmlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "http")
public class HttpConfig {

    private int connectTimeout;

    private int readTimeout;

    private int writeTimeout;

    private int maxIdleConnections;

    private int keepAliveDuration;

    @Bean
    public OkHttpClient okHttpClient() {
        return OkHttpClientBuilder.builder()
            .connectTimeout(connectTimeout)
            .readTimeout(readTimeout)
            .writeTimeout(writeTimeout)
            .maxIdleConnections(maxIdleConnections)
            .keepAliveDuration(keepAliveDuration)
            .build();
    }

}
