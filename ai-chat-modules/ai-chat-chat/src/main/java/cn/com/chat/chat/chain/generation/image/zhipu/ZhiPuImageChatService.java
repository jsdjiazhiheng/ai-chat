package cn.com.chat.chat.chain.generation.image.zhipu;

import cn.com.chat.chat.chain.apis.ZhiPuApis;
import cn.com.chat.chat.chain.auth.zhipu.ZhiPuAccessTokenService;
import cn.com.chat.chat.chain.enums.ImageChatType;
import cn.com.chat.chat.chain.enums.model.ZhiPuModelEnums;
import cn.com.chat.chat.chain.generation.image.ImageChatService;
import cn.com.chat.chat.chain.request.zhipu.image.ZhiPuImageRequest;
import cn.com.chat.chat.chain.response.base.image.ImageData;
import cn.com.chat.chat.chain.response.base.image.ImageResult;
import cn.com.chat.chat.chain.response.zhipu.image.ZhiPuImageResult;
import cn.com.chat.chat.chain.utils.ChatLogUtils;
import cn.com.chat.chat.chain.utils.ImageUtils;
import cn.com.chat.common.http.utils.HttpUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 智谱图像生成
 * 0.25元 / 张
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ZhiPuImageChatService implements ImageChatService {

    private final ZhiPuAccessTokenService accessTokenService;

    @Override
    public ImageResult blockGenImage(String prompt) {
        ZhiPuImageRequest request = ZhiPuImageRequest.builder()
            .model(ZhiPuModelEnums.COG_VIEW.getModel())
            .prompt(prompt)
            .build();

        ChatLogUtils.printRequestLog(this.getClass(), request);

        Map<String, String> header = getHeader();

        String response = HttpUtils.doPostJson(ZhiPuApis.IMAGE_API, request, header);

        ChatLogUtils.printResponseLog(this.getClass(), response);

        ZhiPuImageResult object = JsonUtils.parseObject(response, ZhiPuImageResult.class);

        List<ImageData> list = Objects.requireNonNull(object)
            .getData()
            .stream()
            .peek(s -> s.setUrl(ImageUtils.urlToUrl(ImageChatType.ZHIPU.name(), s.getUrl())))
            .toList();

        object.setData(list);

        List<String> imageList = list.stream().map(ImageData::getUrl).toList();

        ImageResult result = ImageResult.builder()
            .model(ImageChatType.ZHIPU.name())
            .version(ZhiPuModelEnums.COG_VIEW.getModel())
            .data(imageList)
            .totalTokens(25L)
            .response(JsonUtils.toJsonString(object))
            .build();

        ChatLogUtils.printResultLog(this.getClass(), result);

        return result;
    }

    private Map<String, String> getHeader() {
        return Map.of("Authorization", "Bearer " + accessTokenService.getAccessToken());
    }

}
