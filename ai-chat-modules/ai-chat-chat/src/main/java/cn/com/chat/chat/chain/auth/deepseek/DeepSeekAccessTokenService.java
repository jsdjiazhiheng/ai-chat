package cn.com.chat.chat.chain.auth.deepseek;

import cn.com.chat.chat.chain.auth.TextTokenService;
import cn.com.chat.chat.chain.enums.ModelType;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.domain.vo.OpenKeyVo;
import cn.com.chat.chat.service.IOpenKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * DeepSeek认证Service
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-25
 */
@Component
@RequiredArgsConstructor
public class DeepSeekAccessTokenService implements TextTokenService {

    private final IOpenKeyService openKeyService;

    @Override
    public String getTextToken() {
        OpenKeyVo openKeyVo = openKeyService.queryByKey(TextChatType.DEEPSEEK.name(), ModelType.TEXT.getCode());
        return openKeyVo.getAppSecret();
    }

}
