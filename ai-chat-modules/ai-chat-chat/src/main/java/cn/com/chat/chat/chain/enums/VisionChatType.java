package cn.com.chat.chat.chain.enums;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-07-16
 */
public enum VisionChatType {

    /**
     * 多态模型
     */
    ALIYUN,
    SPARK,
    ZHIPU,
    VOLCENGINE;

    public static VisionChatType getByName(String name) {
        for (VisionChatType value : values()) {
            if (value.name().equals(name.toUpperCase())) {
                return value;
            }
        }
        return null;
    }
}
