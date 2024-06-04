package cn.com.chat.chat.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-05-23
 */
@Data
public class MessageVO implements Serializable {

    private ChatMessageVo userMessage;

    private ChatMessageVo assistantMessage;

}
