package cn.com.chat.chat.chain.auth.openai;

import cn.com.chat.chat.chain.auth.ImageTokenService;
import cn.com.chat.chat.chain.auth.TextTokenService;
import cn.com.chat.chat.chain.enums.ImageChatType;
import cn.com.chat.chat.chain.enums.ModelType;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.domain.vo.OpenKeyVo;
import cn.com.chat.chat.service.IOpenKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * OpenAI认证Service
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-06-08
 */
@Service
@RequiredArgsConstructor
public class OpenAiAccessTokenService implements TextTokenService, ImageTokenService {

    private final IOpenKeyService openKeyService;

    @Override
    public String getTextToken() {
        OpenKeyVo openKeyVo = openKeyService.queryByKey(TextChatType.OPENAI.name(), ModelType.TEXT.getCode());
        return openKeyVo.getAppSecret();
    }

    @Override
    public String getImageToken() {
        OpenKeyVo openKeyVo = openKeyService.queryByKey(ImageChatType.OPENAI.name(), ModelType.IMAGE.getCode());
        return openKeyVo.getAppSecret();
    }

}
