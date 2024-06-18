package cn.com.chat.chat.chain.auth.volcengine;

import cn.com.chat.chat.chain.auth.AccessTokenService;
import cn.com.chat.chat.config.VolcengineConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 火山引擎认证服务
 *
 * @author JiaZH
 * @date 2024-06-18
 */
@Component
@RequiredArgsConstructor
public class VolcengineAccessTokenService implements AccessTokenService {

    private final VolcengineConfig config;

    @Override
    public String getAccessToken() {
        return config.getToken();
    }

    public String getPoint() {
        return config.getPoint();
    }

}
