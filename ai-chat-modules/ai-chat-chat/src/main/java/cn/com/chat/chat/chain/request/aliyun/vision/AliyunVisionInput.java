package cn.com.chat.chat.chain.request.aliyun.vision;

import com.fasterxml.jackson.annotation.JsonInclude;
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
 * @date 2024-07-16
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class AliyunVisionInput implements Serializable {

    protected List<AliyunMessage> messages;

}
