package cn.com.chat.chat.chain.request.aliyun.text;

import cn.com.chat.chat.chain.request.base.text.MessageItem;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 阿里云文本输入
 *
 * @author JiaZH
 * @date 2024-06-05
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class AliyunTextInput implements Serializable {

    /**
     * 包含迄今为止对话的消息列表
     */
    protected List<MessageItem> messages;

}
