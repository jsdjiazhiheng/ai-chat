package cn.com.chat.chat.chain.generation.image.spark;

import cn.com.chat.chat.chain.auth.spark.SparkAccessTokenService;
import cn.com.chat.chat.chain.enums.ImageChatType;
import cn.com.chat.chat.chain.enums.model.SparkModelEnums;
import cn.com.chat.chat.chain.exception.ImageChatException;
import cn.com.chat.chat.chain.generation.image.ImageChatService;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.request.spark.SparkRequestHeader;
import cn.com.chat.chat.chain.request.spark.SparkRequestMessage;
import cn.com.chat.chat.chain.request.spark.SparkRequestPayload;
import cn.com.chat.chat.chain.request.spark.image.SparkImageChatParameter;
import cn.com.chat.chat.chain.request.spark.image.SparkImageRequest;
import cn.com.chat.chat.chain.request.spark.image.SparkImageRequestParameter;
import cn.com.chat.chat.chain.response.base.image.ImageResult;
import cn.com.chat.chat.chain.response.spark.image.SparkImageResponse;
import cn.com.chat.chat.chain.utils.ChatLogUtils;
import cn.com.chat.chat.chain.utils.ImageUtils;
import cn.com.chat.common.http.utils.HttpUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 讯飞星火图像生成
 *
 * @author JiaZH
 * @date 2024-06-12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SparkImageChatService implements ImageChatService {

    private final SparkAccessTokenService accessTokenService;

    @Override
    public ImageResult blockGenImage(String prompt) {

        SparkModelEnums enums = SparkModelEnums.SPARK_TTI;

        String authUrl = accessTokenService.getAuthUrl(enums.getUrl(), false);

        SparkRequestHeader header = SparkRequestHeader.builder().appId(accessTokenService.getAppid()).build();
        SparkImageRequestParameter parameter = SparkImageRequestParameter.builder()
            .chat(
                SparkImageChatParameter.builder()
                    .domain(enums.getDomain())
                    .build()
            )
            .build();
        SparkRequestPayload payload = SparkRequestPayload.builder()
            .message(SparkRequestMessage.builder().text(CollUtil.newArrayList(MessageItem.buildUser(prompt))).build())
            .build();

        SparkImageRequest request = SparkImageRequest.builder().header(header).parameter(parameter).payload(payload).build();

        ChatLogUtils.printRequestLog(this.getClass(), request);

        String response = HttpUtils.doPostJson(authUrl, request);

        ChatLogUtils.printResponseLog(this.getClass(), response);

        SparkImageResponse object = JsonUtils.parseObject(response, SparkImageResponse.class);

        Integer code = Objects.requireNonNull(object).getHeader().getCode();

        if (code != 0) {
            throw new ImageChatException(object.getHeader().getMessage());
        }

        List<MessageItem> results = object.getPayload().getChoices().getText();

        List<MessageItem> list = results.stream()
            .peek(item -> item.setContent(ImageUtils.base64ToUrl(ImageChatType.SPARK.name(), item.getContent())))
            .toList();

        object.getPayload().getChoices().setText(list);

        List<String> imageList = list.stream().map(MessageItem::getContent).toList();

        ImageResult result = ImageResult.builder()
            .model(ImageChatType.SPARK.name())
            .version(enums.getModel())
            .data(imageList)
            .totalTokens(6L)
            .response(JsonUtils.toJsonString(object))
            .build();

        ChatLogUtils.printResultLog(this.getClass(), result);

        return result;
    }

}
