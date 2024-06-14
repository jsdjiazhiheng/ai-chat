package cn.com.chat.chat.chain.generation.image.baidu;

import cn.com.chat.chat.chain.auth.baidu.BaiduAccessTokenService;
import cn.com.chat.chat.chain.enums.ImageChatType;
import cn.com.chat.chat.chain.enums.model.BaiduModelEnums;
import cn.com.chat.chat.chain.exception.ImageChatException;
import cn.com.chat.chat.chain.generation.image.ImageChatService;
import cn.com.chat.chat.chain.request.baidu.image.BaiduImageRequest;
import cn.com.chat.chat.chain.response.baidu.image.BaiduImageData;
import cn.com.chat.chat.chain.response.baidu.image.BaiduImageResult;
import cn.com.chat.chat.chain.response.base.image.ImageResult;
import cn.com.chat.chat.chain.utils.ChatLogUtils;
import cn.com.chat.chat.chain.utils.ImageUtils;
import cn.com.chat.common.http.utils.HttpUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import cn.hutool.json.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        ChatLogUtils.printRequestLog(this.getClass(), request);

        HttpEntity<BaiduImageRequest> entity = new HttpEntity<>(request);

        String response = HttpUtils.doPostJson(accessTokenService.getUrl(url), entity);

        ChatLogUtils.printResponseLog(this.getClass(), response);

        if (response.contains("error_code")) {
            JSONObject object = JsonUtils.parseObject(response, JSONObject.class);
            String errorMsg = Objects.requireNonNull(object).getStr("error_msg");
            throw new ImageChatException(errorMsg);
        }

        BaiduImageResult baiduImageResult = JsonUtils.parseObject(response, BaiduImageResult.class);

        List<BaiduImageData> list = Objects.requireNonNull(baiduImageResult)
            .getData()
            .stream()
            .peek(s -> s.setB64Image(ImageUtils.base64ToUrl(ImageChatType.BAIDU.name(), s.getB64Image())))
            .toList();

        baiduImageResult.setData(list);

        List<String> imageList = list.stream().map(BaiduImageData::getB64Image).toList();

        ImageResult result = ImageResult.builder()
            .model(ImageChatType.BAIDU.name())
            .version(BaiduModelEnums.STABLE_DIFFUSION_XL.getModel())
            .data(imageList)
            .totalTokens(Long.valueOf(baiduImageResult.getUsage().getTotalTokens()))
            .response(JsonUtils.toJsonString(baiduImageResult))
            .build();

        ChatLogUtils.printResultLog(this.getClass(), result);

        return result;
    }

}
