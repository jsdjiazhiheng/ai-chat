package cn.com.chat.chat.chain.request.aliyun.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 阿里云文本请求
 *
 * @author JiaZH
 * @date 2024-06-05
 */
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class AliyunTextRequest implements Serializable {

    private String model;

    private AliyunTextInput input;

    private AliyunTextParameter parameter;

}
