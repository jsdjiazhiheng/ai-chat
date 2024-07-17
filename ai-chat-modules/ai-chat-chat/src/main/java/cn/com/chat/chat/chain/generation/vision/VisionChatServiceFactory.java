package cn.com.chat.chat.chain.generation.vision;

import cn.com.chat.chat.chain.enums.VisionChatType;
import cn.com.chat.chat.chain.enums.model.AliyunModelEnums;
import cn.com.chat.chat.chain.enums.model.SparkModelEnums;
import cn.com.chat.chat.chain.enums.model.VolcengineModelEnums;
import cn.com.chat.chat.chain.enums.model.ZhiPuModelEnums;
import cn.com.chat.chat.chain.generation.vision.aliyun.AliyunVisionChatService;
import cn.com.chat.chat.chain.generation.vision.spark.SparkVisionChatService;
import cn.com.chat.chat.chain.generation.vision.volcengine.VolcengineVisionChatService;
import cn.com.chat.chat.chain.generation.vision.zhipu.ZhiPuVisionChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 图文理解服务工厂
 *
 * @author JiaZH
 * @date 2024-07-16
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class VisionChatServiceFactory {

    private final AliyunVisionChatService aliyunVisionChatService;
    private final SparkVisionChatService sparkVisionChatService;
    private final ZhiPuVisionChatService zhiPuVisionChatService;
    private final VolcengineVisionChatService volcengineVisionChatService;

    public VisionChatService getVisionChatService(VisionChatType type) {
        return switch (type) {
            case ALIYUN -> aliyunVisionChatService;
            case SPARK -> sparkVisionChatService;
            case VOLCENGINE -> volcengineVisionChatService;
            case ZHIPU -> zhiPuVisionChatService;
        };
    }

    public String getModel(VisionChatType type) {
        return switch (type) {
            case ALIYUN -> AliyunModelEnums.QWEN_VL_PLUS.getModel();
            case SPARK -> SparkModelEnums.SPARK_IMAGE.getModel();
            case VOLCENGINE -> VolcengineModelEnums.DOUBAO_PRO_128K.getModel();
            case ZHIPU -> ZhiPuModelEnums.GLM_4V.getModel();
        };
    }

}
