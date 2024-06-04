package cn.com.chat.chat.chain.auth.deepseek;

import lombok.AllArgsConstructor;
import cn.com.chat.chat.chain.auth.AccessTokenService;
import cn.com.chat.chat.config.DeepSeekConfig;
import org.springframework.stereotype.Component;

/**
 * DeepSeek认证Service
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-25
 */
@Component
@AllArgsConstructor
public class DeepSeekAccessTokenService implements AccessTokenService {

    private final DeepSeekConfig config;

    @Override
    public String getAccessToken() {
        return config.getToken();
    }
}
