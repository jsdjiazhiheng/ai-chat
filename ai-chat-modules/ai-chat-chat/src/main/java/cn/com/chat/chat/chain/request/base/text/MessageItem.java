package cn.com.chat.chat.chain.request.base.text;

import cn.com.chat.chat.chain.enums.Role;
import cn.com.chat.chat.chain.response.base.text.TextToolCall;
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
 * 消息项
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-01
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class MessageItem implements Serializable {

    @NotNull
    private String role;
    private String content;
    private String name;

    private String images;

    @JsonProperty("content_type")
    private String contentType;

    @JsonProperty("tool_calls")
    private List<TextToolCall> toolCalls;

    @JsonIgnore
    private static final String CONTENT_TIPS = "\n\n注意：回答问题时，须严格根据我给你的系统上下文内容原文进行回答，请不要自己发挥,回答时保持原来文本的段落层级，直接回答问题，无需重复问题的内容";

    public static MessageItem buildSystem(String content) {
        return builder().role(Role.SYSTEM.getName()).content(content).build();
    }

    public static MessageItem buildUser(String content) {
        return builder().role(Role.USER.getName()).content(content).build();
    }

    public static MessageItem buildUser(String content, String contentType) {
        return builder().role(Role.USER.getName()).content(content).contentType(contentType).build();
    }

    public static MessageItem buildAssistant(String content) {
        return builder().role(Role.ASSISTANT.getName()).content(content).build();
    }

    public static MessageItem buildMessage(String role, String content) {
        return builder().role(role).content(content).build();
    }

    public static List<MessageItem> buildMessageList(String system, List<MessageItem> history, String content) {
        List<MessageItem> messageItems = new ArrayList<>();

        if (StringUtils.isNotEmpty(system)) {
            messageItems.add(MessageItem.buildSystem(system));
        }

        if (CollUtil.isNotEmpty(history)) {
            messageItems.addAll(history);
        }

        messageItems.add(MessageItem.buildUser(CollUtil.isEmpty(history) ? content : content + CONTENT_TIPS));

        return messageItems;
    }

    public static Builder builder() {
        return new Builder();
    }

    private MessageItem(Builder builder) {
        setRole(builder.role);
        setContent(builder.content);
        setContentType(builder.contentType);
    }

    public static final class Builder {
        private @NotNull String role;
        private String content;
        private String contentType;

        public Builder() {
        }

        public Builder role(@NotNull String role) {
            this.role = role;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public MessageItem build() {
            return new MessageItem(this);
        }
    }

}
