package cn.com.chat.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 火山引擎配置
 *
 * @author JiaZH
 * @date 2024-06-18
 */
@Data
@ConfigurationProperties(prefix = "ai.volcengine")
public class VolcengineConfig {

    private String token;

    /**
     * 画图key
     */
    private String imageAccessKeyId;

    /**
     * 画图密钥
     */
    private String imageSecretAccessKey;

}
