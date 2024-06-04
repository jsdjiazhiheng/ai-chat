package cn.com.chat.chat.chain.exception;

import cn.com.chat.common.core.exception.base.BaseException;

/**
 * 图片对话异常
 *
 * @author JiaZH
 * @date 2024-05-27
 */
public class ImageChatException extends BaseException {

    public ImageChatException(String code, Object... args) {
        super("ImageChat", code, args, null);
    }

}
