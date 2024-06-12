package cn.com.chat.chat.chain.response.spark;

import cn.com.chat.chat.chain.request.base.text.MessageItem;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 讯飞响应 choices
 *
 * @author JiaZH
 * @date 2024-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class SparkResponsePayloadChoices implements Serializable {

    private Integer status;

    private Integer seq;

    private List<MessageItem> text;

}
