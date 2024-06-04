package cn.com.chat.chat.chain.enums.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Kimi模型列表
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-01
 */
@AllArgsConstructor
@Getter
public enum KimiModelEnums {

    MOONSHOT_V1_8K("moonshot-v1-8k"),
    MOONSHOT_V1_32K("moonshot-v1-32k"),
    MOONSHOT_V1_128K("moonshot-v1-128k"),
    ;

    private final String model;

}
