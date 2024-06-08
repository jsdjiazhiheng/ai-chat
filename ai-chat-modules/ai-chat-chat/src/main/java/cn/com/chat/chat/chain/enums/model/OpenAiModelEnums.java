package cn.com.chat.chat.chain.enums.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * OpenAi模型枚举
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-06-08
 */
@Getter
@AllArgsConstructor
public enum OpenAiModelEnums {

    //文本模型
    GPT_4O("gpt-4o"),
    GPT_4O_2024_05_13("gpt-4o-2024-05-13"),

    GPT_4_TURBO("gpt-4-turbo"),
    GPT_4_TURBO_2024_04_09("gpt-4-turbo-2024-04-09"),
    GPT_4_TURBO_PREVIEW("gpt-4-turbo-preview"),
    GPT_4_0125_PREVIEW("gpt-4-0125-preview"),
    GPT_4_1106_PREVIEW("gpt-4-1106-preview"),
    /**
     * &#064;Deprecated  2024-12-6 废弃
     */
    @Deprecated
    GPT_4_VISION_PREVIEW("gpt-4-vision-preview"),
    /**
     * &#064;Deprecated  2024-12-6 废弃
     */
    @Deprecated
    GPT_4_1106_VISION_PREVIEW("gpt-4-1106-vision-preview"),
    GPT_4("gpt-4"),
    GPT_4_0613("gpt-4-0613"),
    /**
     * &#064;Deprecated  2025-6-6 废弃
     */
    @Deprecated
    GPT_4_32K("gpt-4-32k"),
    /**
     * &#064;Deprecated  2025-6-6 废弃
     */
    @Deprecated
    GPT_4_32K_0613("gpt-4-32k-0613"),

    GPT_3_5_TURBO_0125("gpt-3.5-turbo-0125"),
    GPT_3_5_TURBO("gpt-3.5-turbo"),
    GPT_3_5_TURBO_1106("gpt-3.5-turbo-1106"),
    GPT_3_5_TURBO_INSTRUCT("gpt-3.5-turbo-instruct"),
    @Deprecated
    GPT_3_5_TURBO_16K("gpt-3.5-turbo-16k"),
    /**
     * &#064;Deprecated  2024-6-13 废弃
     */
    @Deprecated
    GPT_3_5_TURBO_0613("gpt-3.5-turbo-0613"),
    /**
     * &#064;Deprecated  2024-6-13 废弃
     */
    @Deprecated
    GPT_3_5_TURBO_16K_0613("gpt-3.5-turbo-16k-0613"),



    //画图模型
    DALL_E_2("dall-e-2"),
    DALL_E_3("dall-e-3"),

    //语音模型
    TTS_1("tts-1"),
    TTS_1_HD("tts-1-hd"),

    //向量数据库
    TEXT_EMBEDDING_3_LARGE("text-embedding-3-large"),
    TEXT_EMBEDDING_3_SMALL("text-embedding-3-small"),
    TEXT_EMBEDDING_ADA_002("text-embedding-ada-002"),
    ;

    private final String model;

}
