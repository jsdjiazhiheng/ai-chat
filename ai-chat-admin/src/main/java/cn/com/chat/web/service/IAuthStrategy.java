package cn.com.chat.web.service;


import cn.com.chat.web.domain.vo.LoginVo;
import cn.com.chat.common.core.exception.ServiceException;
import cn.com.chat.common.core.utils.SpringUtils;
import cn.com.chat.system.domain.SysClient;

/**
 * 授权策略
 *
 * @author Michelle.Chung
 */
public interface IAuthStrategy {

    String BASE_NAME = "AuthStrategy";

    /**
     * 登录
     */
    static LoginVo login(String body, SysClient client, String grantType) {
        // 授权类型和客户端id
        String beanName = grantType + BASE_NAME;
        if (!SpringUtils.containsBean(beanName)) {
            throw new ServiceException("授权类型不正确!");
        }
        IAuthStrategy instance = SpringUtils.getBean(beanName);
        return instance.login(body, client);
    }

    /**
     * 登录
     */
    LoginVo login(String body, SysClient client);

}
