package cn.com.chat.chat.chain.auth.aliyun;

import cn.com.chat.chat.chain.auth.AccessTokenService;
import cn.com.chat.chat.config.AliyunConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 阿里云 AccessToken
 *
 * @author JiaZH
 * @date 2024-06-04
 */
@Component
@RequiredArgsConstructor
public class AliyunAccessTokenService implements AccessTokenService {

    @Autowired
    AliyunConfig aliyunConfig;

    @Override
    public String getAccessToken() {
        return aliyunConfig.getToken();
    }

}
