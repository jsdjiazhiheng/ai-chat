package cn.com.chat.chat.chain.enums.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 智谱模型列表
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-15
 */
@AllArgsConstructor
@Getter
public enum ZhiPuModelEnums {

    /**
     * 智谱模型
     */
    GLM_4("glm-4"),
    GLM_4V("glm-4v"),
    GLM_3_TURBO("glm-3-turbo"),

    COG_VIEW("cogview-3"),

    CHARACTER_GLM("charglm-3"),

    EMBEDDING_2("embedding-2"),
    ;

    private final String model;

}
