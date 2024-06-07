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

    WAIT(1L),

    SUCCESS(2L),

    FAIL(3L);

    private final Long status;

}
