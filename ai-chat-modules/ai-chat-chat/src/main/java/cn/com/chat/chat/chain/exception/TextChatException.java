package cn.com.chat.chat.chain.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 文本对话异常
 *
 * @author JiaZH
 * @date 2024-05-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class TextChatException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     */
    private String detailMessage;

    public TextChatException(String message) {
        this.code = 500;
        this.message = message;
    }

    public TextChatException(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
