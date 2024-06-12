package cn.com.chat.chat.chain.auth.deepseek;

import cn.com.chat.chat.chain.auth.AccessTokenService;
import cn.com.chat.chat.config.DeepSeekConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * DeepSeek认证Service
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-25
 */
@Component
@RequiredArgsConstructor
public class DeepSeekAccessTokenService implements AccessTokenService {

    private final DeepSeekConfig config;

    @Override
    public String getAccessToken() {
        return config.getToken();
    }
}
