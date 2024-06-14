package cn.com.chat.chat.chain.response.nolipix;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 返回结果
 *
 * @author JiaZH
 * @date 2024-06-14
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class NoLiPixResult implements Serializable {

    private String status;

    private Integer pending;

    private NoLiPixImageResult data;

}
