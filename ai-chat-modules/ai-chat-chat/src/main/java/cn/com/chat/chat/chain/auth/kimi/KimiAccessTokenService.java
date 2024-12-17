package cn.com.chat.chat.chain.auth.kimi;

import cn.com.chat.chat.chain.auth.TextTokenService;
import cn.com.chat.chat.chain.enums.ModelType;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.domain.vo.OpenKeyVo;
import cn.com.chat.chat.service.IOpenKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Kimi认证Service
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-25
 */
@Component
@RequiredArgsConstructor
public class KimiAccessTokenService implements TextTokenService {

    private final IOpenKeyService openKeyService;

    @Override
    public String getTextToken() {
        OpenKeyVo openKeyVo = openKeyService.queryByKey(TextChatType.KIMI.name(), ModelType.TEXT.getCode());
        return openKeyVo.getAppSecret();
    }

}
