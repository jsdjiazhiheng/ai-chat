package cn.com.chat.chat.chain.auth.spark;

import cn.com.chat.chat.chain.auth.AccessTokenService;
import cn.com.chat.chat.config.SparkConfig;
import cn.com.chat.common.core.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 讯飞星火认证服务
 *
 * @author JiaZH
 * @date 2024-06-11
 */
@Slf4j
@Component
@AllArgsConstructor
public class SparkAccessTokenService implements AccessTokenService {

    private final SparkConfig config;

    @Override
    public String getAccessToken() {
        return "";
    }

    public String getAppid() {
        return config.getAppid();
    }

    public String getAuthUrl(String hostUrl) {
        try {
            URL url = new URL(hostUrl);
            // 时间
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            String date = format.format(new Date());
            // 拼接
            String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";
            // System.err.println(preStr);
            // SHA256加密
            Mac mac = Mac.getInstance("hmacsha256");
            SecretKeySpec spec = new SecretKeySpec(config.getApiSecret().getBytes(StandardCharsets.UTF_8), "hmacsha256");
            mac.init(spec);

            byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
            // Base64加密
            String sha = Base64.getEncoder().encodeToString(hexDigits);
            // 拼接
            String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", config.getApiKey(), "hmac-sha256", "host date request-line", sha);
            // 拼接地址
            HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).
                addQueryParameter("date", date).
                addQueryParameter("host", url.getHost()).
                build();

            return httpToWsUrl(httpUrl.toString());
        } catch (Exception e) {
            log.error("SparkAccessTokenService -> 获取授权Url失败", e);
        }
        return null;
    }

    private String httpToWsUrl(String url) {
        return StringUtils.replace(StringUtils.replace(url, "https://", "wss://"), "http://", "ws://");
    }

}
