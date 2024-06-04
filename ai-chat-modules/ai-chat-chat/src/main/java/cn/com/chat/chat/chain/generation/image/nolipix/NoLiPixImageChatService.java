package cn.com.chat.chat.chain.generation.image.nolipix;

import cn.com.chat.chat.chain.generation.image.ImageChatService;
import cn.com.chat.chat.chain.response.base.image.ImageResult;

/**
 * 画宇宙图像生成
 * url <a href="https://creator.nolipix.com/">https://creator.nolipix.com/</a>
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-21
 */
public class NoLiPixImageChatService implements ImageChatService {

    @Override
    public ImageResult blockGenImage(String prompt) {
        return ImageChatService.super.blockGenImage(prompt);
    }

}
