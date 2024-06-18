package cn.com.chat.chat.chain.request.volcengine.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-06-18
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class VolcengineStreamOption implements Serializable {

    @JsonProperty("include_usage")
    private Boolean includeUsage;

}
