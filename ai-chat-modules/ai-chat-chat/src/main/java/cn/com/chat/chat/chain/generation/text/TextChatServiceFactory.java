package cn.com.chat.chat.chain.generation.text;

import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.enums.model.*;
import cn.com.chat.chat.chain.generation.text.aliyun.AliyunTextChatService;
import cn.com.chat.chat.chain.generation.text.baidu.BaiduTextChatService;
import cn.com.chat.chat.chain.generation.text.deepseek.DeepSeekTextChatService;
import cn.com.chat.chat.chain.generation.text.kimi.KimiTextChatService;
import cn.com.chat.chat.chain.generation.text.openai.OpenAiTextChatService;
import cn.com.chat.chat.chain.generation.text.spark.SparkTextChatService;
import cn.com.chat.chat.chain.generation.text.zhipu.ZhiPuTextChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 文本聊天服务工厂
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-01
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TextChatServiceFactory {

    private final KimiTextChatService kimiTextChatService;
    private final BaiduTextChatService baiduTextChatService;
    private final DeepSeekTextChatService deepSeekTextChatService;
    private final ZhiPuTextChatService zhiPuTextChatService;
    private final AliyunTextChatService aliyunTextChatService;
    private final OpenAiTextChatService openAiTextChatService;
    private final SparkTextChatService sparkTextChatService;

    public TextChatService getTextChatService(TextChatType type) {
        // TODO 等待模型接入
        if (type == TextChatType.OPENAI) {
            return openAiTextChatService;
        } else if (type == TextChatType.BAIDU) {
            return baiduTextChatService;
        } else if (type == TextChatType.KIMI) {
            return kimiTextChatService;
        } else if (type == TextChatType.ZHIPU) {
            return zhiPuTextChatService;
        } else if (type == TextChatType.DEEPSEEK) {
            return deepSeekTextChatService;
        } else if (type == TextChatType.ALIYUN) {
            return aliyunTextChatService;
        } else if (type == TextChatType.SPARK) {
            return sparkTextChatService;
        } else {
            return null;
        }
    }

    public String getTextChatType(TextChatType type) {
        if (type == TextChatType.OPENAI) {
            return OpenAiModelEnums.GPT_3_5_TURBO.getModel();
        } else if (type == TextChatType.BAIDU) {
            return BaiduModelEnums.ERNIE_4_0_8K.getModel();
        } else if (type == TextChatType.KIMI) {
            return KimiModelEnums.MOONSHOT_V1_8K.getModel();
        } else if (type == TextChatType.ZHIPU) {
            return ZhiPuModelEnums.GLM_3_TURBO.getModel();
        } else if (type == TextChatType.DEEPSEEK) {
            return DeepSeekModelEnums.DEEP_SEEK_CHAT.getModel();
        } else if (type == TextChatType.ALIYUN) {
            return AliyunModelEnums.QWEN_TURBO.getModel();
        } else if (type == TextChatType.SPARK) {
            return SparkModelEnums.SPARK_MAX.getModel();
        } else {
            return null;
        }
    }

}
