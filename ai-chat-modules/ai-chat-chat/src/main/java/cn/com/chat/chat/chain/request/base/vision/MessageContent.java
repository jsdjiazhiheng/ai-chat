package cn.com.chat.chat.chain.request.base.vision;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-07-17
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class MessageContent implements Serializable {

    private String type;

    private String text;

    @JsonProperty("image_url")
    private ImageList imageUrl;

}
