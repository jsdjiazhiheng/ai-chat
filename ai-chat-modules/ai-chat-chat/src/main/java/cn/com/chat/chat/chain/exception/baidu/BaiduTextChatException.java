package cn.com.chat.chat.chain.exception.baidu;

import cn.com.chat.chat.chain.exception.TextChatException;

/**
 * 百度文本对话异常
 *
 * @author JiaZH
 * @date 2024-05-27
 */
public class BaiduTextChatException extends TextChatException {

    public BaiduTextChatException(String message) {
        super(message);
    }

    public BaiduTextChatException(int code, String message) {
        super(code, message);
    }

}
