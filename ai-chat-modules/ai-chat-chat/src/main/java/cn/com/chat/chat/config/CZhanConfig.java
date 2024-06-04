package cn.com.chat.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 触站认证秘钥
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-25
 */
@Data
@ConfigurationProperties(prefix = "ai.czhan")
public class CZhanConfig {

    private String token;

}
