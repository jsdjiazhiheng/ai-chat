package cn.com.chat.chat.chain.request.base.vision;

import cn.com.chat.chat.chain.enums.Role;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.utils.ImageUtils;
import cn.com.chat.common.core.utils.StringUtils;
import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-07-17
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class VisionMessage implements Serializable {

    @NotNull
    private String role;

    @JsonProperty("content")
    private Object content;

    @JsonIgnore
    private static final String CONTENT_TIPS = "\n\n注意：回答问题时，须严格根据我给你的系统上下文内容原文进行回答，请不要自己发挥,回答时保持原来文本的段落层级，直接回答问题，无需重复问题的内容";


    public static VisionMessage buildSystem(String content) {
        return builder().role(Role.SYSTEM.getName()).content(List.of(MessageContent.builder().type("text").text(content).build())).build();
    }

    public static VisionMessage buildBase64System(String content) {
        return builder().role(Role.SYSTEM.getName()).content(content).build();
    }

    public static VisionMessage buildUser(String content, List<String> images) {
        List<MessageContent> list = new ArrayList<>();
        list.add(MessageContent.builder().type("text").text(content).build());
        if (CollUtil.isNotEmpty(images)) {
            images.forEach(image -> list.add(MessageContent.builder().type("image_url").imageUrl(ImageList.builder().url(image).build()).build()));
        }
        return builder().role(Role.USER.getName()).content(list).build();
    }

    public static VisionMessage buildMessage(MessageItem item) {
        if (StringUtils.isNotBlank(item.getImages())) {
            List<MessageContent> list = new ArrayList<>();
            list.add(MessageContent.builder().type("text").text(item.getContent()).build());
            List<String> images = StringUtils.splitList(item.getImages(), ",");
            if (CollUtil.isNotEmpty(images)) {
                images.forEach(image -> list.add(MessageContent.builder().type("image_url").imageUrl(ImageList.builder().url(image).build()).build()));
            }
            return builder().role(item.getRole()).content(list).build();
        } else {
            return builder().role(item.getRole()).content(List.of(MessageContent.builder().type("text").text(item.getContent()).build())).build();
        }
    }

    public static VisionMessage buildBase64Message(MessageItem item) {
        if (StringUtils.isNotBlank(item.getImages())) {
            List<MessageContent> list = new ArrayList<>();
            list.add(MessageContent.builder().type("text").text(item.getContent()).build());
            List<String> images = StringUtils.splitList(item.getImages(), ",");
            if (CollUtil.isNotEmpty(images)) {
                images.forEach(image -> list.add(MessageContent.builder().type("image_url").imageUrl(ImageList.builder().url(ImageUtils.urlToBase64(image, true)).build()).build()));
            }
            return builder().role(item.getRole()).content(list).build();
        } else {
            return builder().role(item.getRole()).content(item.getContent()).build();
        }
    }

    public static List<VisionMessage> buildMessageList(List<MessageItem> history) {
        return history.stream().map(VisionMessage::buildMessage).toList();
    }

    public static List<VisionMessage> buildBase64MessageList(List<MessageItem> history) {
        return history.stream().map(VisionMessage::buildBase64Message).toList();
    }

    public static List<VisionMessage> buildMessageList(String system, List<MessageItem> history, String content, List<String> images) {
        List<VisionMessage> messageItems = new ArrayList<>();

        if (StringUtils.isNotEmpty(system)) {
            messageItems.add(VisionMessage.buildSystem(system));
        }

        if (CollUtil.isNotEmpty(history)) {
            messageItems.addAll(buildMessageList(history));
        }

        messageItems.add(VisionMessage.buildUser(CollUtil.isEmpty(history) ? content : content + CONTENT_TIPS, images));

        return messageItems;
    }

    public static List<VisionMessage> buildBase64MessageList(String system, List<MessageItem> history, String content, List<String> images) {
        List<VisionMessage> messageItems = new ArrayList<>();

        if (StringUtils.isNotEmpty(system)) {
            messageItems.add(VisionMessage.buildBase64System(system));
        }

        if (CollUtil.isNotEmpty(history)) {
            messageItems.addAll(buildBase64MessageList(history));
        }

        messageItems.add(VisionMessage.buildUser(CollUtil.isEmpty(history) ? content : content + CONTENT_TIPS, images));

        return messageItems;
    }

    public static VisionMessage.Builder builder() {
        return new VisionMessage.Builder();
    }

    private VisionMessage(VisionMessage.Builder builder) {
        setRole(builder.role);
        setContent(builder.content);
    }

    public static final class Builder {
        private @NotNull String role;
        private Object content;

        public Builder() {
        }

        public VisionMessage.Builder role(@NotNull String role) {
            this.role = role;
            return this;
        }

        public VisionMessage.Builder content(List<MessageContent> content) {
            this.content = content;
            return this;
        }

        public VisionMessage.Builder content(String content) {
            this.content = content;
            return this;
        }

        public VisionMessage build() {
            return new VisionMessage(this);
        }
    }

}
