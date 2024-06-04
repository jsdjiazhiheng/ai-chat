package cn.com.chat.web.service.impl;

import cn.com.chat.web.domain.vo.LoginVo;
import cn.com.chat.web.service.IAuthStrategy;
import cn.com.chat.web.service.SysLoginService;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.com.chat.common.core.constant.Constants;
import cn.com.chat.common.core.constant.GlobalConstants;
import cn.com.chat.common.core.domain.model.LoginUser;
import cn.com.chat.common.core.domain.model.SmsLoginBody;
import cn.com.chat.common.core.enums.LoginType;
import cn.com.chat.common.core.enums.UserStatus;
import cn.com.chat.common.core.exception.user.CaptchaExpireException;
import cn.com.chat.common.core.exception.user.UserException;
import cn.com.chat.common.core.utils.MessageUtils;
import cn.com.chat.common.core.utils.StringUtils;
import cn.com.chat.common.core.utils.ValidatorUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import cn.com.chat.common.redis.utils.RedisUtils;
import cn.com.chat.common.satoken.utils.LoginHelper;
import cn.com.chat.common.tenant.helper.TenantHelper;
import cn.com.chat.system.domain.SysClient;
import cn.com.chat.system.domain.SysUser;
import cn.com.chat.system.domain.vo.SysUserVo;
import cn.com.chat.system.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

/**
 * 短信认证策略
 *
 * @author Michelle.Chung
 */
@Slf4j
@Service("sms" + IAuthStrategy.BASE_NAME)
@RequiredArgsConstructor
public class SmsAuthStrategy implements IAuthStrategy {

    private final SysLoginService loginService;
    private final SysUserMapper userMapper;

    @Override
    public LoginVo login(String body, SysClient client) {
        SmsLoginBody loginBody = JsonUtils.parseObject(body, SmsLoginBody.class);
        ValidatorUtils.validate(loginBody);
        String tenantId = loginBody.getTenantId();
        String phonenumber = loginBody.getPhonenumber();
        String smsCode = loginBody.getSmsCode();

        // 通过手机号查找用户
        SysUserVo user = loadUserByPhonenumber(tenantId, phonenumber);

        loginService.checkLogin(LoginType.SMS, tenantId, user.getUserName(), () -> !validateSmsCode(tenantId, phonenumber, smsCode));
        // 此处可根据登录用户的数据不同 自行创建 loginUser 属性不够用继承扩展就行了
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
     * 校验短信验证码
     */
    private boolean validateSmsCode(String tenantId, String phonenumber, String smsCode) {
        String code = RedisUtils.getCacheObject(GlobalConstants.CAPTCHA_CODE_KEY + phonenumber);
        if (StringUtils.isBlank(code)) {
            loginService.recordLogininfor(tenantId, phonenumber, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"));
            throw new CaptchaExpireException();
        }
        return code.equals(smsCode);
    }

    private SysUserVo loadUserByPhonenumber(String tenantId, String phonenumber) {
        return TenantHelper.dynamic(tenantId, () -> {
            SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .select(SysUser::getPhonenumber, SysUser::getStatus)
                .eq(SysUser::getPhonenumber, phonenumber));
            if (ObjectUtil.isNull(user)) {
                log.info("登录用户：{} 不存在.", phonenumber);
                throw new UserException("user.not.exists", phonenumber);
            } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
                log.info("登录用户：{} 已被停用.", phonenumber);
                throw new UserException("user.blocked", phonenumber);
            }
            return userMapper.selectUserByPhonenumber(phonenumber);
        });
    }

}
