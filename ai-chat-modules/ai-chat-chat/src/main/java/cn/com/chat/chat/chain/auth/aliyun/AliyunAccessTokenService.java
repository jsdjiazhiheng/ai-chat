package cn.com.chat.chat.chain.auth.aliyun;

import cn.com.chat.chat.chain.auth.AccessTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 阿里云 AccessToken
 *
 * @author JiaZH
 * @date 2024-06-04
 */
@Component
@AllArgsConstructor
public class AliyunAccessTokenService implements AccessTokenService {
    @Override
    public String getAccessToken() {
        return "";
    }

}
