package cn.com.chat.chat.chain.generation.text;

import cn.com.chat.chat.chain.generation.text.baidu.BaiduTextChatService;
import cn.com.chat.chat.chain.generation.text.deepseek.DeepSeekTextChatService;
import cn.com.chat.chat.chain.generation.text.kimi.KimiTextChatService;
import cn.com.chat.chat.chain.generation.text.zhipu.ZhiPuTextChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.enums.model.BaiduModelEnums;
import cn.com.chat.chat.chain.enums.model.KimiModelEnums;
import cn.com.chat.chat.chain.enums.model.ZhiPuModelEnums;
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

    public TextChatService getTextChatService(TextChatType type) {
        // TODO 等待模型接入
        if (type == TextChatType.OPENAI) {
            return null;
        } else if (type == TextChatType.BAIDU) {
            return baiduTextChatService;
        } else if (type == TextChatType.KIMI) {
            return kimiTextChatService;
        } else if (type == TextChatType.ZHIPU) {
            return zhiPuTextChatService;
        } else if (type == TextChatType.DEEPSEEK) {
            return deepSeekTextChatService;
        } else {
            return null;
        }
    }

    public String getTextChatType(TextChatType type) {
        if (type == TextChatType.OPENAI) {
            return null;
        } else if (type == TextChatType.BAIDU) {
            return BaiduModelEnums.ERNIE_3_5_8K.getName();
        } else if (type == TextChatType.KIMI) {
            return KimiModelEnums.MOONSHOT_V1_8K.getModel();
        } else if (type == TextChatType.ZHIPU) {
            return ZhiPuModelEnums.GLM_3_TURBO.getModel();
        } else if (type == TextChatType.DEEPSEEK) {
            return "deepseek-chat";
        } else {
            return null;
        }
    }

}
