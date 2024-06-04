package cn.com.chat.chat.chain.generation.image;

import cn.com.chat.chat.chain.response.base.image.ImageResult;
import cn.com.chat.chat.chain.enums.ImageChatType;
import cn.com.chat.chat.chain.generation.GenerationService;

/**
 * 图像聊天服务接口
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-21
 */
public interface ImageChatService extends GenerationService {

    default ImageResult blockGenImage(String prompt) {
        return null;
    }

    default ImageResult blockGenImage(ImageChatType imageChatType, String prompt) {
        return null;
    }

}
