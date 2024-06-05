package cn.com.chat.chat.chain.request.base.text;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 流式接口传参
 *
 * @author JiaZH
 * @date 2024-05-23
 */
@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class StreamMessage {

    private Long chatId;

    private String messageId;

    private String content;

    private Long userId;

    private Boolean useNet;

}
