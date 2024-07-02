package cn.com.chat.chat.chain.enums;

/**
 * 图像对话模型类型
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-21
 */
public enum ImageChatType {

    /**
     * 接入类型
     */
    OPENAI,
    BAIDU,
    ZHIPU,
    ALIYUN,
    CZHAN_AI,
    YIJIAN,
    NOLIBOX,
    SPARK,
    VOLCENGINE,
    ;

    public static ImageChatType getByName(String name) {
        for (ImageChatType value : values()) {
            if (value.name().equals(name.toUpperCase())) {
                return value;
            }
        }
        return null;
    }

}
