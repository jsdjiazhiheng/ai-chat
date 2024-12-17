package cn.com.chat.chat.chain.auth.zhipu;

import cn.com.chat.chat.chain.auth.ImageTokenService;
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
 * 智谱清言认证Service
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-25
 */
@Component
@RequiredArgsConstructor
public class ZhiPuAccessTokenService implements TextTokenService, ImageTokenService, VisionTokenService {

    private final IOpenKeyService openKeyService;

    @Override
    public String getTextToken() {
        OpenKeyVo openKeyVo = openKeyService.queryByKey(TextChatType.ZHIPU.name(), ModelType.TEXT.getCode());
        return openKeyVo.getAppSecret();
    }

    @Override
    public String getImageToken() {
        OpenKeyVo openKeyVo = openKeyService.queryByKey(ImageChatType.ZHIPU.name(), ModelType.IMAGE.getCode());
        return openKeyVo.getAppSecret();
    }

    @Override
    public String getVisionToken() {
        OpenKeyVo openKeyVo = openKeyService.queryByKey(VisionChatType.ZHIPU.name(), ModelType.VISION.getCode());
        return openKeyVo.getAppSecret();
    }
}
