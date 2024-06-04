package cn.com.chat.chat.chain.response.kimi.text;

import cn.com.chat.chat.chain.response.base.Usage;
import cn.com.chat.chat.chain.response.base.text.TextChoice;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-05-27
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class KimiTextChoice extends TextChoice {

    private Usage usage;

}
