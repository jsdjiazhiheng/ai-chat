package cn.com.chat.chat.chain.enums.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 火山引擎模型枚举
 *
 * @author JiaZH
 * @date 2024-06-18
 */
@Getter
@AllArgsConstructor
public enum VolcengineModelEnums {

    /**
     * 豆包模型
     */
    DOUBAO_LITE_4K("Doubao-lite-4k"),
    DOUBAO_LITE_32K("Doubao-lite-32k"),
    DOUBAO_LITE_128K("Doubao-lite-128k"),

    DOUBAO_PRO_4K("Doubao-pro-4k"),
    DOUBAO_PRO_32K("Doubao-pro-32k"),
    DOUBAO_PRO_128K("Doubao-pro-128k"),
    ;

    private final String model;

}
