package cn.com.chat.chat.chain.enums.model;

import cn.com.chat.chat.chain.apis.SparkApis;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 讯飞星火 模型枚举
 *
 * @author JiaZH
 * @date 2024-06-11
 */
@Getter
@AllArgsConstructor
public enum SparkModelEnums {

    /**
     * 讯飞模型
     */
    SPARK_MAX("Spark3.5 Max", "generalv3.5", SparkApis.SPARK_MAX_URL),
    SPARK_PRO("Spark Pro", "generalv3", SparkApis.SPARK_PRO_URL),
    SPARK_V2("Spark V2.0", "generalv2", SparkApis.SPARK_V2_URL),
    SPARK_LITE("Spark Lite", "general", SparkApis.SPARK_LITE_URL),

    SPARK_TTI("Spark TTI", "general", SparkApis.SPARK_TTI_URL),
    ;

    private final String model;
    private final String domain;
    private final String url;

    public static SparkModelEnums getByModel(String model) {
        for (SparkModelEnums value : values()) {
            if (value.getModel().equals(model)) {
                return value;
            }
        }
        return null;
    }

}
