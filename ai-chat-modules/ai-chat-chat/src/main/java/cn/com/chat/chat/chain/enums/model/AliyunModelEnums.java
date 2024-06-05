package cn.com.chat.chat.chain.enums.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 阿里云模型枚举
 *
 * @author JiaZH
 * @date 2024-06-05
 */
@Getter
@AllArgsConstructor
public enum AliyunModelEnums {

    //文本模型
    QWEN_TURBO("qwen-turbo"),
    QWEN_PLUS("qwen-plus"),
    QWEN_MAX("qwen-max"),
    QWEN_MAX_LONGCONTEXT("qwen-max-longcontext"),

    //视觉模型
    QWEN_VL_PLUS("qwen-vl-plus"),
    QWEN_VL_MAX("qwen-vl-max"),

    //音频模型
    QWEN_AUDIO_TURBO("qwen-audio-turbo"),

    //长文本对话模型
    QWEB_LONG("qwen-long"),

    //画图模型
    WANX_V1("wanx-v1"),
    WANX_STYLE_COSPLAY_V1("wanx-style-cosplay-v1"),
    WANX_REPAINT_V1("wanx-style-repaint-v1"),
    WANX_BACKGROUND_V1("wanx-background-generation-v2"),
    WANX_ANYTEXT_V1("wanx-anytext-v1"),
    WANX_SKETCH_TO_IMAGE_V1("wanx-sketch-to-image-lite"),

    ;

    private final String model;

}
