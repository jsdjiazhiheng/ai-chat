package cn.com.chat.web.controller;

import cn.com.chat.common.core.config.AiChatConfig;
import cn.com.chat.common.core.utils.StringUtils;
import cn.dev33.satoken.annotation.SaIgnore;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页
 *
 * @author Lion Li
 */
@SaIgnore
@RequiredArgsConstructor
@RestController
public class IndexController {

    /**
     * 系统基础配置
     */
    private final AiChatConfig aiChatConfig;


    /**
     * 访问首页，提示语
     */
    @GetMapping("/")
    public String index() {
        return StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", aiChatConfig.getName(), aiChatConfig.getVersion());
    }

}
