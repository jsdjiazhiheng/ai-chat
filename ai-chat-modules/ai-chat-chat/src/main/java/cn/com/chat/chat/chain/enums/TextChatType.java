package cn.com.chat.chat.chain.enums;

/**
 * 文本对话模型类型
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-01
 */
public enum TextChatType {

    /**
     * 接入类型
     */
    OPENAI,
    BAIDU,
    KIMI,
    ZHIPU,
    DEEPSEEK,
    ALIYUN,
    SPARK,
    VOLCENGINE,
    ;
    public static TextChatType getByName(String name) {
        for (TextChatType value : TextChatType.values()) {
            if (value.name().equals(name.toUpperCase())) {
                return value;
            }
        }
        return null;
    }

}
