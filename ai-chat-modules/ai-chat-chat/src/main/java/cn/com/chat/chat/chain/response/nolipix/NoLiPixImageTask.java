package cn.com.chat.chat.chain.response.nolipix;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 画图任务
 *
 * @author JiaZH
 * @date 2024-06-14
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class NoLiPixImageTask implements Serializable {

    private String uid;

}
