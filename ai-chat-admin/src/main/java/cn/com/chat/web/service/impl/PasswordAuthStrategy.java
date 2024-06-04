package cn.com.chat.web.service.impl;

import cn.com.chat.web.domain.vo.LoginVo;
import cn.com.chat.web.service.IAuthStrategy;
import cn.com.chat.web.service.SysLoginService;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.com.chat.common.core.constant.Constants;
import cn.com.chat.common.core.constant.GlobalConstants;
import cn.com.chat.common.core.domain.model.LoginUser;
import cn.com.chat.common.core.domain.model.PasswordLoginBody;
import cn.com.chat.common.core.enums.LoginType;
import cn.com.chat.common.core.enums.UserStatus;
import cn.com.chat.common.core.exception.user.CaptchaException;
import cn.com.chat.common.core.exception.user.CaptchaExpireException;
import cn.com.chat.common.core.exception.user.UserException;
import cn.com.chat.common.core.utils.MessageUtils;
import cn.com.chat.common.core.utils.StringUtils;
import cn.com.chat.common.core.utils.ValidatorUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import cn.com.chat.common.redis.utils.RedisUtils;
import cn.com.chat.common.satoken.utils.LoginHelper;
import cn.com.chat.common.tenant.helper.TenantHelper;
import cn.com.chat.common.web.config.properties.CaptchaProperties;
import cn.com.chat.system.domain.SysClient;
import cn.com.chat.system.domain.SysUser;
import cn.com.chat.system.domain.vo.SysUserVo;
import cn.com.chat.system.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

/**
 * 密码认证策略
 *
 * @author Michelle.Chung
 */
@Slf4j
@Service("password" + IAuthStrategy.BASE_NAME)
@RequiredArgsConstructor
public class PasswordAuthStrategy implements IAuthStrategy {

    private final CaptchaProperties captchaProperties;
    private final SysLoginService loginService;
    private final SysUserMapper userMapper;

    @Override
    public LoginVo login(String body, SysClient client) {
        PasswordLoginBody loginBody = JsonUtils.parseObject(body, PasswordLoginBody.class);
        ValidatorUtils.validate(loginBody);
        String tenantId = loginBody.getTenantId();
        String username = loginBody.getUsername();
        String password = loginBody.getPassword();
        String code = loginBody.getCode();
        String uuid = loginBody.getUuid();

        boolean captchaEnabled = captchaProperties.getEnable();
        // 验证码开关
        if (captchaEnabled) {
            validateCaptcha(tenantId, username, code, uuid);
        }

        SysUserVo user = loadUserByUsername(tenantId, username);
        loginService.checkLogin(LoginType.PASSWORD, tenantId, username, () -> !BCrypt.checkpw(password, user.getPassword()));
        // 此处可根据登录用户的数据不同 自行创建 loginUser
        LoginUser loginUser = loginService.buildLoginUser(user);
        loginUser.setClientKey(client.getClientKey());
        loginUser.setDeviceType(client.getDeviceType());
        SaLoginModel model = new SaLoginModel();
        model.setDevice(client.getDeviceType());
        // 自定义分配 不同用户体系 不同 token 授权时间 不设置默认走全局 yml 配置
        // 例如: 后台用户30分钟过期 app用户1天过期
        model.setTimeout(client.getTimeout());
        model.setActiveTimeout(client.getActiveTimeout());
        model.setExtra(LoginHelper.CLIENT_KEY, client.getClientId());
        // 生成token
        LoginHelper.login(loginUser, model);

        LoginVo loginVo = new LoginVo();
        loginVo.setAccessToken(StpUtil.getTokenValue());
        loginVo.setExpireIn(StpUtil.getTokenTimeout());
        loginVo.setClientId(client.getClientId());
        return loginVo;
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    private void validateCaptcha(String tenantId, String username, String code, String uuid) {
        String verifyKey = GlobalConstants.CAPTCHA_CODE_KEY + StringUtils.defaultString(uuid, "");
        String captcha = RedisUtils.getCacheObject(verifyKey);
        RedisUtils.deleteObject(verifyKey);
        if (captcha == null) {
            loginService.recordLogininfor(tenantId, username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            loginService.recordLogininfor(tenantId, username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error"));
            throw new CaptchaException();
        }
    }

    private SysUserVo loadUserByUsername(String tenantId, String username) {
        return TenantHelper.dynamic(tenantId, () -> {
            SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .select(SysUser::getUserName, SysUser::getStatus)
                .eq(SysUser::getUserName, username));
            if (ObjectUtil.isNull(user)) {
                log.info("登录用户：{} 不存在.", username);
                throw new UserException("user.not.exists", username);
            } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
                log.info("登录用户：{} 已被停用.", username);
                throw new UserException("user.blocked", username);
            }
            return userMapper.selectUserByUserName(username);
        });
    }

}
