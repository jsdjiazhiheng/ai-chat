package cn.com.chat.chat.chain.generation.image.yijian;

import cn.com.chat.chat.chain.generation.image.ImageChatService;
import cn.com.chat.chat.chain.response.base.image.ImageResult;

/**
 * 意间图像生成
 * url <a href="https://open.yjai.art/home">https://open.yjai.art/home</a>
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-21
 */
public class YiJianImageChatService implements ImageChatService {

    @Override
    public ImageResult blockGenImage(String prompt) {
        return ImageChatService.super.blockGenImage(prompt);
    }

}
