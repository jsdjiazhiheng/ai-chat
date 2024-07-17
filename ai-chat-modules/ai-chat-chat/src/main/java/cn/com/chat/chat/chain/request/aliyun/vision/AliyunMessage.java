package cn.com.chat.chat.chain.request.aliyun.vision;

import cn.com.chat.chat.chain.enums.Role;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.common.core.utils.StringUtils;
import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-07-16
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class AliyunMessage implements Serializable {

    private String role;
    private List<AliyunContent> content;

    public static AliyunMessage buildAssistant(String content) {
        return AliyunMessage.builder()
            .role(Role.ASSISTANT.getName())
            .content(List.of(AliyunContent.builder().text(content).build()))
            .build();
    }

    public static AliyunMessage buildUser(String content) {
        return AliyunMessage.builder()
            .role(Role.USER.getName())
            .content(List.of(AliyunContent.builder().text(content).build()))
            .build();
    }

    public static AliyunMessage buildUser(String content, List<String> images) {
        List<AliyunContent> list = new ArrayList<>();
        if (CollUtil.isNotEmpty(images)) {
            images.forEach(image -> list.add(AliyunContent.builder().image(image).build()));
        }
        if (StringUtils.isNotBlank(content)) {
            list.add(AliyunContent.builder().text(content).build());
        }
        return AliyunMessage.builder()
            .role(Role.USER.getName())
            .content(list)
            .build();
    }

    public static AliyunMessage buildSystem(String content) {
        return AliyunMessage.builder()
            .role(Role.SYSTEM.getName())
            .content(List.of(AliyunContent.builder().text(content).build()))
            .build();
    }

    public static AliyunMessage buildMessage(MessageItem messageItem) {
        List<AliyunContent> list = new ArrayList<>();
        if (StringUtils.isNotBlank(messageItem.getImages())) {
            List<String> images = StringUtils.splitList(messageItem.getImages(), ",");
            images.forEach(image -> list.add(AliyunContent.builder().image(image).build()));
        }
        if (StringUtils.isNotBlank(messageItem.getContent())) {
            list.add(AliyunContent.builder().text(messageItem.getContent()).build());
        }
        return AliyunMessage.builder()
            .role(messageItem.getRole())
            .content(list)
            .build();
    }

    public static List<AliyunMessage> buildMessageList(String system, List<MessageItem> history, String content, List<String> images) {
        List<AliyunMessage> messageItems = new ArrayList<>();

        if (StringUtils.isNotEmpty(system)) {
            messageItems.add(AliyunMessage.buildSystem(system));
        }

        if (CollUtil.isNotEmpty(history)) {
            List<AliyunMessage> list = history.stream().map(AliyunMessage::buildMessage).toList();
            messageItems.addAll(list);
        }

        messageItems.add(AliyunMessage.buildUser(content, images));

        return messageItems;
    }

}
