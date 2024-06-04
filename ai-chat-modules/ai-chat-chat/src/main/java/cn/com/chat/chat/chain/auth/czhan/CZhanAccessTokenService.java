package cn.com.chat.chat.chain.auth.czhan;

import lombok.AllArgsConstructor;
import cn.com.chat.chat.chain.auth.AccessTokenService;
import cn.com.chat.chat.config.CZhanConfig;
import org.springframework.stereotype.Component;

/**
 * 触站认证Service
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-25
 */
@Component
@AllArgsConstructor
public class CZhanAccessTokenService implements AccessTokenService {

    private final CZhanConfig config;

    @Override
    public String getAccessToken() {
        return config.getToken();
    }
}
