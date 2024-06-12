package cn.com.chat.chat.chain.auth.kimi;

import cn.com.chat.chat.chain.auth.AccessTokenService;
import cn.com.chat.chat.config.KimiConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Kimi认证Service
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-25
 */
@Component
@RequiredArgsConstructor
public class KimiAccessTokenService implements AccessTokenService {

    private final KimiConfig config;

    @Override
    public String getAccessToken() {
        return config.getToken();
    }
}
