package cn.com.chat.chat.chain.auth.nolipix;

import cn.com.chat.chat.chain.auth.AccessTokenService;
import cn.com.chat.chat.config.NoLiPixConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 画宇宙认证服务
 *
 * @author JiaZH
 * @date 2024-06-13
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NoLiPixAccessTokenService implements AccessTokenService {

    private final NoLiPixConfig config;

    @Override
    public String getAccessToken() {
        return config.getToken();
    }

}
