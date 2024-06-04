package cn.com.chat.common.web.interceptor;

import cn.com.chat.common.web.wrapper.BufferingClientHttpResponseWrapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 自定义请求拦截器
 *
 * @author JiaZH
 * @date 2024-05-06
 */
@Slf4j
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    @NonNull
    @Override
    public ClientHttpResponse intercept(@NonNull HttpRequest request, byte @NonNull [] body, ClientHttpRequestExecution execution) throws IOException {
        //displayRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        ClientHttpResponse responseWrapper = new BufferingClientHttpResponseWrapper(response);
        //displayResponse(responseWrapper);
        return responseWrapper;
    }

    /**
     * 显示请求相关信息
     *
     * @param request
     * @param body
     */
    private void displayRequest(HttpRequest request, byte[] body) {
        log.info("====RestTemplate Request info====");
        log.info("URI         : {}", request.getURI());
        log.info("Method      : {}", request.getMethod());
        log.info("Req Headers : {}", this.headersToString(request.getHeaders()));
        log.info("Request body: {}", body == null ? "" : new String(body, StandardCharsets.UTF_8));
    }

    /**
     * 显示响应相关信息
     *
     * @param response
     * @throws IOException
     */
    private void displayResponse(ClientHttpResponse response) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                inputStringBuilder.append(line);
                inputStringBuilder.append('\n');
                line = bufferedReader.readLine();
            }
        }
        log.info("====RestTemplate Response info====");
        log.info("Status code  : {}", response.getStatusCode());
        log.info("Status text  : {}", response.getStatusText());
        log.info("Resp Headers : {}", headersToString(response.getHeaders()));
        log.info("Response body: {}", inputStringBuilder);
    }

    /**
     * 将Http头信息格式化处理
     *
     * @param httpHeaders
     * @return
     */
    private String headersToString(HttpHeaders httpHeaders) {
        if (Objects.isNull(httpHeaders)) {
            return "[]";
        }
        return httpHeaders.entrySet().stream()
            .map(entry -> {
                List<String> values = entry.getValue();
                return "\t" + entry.getKey() + ":" + (values.size() == 1 ?
                    "\"" + values.get(0) + "\"" :
                    values.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(", ")));
            })
            .collect(Collectors.joining(", \n", "\n[\n", "\n]\n"));
    }

}
