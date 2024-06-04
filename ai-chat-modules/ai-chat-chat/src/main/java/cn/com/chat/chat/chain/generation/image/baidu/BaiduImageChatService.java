package cn.com.chat.chat.chain.generation.image.baidu;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.com.chat.chat.chain.auth.baidu.BaiduAccessTokenService;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.enums.model.BaiduModelEnums;
import cn.com.chat.chat.chain.generation.image.ImageChatService;
import cn.com.chat.chat.chain.request.baidu.image.BaiduImageRequest;
import cn.com.chat.chat.chain.response.baidu.image.BaiduImageData;
import cn.com.chat.chat.chain.response.baidu.image.BaiduImageResult;
import cn.com.chat.chat.chain.response.base.image.ImageResult;
import cn.com.chat.chat.chain.utils.HttpUtils;
import cn.com.chat.chat.chain.utils.ImageUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 百度图像生成
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BaiduImageChatService implements ImageChatService {

    private final BaiduAccessTokenService accessTokenService;

    @Override
    public ImageResult blockGenImage(String prompt) {

        String url = BaiduModelEnums.STABLE_DIFFUSION_XL.getUrl();

        BaiduImageRequest request = BaiduImageRequest.builder()
            .prompt(prompt)
            .build();

        log.info("BaiduImageChatService -> 请求参数 ： {}", JsonUtils.toJsonString(request));

        HttpEntity<BaiduImageRequest> entity = new HttpEntity<>(request);

        BaiduImageResult baiduImageResult = HttpUtils.getRestTemplate().postForObject(accessTokenService.getUrl(url), entity, BaiduImageResult.class);

        List<BaiduImageData> list = Objects.requireNonNull(baiduImageResult)
            .getData()
            .stream()
            .peek(s -> s.setB64Image(ImageUtils.base64ToUrl(TextChatType.BAIDU.name(), s.getB64Image())))
            .toList();

        baiduImageResult.setData(list);

        List<String> imageList = list.stream().map(BaiduImageData::getB64Image).toList();

        ImageResult imageResult = ImageResult.builder()
            .model(TextChatType.BAIDU.name())
            .version(BaiduModelEnums.STABLE_DIFFUSION_XL.getName())
            .data(imageList)
            .totalTokens(Long.valueOf(baiduImageResult.getUsage().getTotalTokens()))
            .response(JsonUtils.toJsonString(baiduImageResult))
            .build();

        log.info("BaiduImageChatService -> 返回结果 ： {}", imageResult);

        return imageResult;
    }

}