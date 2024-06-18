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
     * 模型推理接入点
     */
    private String point;

}
