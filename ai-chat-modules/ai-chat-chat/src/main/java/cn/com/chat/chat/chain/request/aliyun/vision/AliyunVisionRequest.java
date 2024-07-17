package cn.com.chat.chat.chain.request.aliyun.vision;

import cn.com.chat.chat.chain.request.aliyun.text.AliyunTextParameter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-07-16
 */
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class AliyunVisionRequest implements Serializable {

    private String model;

    private AliyunVisionInput input;

    private AliyunTextParameter parameter;

}
