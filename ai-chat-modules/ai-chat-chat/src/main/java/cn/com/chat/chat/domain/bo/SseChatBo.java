package cn.com.chat.chat.domain.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-06-04
 */
@Data
public class SseChatBo implements Serializable {

    private String type;

    private Long chatId;

    private String content;

}
