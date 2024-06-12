package cn.com.chat.chat.chain.generation.image.openai;

import cn.com.chat.chat.chain.apis.OpenAiApis;
import cn.com.chat.chat.chain.auth.openai.OpenAiAccessTokenService;
import cn.com.chat.chat.chain.enums.ImageChatType;
import cn.com.chat.chat.chain.enums.model.OpenAiModelEnums;
import cn.com.chat.chat.chain.generation.image.ImageChatService;
import cn.com.chat.chat.chain.response.base.image.ImageData;
import cn.com.chat.chat.chain.request.openai.image.OpenAiImageRequest;
import cn.com.chat.chat.chain.response.base.image.ImageResult;
import cn.com.chat.chat.chain.response.openai.image.OpenAiImageResult;
import cn.com.chat.chat.chain.service.HttpService;
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
 * OpenAI 图片生成
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-06-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiImageChatService implements ImageChatService {

    private final OpenAiAccessTokenService accessTokenService;
    private final HttpService httpService;

    @Override
    public ImageResult blockGenImage(String prompt) {

        OpenAiImageRequest request = OpenAiImageRequest.builder()
            .model(OpenAiModelEnums.DALL_E_3.getModel())
            .prompt(prompt)
            .build();

        log.info("OpenAiImageChatService -> 请求参数 ： {}", JsonUtils.toJsonString(request));

        String response;

        try {
            httpService.setProxyHttpUtils();

            response = HttpUtils.doPostJson(OpenAiApis.IMAGE_API, request, getHeader());

            log.info("OpenAiImageChatService -> 请求结果 ： {}", response);

        } finally {
            httpService.clearProxyHttpUtils();
        }

        OpenAiImageResult object = JsonUtils.parseObject(response, OpenAiImageResult.class);

        List<ImageData> list = Objects.requireNonNull(object)
            .getData()
            .stream()
            .peek(s -> s.setUrl(ImageUtils.urlToUrl(ImageChatType.OPENAI.name(), s.getUrl())))
            .toList();

        object.setData(list);

        List<String> imageList = list.stream().map(ImageData::getUrl).toList();

        ImageResult result = ImageResult.builder()
            .model(ImageChatType.OPENAI.name())
            .version(OpenAiModelEnums.DALL_E_3.getModel())
            .data(imageList)
            .totalTokens(25L)
            .response(JsonUtils.toJsonString(object))
            .build();

        log.info("OpenAiImageChatService -> 返回结果 ： {}", result);

        return result;
    }

    private Map<String, String> getHeader() {
        return Map.of("Authorization", "Bearer " + accessTokenService.getAccessToken());
    }

}
