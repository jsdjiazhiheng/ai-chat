package cn.com.chat.chat.chain.request.base.text;

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文本工具函数
 *
 * @author JiaZH
 * @date 2024-06-05
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class TextToolFunction implements Serializable {

    private String name;

    private String description;

    private JSONObject parameters;

}
