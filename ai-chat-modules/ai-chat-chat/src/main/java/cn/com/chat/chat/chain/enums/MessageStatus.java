package cn.com.chat.chat.chain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-06-07
 */
@Getter
@AllArgsConstructor
public enum MessageStatus {

    /**
     * 等待返回
     */
    WAIT(1L),

    /**
     * 成功
     */
    SUCCESS(2L),

    /**
     * 失败
     */
    FAIL(3L);

    private final Long status;

}
