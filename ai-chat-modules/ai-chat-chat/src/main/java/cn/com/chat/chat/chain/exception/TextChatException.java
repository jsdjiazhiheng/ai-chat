package cn.com.chat.chat.chain.exception;

import cn.com.chat.common.core.exception.base.BaseException;

/**
 * 文本对话异常
 *
 * @author JiaZH
 * @date 2024-05-27
 */
public class TextChatException extends BaseException {

    public TextChatException(String code, Object... args) {
        super("TextChat", code, args, null);
    }

}
