package cn.com.chat.chat.chain.request.zhipu.text;

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

/**
 * TODO
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-15
 */
@Data
@Builder
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ZhiPuTextFunction implements Serializable {

    private String name;

    private String description;

    private JSONObject parameters;

}
