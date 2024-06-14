package cn.com.chat.chat.chain.generation.image.nolipix;

import cn.com.chat.chat.chain.apis.NoLiPixApis;
import cn.com.chat.chat.chain.auth.nolipix.NoLiPixAccessTokenService;
import cn.com.chat.chat.chain.enums.ImageChatType;
import cn.com.chat.chat.chain.exception.ImageChatException;
import cn.com.chat.chat.chain.generation.image.ImageChatService;
import cn.com.chat.chat.chain.request.nolipix.NoLiPixImageParams;
import cn.com.chat.chat.chain.request.nolipix.NoLiPixImageRequest;
import cn.com.chat.chat.chain.response.base.image.ImageResult;
import cn.com.chat.chat.chain.response.nolipix.NoLiPixImageTask;
import cn.com.chat.chat.chain.response.nolipix.NoLiPixResult;
import cn.com.chat.chat.chain.utils.ChatLogUtils;
import cn.com.chat.chat.chain.utils.ImageUtils;
import cn.com.chat.common.core.utils.StringUtils;
import cn.com.chat.common.http.utils.HttpUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import cn.hutool.core.thread.ThreadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 画宇宙图像生成 <br/>
 * url <a href="https://creator.nolipix.com/">https://creator.nolipix.com/</a><br/>
 * API <a href="https://creator-nolibox.apifox.cn/api-56290971">接口文档</a><br/>
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NoLiPixImageChatService implements ImageChatService {

    private final NoLiPixAccessTokenService accessTokenService;

    @Override
    public ImageResult blockGenImage(String prompt) {

        NoLiPixImageParams params = NoLiPixImageParams.builder()
            .model("general")
            .text(prompt)
            .w(512)
            .h(512)
            .sampler("k_euler")
            .build();

        NoLiPixImageRequest request = NoLiPixImageRequest.builder()
            .task("txt2img.sd")
            .params(params)
            .build();

        ChatLogUtils.printRequestLog(this.getClass(), request);

        Map<String, String> header = getHeader();

        String response = HttpUtils.doPostJson(NoLiPixApis.DRAW_URL, request, header);

        ChatLogUtils.printResponseLog(this.getClass(), response);

        NoLiPixImageTask task = JsonUtils.parseObject(response, NoLiPixImageTask.class);

        if (Objects.isNull(task) || Objects.isNull(task.getUid())) {
            throw new ImageChatException(500, "任务生成失败");
        }

        NoLiPixResult object = getResult(Objects.requireNonNull(task).getUid());

        String status = object.getStatus();
        if (StringUtils.equals(status, "not_found")) {
            throw new ImageChatException(500, "不存在的任务");
        } else if (StringUtils.equals(status, "exception")) {
            throw new ImageChatException(500, object.getData().getReason());
        } else if (StringUtils.equals(status, "finished")) {
            List<String> imgUrls = object.getData().getImgUrls();
            List<String> list = imgUrls.stream().map(s -> ImageUtils.urlToUrl(ImageChatType.NOLIBOX.name(), s)).toList();
            object.getData().setImgUrls(list);
        }

        ImageResult result = ImageResult.builder()
            .model(ImageChatType.NOLIBOX.name())
            .version("general")
            .data(object.getData().getImgUrls())
            .totalTokens(2L)
            .response(JsonUtils.toJsonString(object))
            .build();

        ChatLogUtils.printResultLog(this.getClass(), result);

        return result;
    }

    private NoLiPixResult getResult(String taskId) {
        String response = HttpUtils.doGet(StringUtils.format(NoLiPixApis.TASK_URL, taskId), null, getHeader());

        ChatLogUtils.printResponseLog(this.getClass(), response);

        NoLiPixResult result = JsonUtils.parseObject(response, NoLiPixResult.class);

        String status = Objects.requireNonNull(result).getStatus();

        if (StringUtils.equals(status, "pending") || StringUtils.equals(status, "working")) {
            ThreadUtil.safeSleep(1000);
            return getResult(taskId);
        }

        return result;
    }

    private Map<String, String> getHeader() {
        return Map.of("Authorization", "Basic " + accessTokenService.getAccessToken());
    }

}
