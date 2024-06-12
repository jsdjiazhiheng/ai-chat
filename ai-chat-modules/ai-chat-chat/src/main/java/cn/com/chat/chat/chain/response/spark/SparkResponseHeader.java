package cn.com.chat.chat.chain.response.spark;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 讯飞响应头
 *
 * @author JiaZH
 * @date 2024-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class SparkResponseHeader implements Serializable {

    private Integer code;

    private String message;

    private String sid;

    private Integer status;

}
