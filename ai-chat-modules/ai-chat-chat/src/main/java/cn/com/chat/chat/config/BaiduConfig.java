package cn.com.chat.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 百度认证秘钥
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-03
 */
@Data
@ConfigurationProperties(prefix = "ai.baidu")
public class BaiduConfig {

    private String clientId;

    private String clientSecret;

}
