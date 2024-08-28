package cn.com.chat.chat.domain.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-08-27
 */
@Data
public class MessageBo implements Serializable {

    private String title;

    private String type;

    private String contentType;

    private Long chatId;

    private String content;

    private String images;

}
