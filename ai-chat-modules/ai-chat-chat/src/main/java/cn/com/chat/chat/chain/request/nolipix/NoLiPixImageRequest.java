package cn.com.chat.chat.chain.request.nolipix;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-06-13
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class NoLiPixImageRequest implements Serializable {

    @NonNull
    private String task;

    @NotNull
    private NoLiPixImageParams params;

    @JsonProperty("notify_url")
    private String notifyUrl;

}
