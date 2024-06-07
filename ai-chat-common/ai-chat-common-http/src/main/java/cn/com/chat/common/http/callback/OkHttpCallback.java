package cn.com.chat.common.http.callback;

import java.io.IOException;

/**
 * 返回
 *
 * @author JiaZH
 * @date 2024-06-07
 */
public interface OkHttpCallback {

    void onFailure(IOException e);

    void onResponse(String response);

}
