package cn.com.chat.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * OpenAi配置
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-06-08
 */
@Data
@ConfigurationProperties(prefix = "ai.openai")
public class OpenAiConfig {

    private String token;

    private Boolean proxyEnable;

    private String proxyHost;

    private Integer proxyPort;

}
