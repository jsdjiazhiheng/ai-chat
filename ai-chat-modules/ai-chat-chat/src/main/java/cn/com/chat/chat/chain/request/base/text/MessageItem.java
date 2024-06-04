package cn.com.chat.chat.chain.request.base.text;

import cn.com.chat.chat.chain.enums.Role;
import cn.com.chat.chat.chain.response.base.text.TextToolCall;
import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.com.chat.common.core.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
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

    @JsonProperty("tool_calls")
    private List<TextToolCall> toolCalls;

    public static MessageItem buildSystem(String content) {
        return builder().role(Role.SYSTEM.getName()).content(content).build();
    }

    public static MessageItem buildUser(String content) {
        return builder().role(Role.USER.getName()).content(content).build();
    }

    public static MessageItem buildAssistant(String content) {
        return builder().role(Role.ASSISTANT.getName()).content(content).build();
    }

    public static List<MessageItem> buildMessageList(String system, List<MessageItem> history, String content) {
        List<MessageItem> messageItems = new ArrayList<>();

        if (StringUtils.isNotEmpty(system)) {
            messageItems.add(MessageItem.buildSystem(system));
        }

        if (CollUtil.isNotEmpty(history)) {
            messageItems.addAll(history);
        }

        messageItems.add(MessageItem.buildUser(content));

        return messageItems;
    }

    public static Builder builder() {
        return new Builder();
    }

    private MessageItem(Builder builder) {
        setRole(builder.role);
        setContent(builder.content);
    }

    public static final class Builder {
        private @NotNull String role;
        private String content;

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

        public MessageItem build() {
            return new MessageItem(this);
        }
    }

}