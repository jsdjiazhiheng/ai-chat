package cn.com.chat.common.http.utils;

import cn.com.chat.common.core.utils.SpringUtils;
import cn.com.chat.common.core.utils.StringUtils;
import cn.com.chat.common.http.callback.OkHttpCallback;
import cn.com.chat.common.json.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.Objects;

/**
 * Http工具类
 *
 * @author JiaZH
 * @date 2024-06-07
 */
@Slf4j
@Component
public class HttpUtils {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final String CONTENT_TYPE_JSON = "application/json";

    private static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";

    private static volatile OkHttpClient httpClient;


    static {
        httpClient = SpringUtils.getBean(OkHttpClient.class);
    }

    private HttpUtils() {

    }

    /**
     * get请求
     *
     * @param url 请求地址
     * @return 请求结果
     */
    public static String doGet(String url) {
        return doGet(url, null, null);
    }

    /**
     * get请求
     *
     * @param url 请求地址
     * @return 请求结果
     */
    public static byte[] doGetByte(String url) {
        return doGetByte(url, null, null);
    }

    /**
     * get请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 请求结果
     */
    public static String doGet(String url, Map<String, String> params) {
        return doGet(url, params, null);
    }

    /**
     * post请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 请求结果
     */
    public static String doPostForm(String url, Map<String, String> params) {
        return doPostForm(url, params, null);
    }

    /**
     * post请求
     *
     * @param url  请求地址
     * @param json 请求参数
     * @return 请求结果
     */
    public static String doPostJson(String url, Object json) {
        return doPostJson(url, json, null);
    }

    /**
     * 异步post请求
     *
     * @param url      请求地址
     * @param json     请求参数
     * @param callback 回调
     */
    public static void asyncPostJson(String url, Object json, OkHttpCallback callback) {
        asyncPostJson(url, json, null, callback);
    }

    /**
     * get请求
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @return 请求结果
     */
    public static String doGet(String url, Map<String, String> params, Map<String, String> headers) {
        StringBuilder urlBuilder = new StringBuilder(url);
        if (params != null && !params.isEmpty()) {
            urlBuilder.append("?");
            params.forEach((k, v) -> urlBuilder.append(k).append("=").append(v).append("&"));
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        Request.Builder builder = new Request.Builder();
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(builder::addHeader);
        }
        Request request = builder.url(urlBuilder.toString()).build();
        log.info("请求地址:{}", urlBuilder);
        return executeBody(request);
    }

    /**
     * get请求
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @return 请求结果
     */
    public static byte[] doGetByte(String url, Map<String, String> params, Map<String, String> headers) {
        printLog(url, params, headers);
        StringBuilder urlBuilder = new StringBuilder(url);
        if (params != null && !params.isEmpty()) {
            urlBuilder.append("?");
            params.forEach((k, v) -> urlBuilder.append(k).append("=").append(v).append("&"));
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        Request.Builder builder = new Request.Builder();
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(builder::addHeader);
        }
        Request request = builder.url(urlBuilder.toString()).build();
        return executeBytes(request);
    }

    /**
     * post请求
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @return 请求结果
     */
    public static String doPostForm(String url, Map<String, String> params, Map<String, String> headers) {
        printLog(url, params, headers);
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && !params.isEmpty()) {
            params.forEach(builder::add);
        }
        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(requestBuilder::addHeader);
        }
        requestBuilder.addHeader("Content-Type", CONTENT_TYPE_FORM);
        Request request = requestBuilder.url(url).post(builder.build()).build();
        return executeBody(request);
    }

    /**
     * post请求
     *
     * @param url     请求地址
     * @param json    请求参数
     * @param headers 请求头
     * @return 请求结果
     */
    public static String doPostJson(String url, Object json, Map<String, String> headers) {
        printLog(url, json, headers);
        if (Objects.isNull(json)) {
            json = "";
        }
        RequestBody body = RequestBody.create(StringUtils.defaultIfBlank(JsonUtils.toJsonString(json), ""), JSON);
        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(requestBuilder::addHeader);
        }
        requestBuilder.addHeader("Content-Type", CONTENT_TYPE_JSON);
        Request request = requestBuilder.url(url).post(body).build();
        return executeBody(request);
    }

    /**
     * 异步post请求
     *
     * @param url      请求地址
     * @param json     请求参数
     * @param headers  请求头
     * @param callback 回调
     */
    public static void asyncPostJson(String url, Object json, Map<String, String> headers, OkHttpCallback callback) {
        printLog(url, json, headers);
        if (Objects.isNull(json)) {
            json = "";
        }
        RequestBody body = RequestBody.create(StringUtils.defaultIfBlank(JsonUtils.toJsonString(json), ""), JSON);
        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(requestBuilder::addHeader);
        }
        requestBuilder.addHeader("Content-Type", CONTENT_TYPE_JSON);
        Request request = requestBuilder.url(url).post(body).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code :" + response);
                }
                ResponseBody responseBody = response.body();
                if (responseBody == null) {
                    callback.onFailure(new IOException("response body is null"));
                    return;
                }

                // 逐行读取消息
                Reader reader = responseBody.charStream();
                BufferedReader bufferedReader = new BufferedReader(reader);

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (!line.isEmpty()) { // SSE消息不包含空行
                        if (line.startsWith("data:")) {
                            line = line.substring(5).trim();
                            callback.onResponse(line);
                        }
                    }
                }
            }
        });
    }

    private static String executeBody(Request request) {
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.body() != null) {
                return response.body().string();
            }
        } catch (Exception e) {
            log.error("请求发生异常：", e);
        }
        return null;
    }

    private static byte[] executeBytes(Request request) {
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.body() != null) {
                return response.body().bytes();
            }
        } catch (Exception e) {
            log.error("请求发生异常：", e);
        }
        return null;
    }

    private static void printLog(String url, Object params, Map<String, String> headers) {
        StringBuilder builder = new StringBuilder();
        builder.append("-------------------------------------------->").append("\n");
        builder.append("请求地址：").append(url).append("\n");
        if (headers != null && !headers.isEmpty()) {
            builder.append("请求头：").append("\n").append(JsonUtils.toFormatJsonString(headers)).append("\n");
        }
        if (params != null) {
            builder.append("请求参数：").append("\n").append(JsonUtils.toFormatJsonString(params)).append("\n");
        }
        builder.append("<--------------------------------------------");
        log.info(builder.toString());
    }

    public static void setCustomClient(OkHttpClient customClient) {
        httpClient = customClient;
    }

    public static void clearCustomClient() {
        httpClient = SpringUtils.getBean(OkHttpClient.class);
    }


}
