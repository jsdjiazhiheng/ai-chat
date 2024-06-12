package cn.com.chat.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 讯飞星火配置
 *
 * @author JiaZH
 * @date 2024-06-11
 */
@Data
@ConfigurationProperties(prefix = "ai.spark")
public class SparkConfig {

    private String appid;

    private String apiKey;

    private String apiSecret;

}
