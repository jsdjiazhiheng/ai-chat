package cn.com.chat.chat.agent.core.function;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AI能力类型枚举
 *
 * @author JiaZH
 * @date 2024-05-11
 */
@AllArgsConstructor
@Getter
public enum AiFunctionType {

    TEXT_GEN("文本生成"),
    IMAGE_GEN("图片生成"),
    VIDEO_GEN("视频生成"),
    ;

    private final String type;

}
