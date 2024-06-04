package cn.com.chat.chat.chain.request.baidu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 百度AccessToken请求参数
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-02
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class BaiduAccessTokenRequest implements Serializable {

    @Builder.Default
    @JsonProperty("grant_type")
    private String grantType = "client_credentials";

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("client_secret")
    private String clientSecret;

}
