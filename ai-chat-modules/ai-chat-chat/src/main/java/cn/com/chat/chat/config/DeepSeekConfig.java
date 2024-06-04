package cn.com.chat.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * TODO
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-25
 */
@Data
@ConfigurationProperties(prefix = "ai.deepseek")
public class DeepSeekConfig {

    private String token;

}
