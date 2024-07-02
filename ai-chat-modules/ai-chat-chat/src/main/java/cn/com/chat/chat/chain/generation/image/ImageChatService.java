package cn.com.chat.chat.chain.generation.image;

import cn.com.chat.chat.chain.enums.ImageChatType;
import cn.com.chat.chat.chain.generation.GenerationService;
import cn.com.chat.chat.chain.response.base.image.ImageResult;

/**
 * 图像聊天服务接口
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-21
 */
public interface ImageChatService extends GenerationService {

    /**
     * 生成图片
     * @param prompt 提示
     * @return 图片结果
     */
    default ImageResult blockGenImage(String prompt) {
        return null;
    }

    /**
     * 生成图片
     * @param imageChatType 图像聊天类型
     * @param prompt 提示
     * @return 图片结果
     */
    default ImageResult blockGenImage(ImageChatType imageChatType, String prompt) {
        return null;
    }

}
