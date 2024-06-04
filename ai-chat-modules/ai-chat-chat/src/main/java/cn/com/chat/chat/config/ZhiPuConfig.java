package cn.com.chat.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 智谱认证秘钥
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-25
 */
@Data
@ConfigurationProperties(prefix = "ai.zhipu")
public class ZhiPuConfig {

    private String token;

}
