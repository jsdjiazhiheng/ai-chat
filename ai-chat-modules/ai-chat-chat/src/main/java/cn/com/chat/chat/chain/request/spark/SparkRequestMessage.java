package cn.com.chat.chat.chain.request.spark;

import cn.com.chat.chat.chain.request.base.text.MessageItem;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 讯飞星火 请求消息
 *
 * @author JiaZH
 * @date 2024-06-11
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class SparkRequestMessage implements Serializable {

    private List<MessageItem> text;

}
