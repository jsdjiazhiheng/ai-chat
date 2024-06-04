package cn.com.chat.common.web.builder;

import cn.com.chat.common.web.interceptor.LoggingInterceptor;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * RestTemplateBuilder
 *
 * @author JiaZH
 * @date 2024-05-06
 */
public class RestTemplateBuilder {

    private int connectTimeout = 100000;
    private int readTimeout = 100000;
    private boolean enableSslCheck = false;
    private String proxyIp;
    private Integer proxyPort;

    private MediaType contentType = MediaType.APPLICATION_JSON;

    public RestTemplateBuilder connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public RestTemplateBuilder readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public RestTemplateBuilder enableSslCheck(boolean enableSslCheck) {
        this.enableSslCheck = enableSslCheck;
        return this;
    }

    public RestTemplateBuilder proxy(String ip, int port) {
        this.proxyIp = ip;
        this.proxyPort = port;
        return this;
    }

    public RestTemplateBuilder contentType(MediaType contentType) {
        this.contentType = contentType;
        return this;
    }

    public static RestTemplateBuilder builder() {
        return new RestTemplateBuilder();
    }

    public RestTemplate build() {
        final RestTemplate restTemplate = new RestTemplate();

        //sslIgnore
        SimpleClientHttpRequestFactory requestFactory;
        if (!this.enableSslCheck) {
            requestFactory = getUnsafeClientRequestFactory();
        } else {
            requestFactory = new SimpleClientHttpRequestFactory();
        }

        //timeout
        requestFactory.setConnectTimeout(this.connectTimeout);
        requestFactory.setReadTimeout(this.readTimeout);

        if (Objects.nonNull(proxyIp) && Objects.nonNull(proxyPort)) {
            requestFactory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.proxyIp, this.proxyPort)));
        }

        restTemplate.setInterceptors(getInterceptors());

        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }

    private List<ClientHttpRequestInterceptor> getInterceptors() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.setContentType(this.contentType);
            return execution.execute(request, body);
        });
        interceptors.add(new LoggingInterceptor());
        return interceptors;
    }


    private SimpleClientHttpRequestFactory getUnsafeClientRequestFactory() {
        TrustManager[] byPassTrustManagers = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};
        final SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, byPassTrustManagers, new SecureRandom());
            sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }

        return new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(HttpURLConnection connection, @NotNull String httpMethod) throws IOException {
                super.prepareConnection(connection, httpMethod);
                if (connection instanceof HttpsURLConnection) {
                    ((HttpsURLConnection) connection).setSSLSocketFactory(sslContext.getSocketFactory());
                }
            }
        };
    }

}
