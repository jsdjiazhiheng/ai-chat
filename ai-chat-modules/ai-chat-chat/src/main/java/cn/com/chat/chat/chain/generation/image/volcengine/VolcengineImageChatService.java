package cn.com.chat.chat.chain.generation.image.volcengine;

import cn.com.chat.chat.chain.auth.volcengine.VolcengineAccessTokenService;
import cn.com.chat.chat.chain.enums.ImageChatType;
import cn.com.chat.chat.chain.generation.image.ImageChatService;
import cn.com.chat.chat.chain.request.volcengine.imge.VolcengineImageRequest;
import cn.com.chat.chat.chain.response.base.image.ImageResult;
import cn.com.chat.chat.chain.utils.ChatLogUtils;
import cn.com.chat.chat.chain.utils.ImageUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import com.volcengine.service.visual.IVisualService;
import com.volcengine.service.visual.impl.VisualServiceImpl;
import com.volcengine.service.visual.model.response.VisualHighAesSmartDrawingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * 豆包图像服务
 *
 * @author JiaZH
 * @date 2024-06-18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VolcengineImageChatService implements ImageChatService {

    private final VolcengineAccessTokenService accessTokenService;

    @Override
    public ImageResult blockGenImage(String prompt) {

        IVisualService visualService = VisualServiceImpl.getInstance();

        visualService.setAccessKey(accessTokenService.getImageAccessKeyId());
        visualService.setSecretKey(accessTokenService.getImageSecretAccessKey());

        VolcengineImageRequest request = VolcengineImageRequest.builder()
            .prompt(prompt)
            .build();

        ChatLogUtils.printRequestLog(this.getClass(), request);

        ImageResult result = ImageResult.builder()
            .model(ImageChatType.VOLCENGINE.name())
            .version("v1.4")
            .totalTokens(2L)
            .build();

        try {
            VisualHighAesSmartDrawingResponse response = visualService.visualHighAesSmartDrawing(request);

            ArrayList<String> list = response.getData().getBinaryDataBase64();

            ArrayList<String> imageList = new ArrayList<>();

            list.stream()
                .map(s -> ImageUtils.base64ToUrl(ImageChatType.VOLCENGINE.name(), s))
                .forEach(imageList::add);

            response.getData().setBinaryDataBase64(imageList);

            ChatLogUtils.printResponseLog(this.getClass(), response);

            result.setData(imageList);
            result.setResponse(JsonUtils.toJsonString(response));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ChatLogUtils.printResultLog(this.getClass(), result);

        return result;
    }

}
