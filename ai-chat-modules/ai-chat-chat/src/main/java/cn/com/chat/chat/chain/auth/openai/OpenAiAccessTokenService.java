package cn.com.chat.chat.chain.auth.openai;

import cn.com.chat.chat.chain.auth.AccessTokenService;
import org.springframework.stereotype.Service;

/**
 * OpenAI认证Service
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-06-08
 */
@Service
public class OpenAiAccessTokenService implements AccessTokenService {

    @Override
    public String getAccessToken() {
        return "";
    }

}
