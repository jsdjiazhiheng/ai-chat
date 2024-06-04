package cn.com.chat.chat.chain.response.base.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 图像响应结果
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ImageResult implements Serializable {

    private List<String> data;

    private String model;

    private String version;

    private Long totalTokens;

    private String response;

}
