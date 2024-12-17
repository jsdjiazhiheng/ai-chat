package cn.com.chat.chat.chain.auth.baidu;

import cn.com.chat.chat.chain.apis.BaiduApis;
import cn.com.chat.chat.chain.auth.ImageTokenService;
import cn.com.chat.chat.chain.auth.TextTokenService;
import cn.com.chat.chat.chain.auth.baidu.entity.BaiduAccessTokenResponse;
import cn.com.chat.chat.chain.enums.ImageChatType;
import cn.com.chat.chat.chain.enums.ModelType;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.domain.vo.OpenKeyVo;
import cn.com.chat.chat.service.IOpenKeyService;
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
public class BaiduAccessTokenService implements TextTokenService, ImageTokenService {

    private final IOpenKeyService openKeyService;
    private final static String TEXT_TOKEN_KEY = "text_chat:baidu_access_token";
    private final static String IMAGE_TOKEN_KEY = "image_chat:baidu_access_token";

    @Override
    public String getTextToken() {
        OpenKeyVo openKeyVo = openKeyService.queryByKey(TextChatType.BAIDU.name(), ModelType.TEXT.getCode());
        return getAccessToken(TEXT_TOKEN_KEY, openKeyVo.getAppKey(), openKeyVo.getAppSecret());
    }

    @Override
    public String getImageToken() {
        OpenKeyVo openKeyVo = openKeyService.queryByKey(ImageChatType.BAIDU.name(), ModelType.IMAGE.getCode());
        return getAccessToken(IMAGE_TOKEN_KEY, openKeyVo.getAppKey(), openKeyVo.getAppSecret());
    }

    public String getUrl(String url, ModelType modelType) {
        String accessToken = "";
        if (modelType == ModelType.TEXT) {
            accessToken = getTextToken();
        } else if (modelType == ModelType.IMAGE) {
            accessToken = getImageToken();
        }
        if (StringUtils.isEmpty(accessToken)) {
            throw new NullPointerException("accessToken不能为空");
        }
        return url + "?access_token=" + accessToken;
    }

    /**
     * 刷新AccessToken
     */
    private String refreshAccessToken(String clientId, String clientSecret) {
        String url = BaiduApis.GET_ACCESS_TOKEN_URL +
            "?grant_type=client_credentials" +
            "&client_id=" + clientId +
            "&client_secret=" + clientSecret;

        String response = HttpUtils.doPostJson(url, null, null);

        BaiduAccessTokenResponse tokenResponse = JsonUtils.parseObject(response, BaiduAccessTokenResponse.class);

        return Objects.requireNonNull(tokenResponse).getAccessToken();
    }

    private String getAccessToken(String key, String clientId, String clientSecret) {
        String accessToken = RedisUtils.getCacheObject(key);
        if (StringUtils.isEmpty(accessToken)) {
            accessToken = refreshAccessToken(clientId, clientSecret);
            Duration duration = Duration.ofDays(30);
            RedisUtils.setCacheObject(key, accessToken, duration.minusHours(1));
        }
        return accessToken;
    }

}
