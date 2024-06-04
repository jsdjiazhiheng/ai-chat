package cn.com.chat.chat.chain.response.czhan;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 触站返回结果
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CZhanResult<T> implements Serializable {

    private Integer code;

    private Boolean success;

    private String msg;

    private T data;

}
