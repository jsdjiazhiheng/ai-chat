package cn.com.chat.chat.chain.auth.baidu;

import cn.com.chat.chat.chain.apis.BaiduApis;
import cn.com.chat.chat.chain.auth.AccessTokenService;
import cn.com.chat.chat.chain.auth.baidu.entity.BaiduAccessTokenResponse;
import cn.com.chat.chat.config.BaiduConfig;
import cn.com.chat.common.core.utils.StringUtils;
import cn.com.chat.common.http.utils.HttpUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import cn.com.chat.common.redis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

/**
 * 百度认证Service
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-25
 */
@Component
@RequiredArgsConstructor
public class BaiduAccessTokenService implements AccessTokenService {

    private final BaiduConfig config;

    @Override
    public String getAccessToken() {
        String accessToken = RedisUtils.getCacheObject("text_chat:baidu_access_token");
        if (StringUtils.isEmpty(accessToken)) {
            accessToken = refreshAccessToken();
            Duration duration = Duration.ofDays(30);
            RedisUtils.setCacheObject("text_chat:baidu_access_token", accessToken, duration.minusHours(1));
        }
        return accessToken;
    }

    public String getUrl(String url) {
        String accessToken = getAccessToken();
        if (StringUtils.isEmpty(accessToken)) {
            throw new NullPointerException("accessToken不能为空");
        }
        return url + "?access_token=" + accessToken;
    }

    /**
     * 刷新AccessToken
     */
    private String refreshAccessToken() {

        String url = BaiduApis.GET_ACCESS_TOKEN_URL +
            "?grant_type=client_credentials" +
            "&client_id=" + config.getClientId() +
            "&client_secret=" + config.getClientSecret();

        String response = HttpUtils.doPostJson(url, null, null);

        BaiduAccessTokenResponse tokenResponse = JsonUtils.parseObject(response, BaiduAccessTokenResponse.class);

        return Objects.requireNonNull(tokenResponse).getAccessToken();
    }

}
