package cn.com.chat.chat.chain.enums.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-06-09
 */
@Getter
@AllArgsConstructor
public enum DeepSeekModelEnums {

    /**
     * chat模型
     */
    DEEP_SEEK_CHAT("deepseek-chat"),
    /**
     * coder模型
     */
    DEEP_SEEK_CODER("deepseek-coder"),
    ;

    private final String model;

}
