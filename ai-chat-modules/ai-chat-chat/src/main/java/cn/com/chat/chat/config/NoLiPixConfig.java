package cn.com.chat.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 画宇宙配置
 *
 * @author JiaZH
 * @date 2024-06-13
 */
@Data
@ConfigurationProperties(prefix = "ai.nolipix")
public class NoLiPixConfig {

    String token;

}
