package cn.com.chat.chat.chain.generation.image;

import cn.com.chat.chat.chain.response.base.image.ImageResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.com.chat.chat.chain.enums.ImageChatType;
import cn.com.chat.chat.chain.function.service.ICompletionService;
import cn.com.chat.common.core.exception.ServiceException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * TODO
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-21
 */
@Slf4j
@Service
@Primary
@AllArgsConstructor
public class ImageChatServiceWrapper implements ImageChatService {

    private final ImageChatServiceFactory imageChatServiceFactory;
    private final ICompletionService completionService;

    @Override
    public ImageResult blockGenImage(ImageChatType imageChatType, String prompt) {
        ImageChatService imageChatService = imageChatServiceFactory.getImageChatService(imageChatType);
        if(Objects.isNull(imageChatService)) {
            throw new ServiceException("不支持的图像模型服务");
        }
        //优化提示词
        prompt = completionService.functionDrawPrompt(prompt);
        return imageChatService.blockGenImage(prompt);
    }

}
