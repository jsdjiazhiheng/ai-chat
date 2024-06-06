package cn.com.chat.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 阿里云认证配置
 *
 * @author JiaZH
 * @date 2024-06-06
 */
@Data
@ConfigurationProperties(prefix = "ai.aliyun")
public class AliyunConfig {

    private String token;

}
