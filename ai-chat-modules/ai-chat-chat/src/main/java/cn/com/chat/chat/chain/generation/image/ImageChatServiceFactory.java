package cn.com.chat.chat.chain.generation.image;

import cn.com.chat.chat.chain.enums.ImageChatType;
import cn.com.chat.chat.chain.generation.image.aliyun.AliyunImageChatService;
import cn.com.chat.chat.chain.generation.image.baidu.BaiduImageChatService;
import cn.com.chat.chat.chain.generation.image.czhan.CZhanImageChatService;
import cn.com.chat.chat.chain.generation.image.nolipix.NoLiPixImageChatService;
import cn.com.chat.chat.chain.generation.image.openai.OpenAiImageChatService;
import cn.com.chat.chat.chain.generation.image.spark.SparkImageChatService;
import cn.com.chat.chat.chain.generation.image.zhipu.ZhiPuImageChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 图像聊天服务工厂
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-21
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ImageChatServiceFactory {

    private final BaiduImageChatService baiduImageChatService;
    private final ZhiPuImageChatService zhiPuImageChatService;
    private final CZhanImageChatService cZhanImageChatService;
    private final AliyunImageChatService aliyunImageChatService;
    private final OpenAiImageChatService openAiImageChatService;
    private final SparkImageChatService sparkImageChatService;
    private final NoLiPixImageChatService noLiPixImageChatService;

    public ImageChatService getImageChatService(ImageChatType type) {
        //TODO 等待模型接入
        if (type == ImageChatType.BAIDU) {
            return baiduImageChatService;
        } else if (type == ImageChatType.ZHIPU) {
            return zhiPuImageChatService;
        } else if (type == ImageChatType.ALIYUN) {
            return aliyunImageChatService;
        } else if (type == ImageChatType.CZHAN_AI) {
            return cZhanImageChatService;
        } else if (type == ImageChatType.YIJIAN) {
            return null;
        } else if (type == ImageChatType.OPENAI) {
            return openAiImageChatService;
        } else if (type == ImageChatType.SPARK) {
            return sparkImageChatService;
        } else if (type == ImageChatType.NOLIBOX) {
            return noLiPixImageChatService;
        }
        return null;
    }

}
