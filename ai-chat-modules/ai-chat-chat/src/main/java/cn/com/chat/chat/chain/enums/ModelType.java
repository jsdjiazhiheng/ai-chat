package cn.com.chat.chat.chain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-12-17
 */
@AllArgsConstructor
@Getter
public enum ModelType {

    /**
     * 文本
     */
    TEXT(1),
    /**
     * 图像
     */
    IMAGE(2),
    /**
     * 视觉
     */
    VISION(3),
    /**
     * 语音
     */
    SPEECH(4);

    private final int code;
}
