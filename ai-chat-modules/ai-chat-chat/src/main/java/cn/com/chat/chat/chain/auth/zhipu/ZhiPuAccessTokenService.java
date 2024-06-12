package cn.com.chat.chat.chain.auth.zhipu;

import cn.com.chat.chat.chain.auth.AccessTokenService;
import cn.com.chat.chat.config.ZhiPuConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 智谱清言认证Service
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-25
 */
@Component
@RequiredArgsConstructor
public class ZhiPuAccessTokenService implements AccessTokenService {

    private final ZhiPuConfig config;

    @Override
    public String getAccessToken() {
        return config.getToken();
    }
}
