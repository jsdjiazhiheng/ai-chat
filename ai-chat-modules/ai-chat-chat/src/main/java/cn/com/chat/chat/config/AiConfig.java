package cn.com.chat.chat.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * AI配置
 *
 * @author JiaZH
 * @date 2024-06-12
 */
@Data
@Configuration
public class AiConfig {

    @Value("${ai.completion.model}")
    String model;

}
