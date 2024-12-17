package cn.com.chat.chat.chain.auth.nolipix;

import cn.com.chat.chat.chain.auth.ImageTokenService;
import cn.com.chat.chat.chain.auth.TextTokenService;
import cn.com.chat.chat.chain.enums.ImageChatType;
import cn.com.chat.chat.chain.enums.ModelType;
import cn.com.chat.chat.domain.vo.OpenKeyVo;
import cn.com.chat.chat.service.IOpenKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 画宇宙认证服务
 *
 * @author JiaZH
 * @date 2024-06-13
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NoLiPixAccessTokenService implements ImageTokenService {

    private final IOpenKeyService openKeyService;

    @Override
    public String getImageToken() {
        OpenKeyVo openKeyVo = openKeyService.queryByKey(ImageChatType.NOLIBOX.name(), ModelType.IMAGE.getCode());
        return openKeyVo.getAppSecret();
    }

}
