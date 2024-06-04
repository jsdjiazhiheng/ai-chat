package cn.com.chat.common.translation.core.impl;

import lombok.AllArgsConstructor;
import cn.com.chat.common.core.service.UserService;
import cn.com.chat.common.translation.annotation.TranslationType;
import cn.com.chat.common.translation.constant.TransConstant;
import cn.com.chat.common.translation.core.TranslationInterface;

/**
 * 用户名称翻译实现
 *
 * @author may
 */
@AllArgsConstructor
@TranslationType(type = TransConstant.USER_ID_TO_NICKNAME)
public class NicknameTranslationImpl implements TranslationInterface<String> {

    private final UserService userService;

    @Override
    public String translation(Object key, String other) {
        if (key instanceof Long id) {
            return userService.selectNicknameById(id);
        }
        return null;
    }
}
