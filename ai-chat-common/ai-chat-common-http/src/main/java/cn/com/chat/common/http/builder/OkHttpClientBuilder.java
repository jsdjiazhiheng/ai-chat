package cn.com.chat.common.http.builder;

import lombok.Data;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import static java.net.Proxy.Type.HTTP;

/**
 * okhttp client 构建
 *
 * @author JiaZH
 * @date 2024-06-07
 */
@Data
public class OkHttpClientBuilder {

    private int connectTimeout = 60;

    private int readTimeout = 60;

    private int writeTimeout = 60;

    private int maxIdleConnections = 200;

    private int keepAliveDuration = 300;

    private String proxyHost;

    private int proxyPort;

    public static OkHttpClientBuilder builder() {
        return new OkHttpClientBuilder();
    }

    public OkHttpClient build() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.sslSocketFactory(sslSocketFactory(), x509TrustManager())
            // 是否开启缓存
            .retryOnConnectionFailure(false)
            .connectionPool(pool())
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)
            .hostnameVerifier((hostname, session) -> true)
            .build();
        if (proxyHost != null && proxyPort != 0) {
            builder.proxy(new Proxy(HTTP, new InetSocketAddress(proxyHost, proxyPort)));
        }
        return builder.build();
    }

    public OkHttpClientBuilder proxy(String proxyHost, int proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        return this;
    }

    public OkHttpClientBuilder connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public OkHttpClientBuilder readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public OkHttpClientBuilder writeTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    public OkHttpClientBuilder maxIdleConnections(int maxIdleConnections) {
        this.maxIdleConnections = maxIdleConnections;
        return this;
    }

    public OkHttpClientBuilder keepAliveDuration(int keepAliveDuration) {
        this.keepAliveDuration = keepAliveDuration;
        return this;
    }

    public X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    public SSLSocketFactory sslSocketFactory() {
        try {
            // 信任任何链接
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ConnectionPool pool() {
        return new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.SECONDS);
    }


}
