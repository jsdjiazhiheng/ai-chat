package cn.com.chat.common.web.wrapper;

import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-05-06
 */
public class BufferingClientHttpResponseWrapper implements ClientHttpResponse {

    private final ClientHttpResponse response;
    private byte[] body;

    public BufferingClientHttpResponseWrapper(ClientHttpResponse response) {
        this.response = response;
    }

    @NonNull
    @Override
    public HttpStatusCode getStatusCode() throws IOException {
        return this.response.getStatusCode();
    }

    @NonNull
    @Override
    public String getStatusText() throws IOException {
        return this.response.getStatusText();
    }

    @Override
    public void close() {
        this.response.close();
    }

    @NonNull
    @Override
    public InputStream getBody() throws IOException {
        if (this.body == null) {
            this.body = StreamUtils.copyToByteArray(this.response.getBody());
        }
        return new ByteArrayInputStream(this.body);
    }

    @NonNull
    @Override
    public HttpHeaders getHeaders() {
        return this.response.getHeaders();
    }

}
