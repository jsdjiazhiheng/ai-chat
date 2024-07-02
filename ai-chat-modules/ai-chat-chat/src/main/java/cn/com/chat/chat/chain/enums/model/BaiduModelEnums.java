package cn.com.chat.chat.chain.enums.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-02
 */
@Getter
@AllArgsConstructor
public enum BaiduModelEnums {

    /**
     * 对话
     */
    ERNIE_3_5_8K("ERNIE-3.5-8K", "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions"),
    ERNIE_3_5_4K("ERNIE-3.5-4K-0205", "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie-3.5-4k-0205"),
    ERNIE_4_0_8K("ERNIE-4.0-8K", "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro"),

    ERNIE_SPEED_128K("ERNIE_SPEED_128K", "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie-speed-128k"),

    /**
     * 向量
     */
    EMBEDDING_V1("Embedding-V1", "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/embeddings/embedding-v1"),

    /**
     * 图像
     */
    STABLE_DIFFUSION_XL("Stable-Diffusion-XL", "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/text2image/sd_xl"),
    FUYU_8B("Fuyu-8B", "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/image2text/fuyu_8b"),

    ;

    private final String model;
    private final String url;

    private static final Map<String, String> MODEL_MAP = new HashMap<>();

    static {
        for (BaiduModelEnums value : values()) {
            MODEL_MAP.put(value.getModel(), value.getUrl());
        }
    }

    public static String getModelUrl(String model) {
        return MODEL_MAP.get(model);
    }

}
