package cn.com.chat.chat.chain.auth.volcengine;

import cn.com.chat.chat.chain.auth.TextTokenService;
import cn.com.chat.chat.chain.auth.VisionTokenService;
import cn.com.chat.chat.chain.enums.ImageChatType;
import cn.com.chat.chat.chain.enums.ModelType;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.enums.VisionChatType;
import cn.com.chat.chat.domain.vo.OpenKeyVo;
import cn.com.chat.chat.service.IOpenKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 火山引擎认证服务
 *
 * @author JiaZH
 * @date 2024-06-18
 */
@Component
@RequiredArgsConstructor
public class VolcengineAccessTokenService implements TextTokenService, VisionTokenService {

    private final IOpenKeyService openKeyService;

    @Override
    public String getTextToken() {
        OpenKeyVo openKeyVo = openKeyService.queryByKey(TextChatType.VOLCENGINE.name(), ModelType.TEXT.getCode());
        return openKeyVo.getAppSecret();
    }

    public String getImageAccessKeyId() {
        OpenKeyVo openKeyVo = openKeyService.queryByKey(ImageChatType.VOLCENGINE.name(), ModelType.IMAGE.getCode());
        return openKeyVo.getAppKey();
    }

    public String getImageSecretAccessKey() {
        OpenKeyVo openKeyVo = openKeyService.queryByKey(ImageChatType.VOLCENGINE.name(), ModelType.IMAGE.getCode());
        return openKeyVo.getAppSecret();
    }

    @Override
    public String getVisionToken() {
        OpenKeyVo openKeyVo = openKeyService.queryByKey(VisionChatType.VOLCENGINE.name(), ModelType.VISION.getCode());
        return openKeyVo.getAppSecret();
    }

}
