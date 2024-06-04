package cn.com.chat.chat.chain.generation.image.zhipu;

import cn.com.chat.chat.chain.response.zhipu.image.ZhiPuImageData;
import cn.com.chat.chat.chain.response.zhipu.image.ZhiPuImageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.com.chat.chat.chain.apis.ZhiPuApis;
import cn.com.chat.chat.chain.auth.zhipu.ZhiPuAccessTokenService;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.enums.model.ZhiPuModelEnums;
import cn.com.chat.chat.chain.generation.image.ImageChatService;
import cn.com.chat.chat.chain.request.zhipu.image.ZhiPuImageRequest;
import cn.com.chat.chat.chain.response.base.image.ImageResult;
import cn.com.chat.chat.chain.utils.HttpUtils;
import cn.com.chat.chat.chain.utils.ImageUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
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

    private ZhiPuAccessTokenService accessTokenService;

    @Override
    public ImageResult blockGenImage(String prompt) {
        ZhiPuImageRequest request = ZhiPuImageRequest.builder()
            .model(ZhiPuModelEnums.COG_VIEW.getModel())
            .prompt(prompt)
            .build();

        log.info("ZhiPuImageChatService -> 请求参数 ： {}", JsonUtils.toJsonString(request));

        HttpHeaders header = getHeader();

        HttpEntity<ZhiPuImageRequest> entity = new HttpEntity<>(request, header);

        ZhiPuImageResult object = HttpUtils.getRestTemplate().postForObject(ZhiPuApis.IMAGE_API, entity, ZhiPuImageResult.class);

        List<ZhiPuImageData> list = Objects.requireNonNull(object)
            .getData()
            .stream()
            .peek(s -> s.setUrl(ImageUtils.urlToUrl(TextChatType.ZHIPU.name(), s.getUrl())))
            .toList();

        object.setData(list);

        List<String> imageList = list.stream().map(ZhiPuImageData::getUrl).toList();

        ImageResult result = ImageResult.builder()
            .model(TextChatType.ZHIPU.name())
            .version(ZhiPuModelEnums.COG_VIEW.getModel())
            .data(imageList)
            .totalTokens(25L)
            .response(JsonUtils.toJsonString(object))
            .build();

        log.info("ZhiPuImageChatService -> 返回结果 ： {}", result);

        return result;
    }

    private HttpHeaders getHeader() {
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + accessTokenService.getAccessToken());
        return header;
    }

}
