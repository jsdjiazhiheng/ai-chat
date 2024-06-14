package cn.com.chat.chat.chain.generation.image.aliyun;

import cn.com.chat.chat.chain.apis.AliyunApis;
import cn.com.chat.chat.chain.auth.aliyun.AliyunAccessTokenService;
import cn.com.chat.chat.chain.enums.ImageChatType;
import cn.com.chat.chat.chain.enums.model.AliyunModelEnums;
import cn.com.chat.chat.chain.exception.ImageChatException;
import cn.com.chat.chat.chain.generation.image.ImageChatService;
import cn.com.chat.chat.chain.request.aliyun.image.AliyunImageInput;
import cn.com.chat.chat.chain.request.aliyun.image.AliyunImageParameters;
import cn.com.chat.chat.chain.request.aliyun.image.AliyunImageRequest;
import cn.com.chat.chat.chain.response.aliyun.image.AliyunImageResult;
import cn.com.chat.chat.chain.response.aliyun.image.AliyunImageTask;
import cn.com.chat.chat.chain.response.base.image.ImageData;
import cn.com.chat.chat.chain.response.base.image.ImageResult;
import cn.com.chat.chat.chain.utils.ChatLogUtils;
import cn.com.chat.chat.chain.utils.ImageUtils;
import cn.com.chat.common.http.utils.HttpUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import cn.hutool.core.thread.ThreadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 阿里云图像生成
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-06-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliyunImageChatService implements ImageChatService {

    private final AliyunAccessTokenService accessTokenService;

    @Override
    public ImageResult blockGenImage(String prompt) {

        AliyunImageInput input = AliyunImageInput.builder().prompt(prompt).build();

        AliyunImageParameters parameters = AliyunImageParameters.builder().build();

        AliyunImageRequest request = AliyunImageRequest.builder()
            .model(AliyunModelEnums.WANX_V1.getModel())
            .input(input)
            .parameters(parameters)
            .build();

        ChatLogUtils.printRequestLog(this.getClass(), request);

        String response = HttpUtils.doPostJson(AliyunApis.WANX_V1_API, request, getHeader());

        ChatLogUtils.printResponseLog(this.getClass(), response);

        AliyunImageTask task = JsonUtils.parseObject(response, AliyunImageTask.class);

        String taskId = Objects.requireNonNull(task).getOutput().getTaskId();

        AliyunImageResult imageResult = getImageResult(taskId);

        if (Objects.isNull(imageResult) || "FAILED".equalsIgnoreCase(imageResult.getOutput().getTaskStatus())) {
            throw new ImageChatException("生成失败");
        }

        List<ImageData> results = imageResult.getOutput().getResults();

        List<ImageData> list = results.stream()
            .peek(s -> s.setUrl(ImageUtils.urlToUrl(ImageChatType.ALIYUN.name(), s.getUrl())))
            .toList();

        imageResult.getOutput().setResults(list);

        List<String> imageList = list.stream().map(ImageData::getUrl).toList();

        ImageResult result = ImageResult.builder()
            .model(ImageChatType.ALIYUN.name())
            .version(AliyunModelEnums.WANX_V1.getModel())
            .data(imageList)
            .totalTokens(imageResult.getUsage().getImageCount().longValue())
            .response(JsonUtils.toJsonString(imageResult))
            .build();

        ChatLogUtils.printResultLog(this.getClass(), result);

        return result;
    }

    private AliyunImageResult getImageResult(String taskId) {

        String response = HttpUtils.doGet(AliyunApis.WANX_TASK_API + taskId, null, getHeader());

        ChatLogUtils.printResponseLog(this.getClass(), response);

        AliyunImageResult imageResult = JsonUtils.parseObject(response, AliyunImageResult.class);

        if (Objects.nonNull(imageResult)) {
            if (!"SUCCEEDED".equalsIgnoreCase(imageResult.getOutput().getTaskStatus()) && !"FAILED".equalsIgnoreCase(imageResult.getOutput().getTaskStatus())) {
                ThreadUtil.safeSleep(1000);
                return getImageResult(taskId);
            }
        }
        return imageResult;
    }

    private Map<String, String> getHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessTokenService.getAccessToken());
        headers.put("X-DashScope-Async", "enable");
        return headers;
    }

}
