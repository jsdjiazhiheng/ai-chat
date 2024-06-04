package cn.com.chat.chat.chain.generation.image.czhan;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.com.chat.chat.chain.apis.CZhanApis;
import cn.com.chat.chat.chain.auth.czhan.CZhanAccessTokenService;
import cn.com.chat.chat.chain.enums.ImageChatType;
import cn.com.chat.chat.chain.generation.image.ImageChatService;
import cn.com.chat.chat.chain.request.czhan.image.CZhanImageRequest;
import cn.com.chat.chat.chain.request.czhan.image.CZhanImageTaskRequest;
import cn.com.chat.chat.chain.response.base.image.ImageResult;
import cn.com.chat.chat.chain.response.czhan.CZhanResult;
import cn.com.chat.chat.chain.response.czhan.image.CZhanImageData;
import cn.com.chat.chat.chain.response.czhan.image.CZhanImageResult;
import cn.com.chat.chat.chain.response.czhan.image.CZhanImageTask;
import cn.com.chat.chat.chain.utils.HttpUtils;
import cn.com.chat.chat.chain.utils.ImageUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p><b>触站AI画图服务</b></p>
 * <p>文档地址：<a href="https://open.czhanai.com/platform/doc">https://open.czhanai.com/platform/doc</a></p>
 * <p>模型获取接口：<a href="https://rt.huashi6.com/front/ai/mode/list/v2">https://rt.huashi6.com/front/ai/mode/list/v2</a></p>
 * <p>封面地址：<a href="https://img2.huashi6.com/images/cover/global/2024/01/08/105155_31507121297.jpg">919 通用模型 封面</a></p>
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CZhanImageChatService implements ImageChatService {

    private final CZhanAccessTokenService accessTokenService;

    @Override
    public ImageResult blockGenImage(String prompt) {
        CZhanImageRequest request = CZhanImageRequest.builder()
            .prompt(prompt)
            .build();

        log.info("CZhanImageChatService -> 请求参数 ： {}", JsonUtils.toJsonString(request));

        HttpHeaders header = getHeader();

        HttpEntity<CZhanImageRequest> entity = new HttpEntity<>(request, header);

        String response = HttpUtils.getRestTemplate().postForObject(CZhanApis.DRAW_API, entity, String.class);

        CZhanResult<CZhanImageTask> taskCZhanResult = JsonUtils.parseObject(response, new TypeReference<>() {
        });

        CZhanImageTask task = Objects.requireNonNull(taskCZhanResult).getData();
        String paintingSign = task.getPaintingSign();

        CZhanResult<CZhanImageResult> imageResult = getImageResult(paintingSign);

        CZhanImageResult resultData = imageResult.getData();

        List<CZhanImageData> list = resultData.getImages()
            .stream()
            .peek(s -> s.setImageUrl(ImageUtils.urlToUrl(ImageChatType.CZHAN_AI.name(), s.getImageUrl())))
            .toList();

        resultData.setImages(list);

        List<String> imageList = list.stream().map(CZhanImageData::getImageUrl).toList();

        ImageResult result = ImageResult.builder()
            .model(ImageChatType.CZHAN_AI.name())
            .version("v1")
            .data(imageList)
            .totalTokens(Long.valueOf(task.getUsed()))
            .response(JsonUtils.toJsonString(resultData))
            .build();

        log.info("CZhanImageChatService -> 返回结果 ： {}", result);

        return result;
    }

    private CZhanResult<CZhanImageResult> getImageResult(String paintingSign) {
        CZhanImageTaskRequest request = CZhanImageTaskRequest.builder()
            .taskId(paintingSign)
            .build();

        HttpHeaders header = getHeader();

        HttpEntity<CZhanImageTaskRequest> entity = new HttpEntity<>(request, header);

        String response = HttpUtils.getRestTemplate().postForObject(CZhanApis.DRAW_TASK_API, entity, String.class);

        return JsonUtils.parseObject(response, new TypeReference<>() {
        });
    }

    private HttpHeaders getHeader() {
        HttpHeaders header = new HttpHeaders();
        header.add("Auth-Token", accessTokenService.getAccessToken());
        return header;
    }

}
