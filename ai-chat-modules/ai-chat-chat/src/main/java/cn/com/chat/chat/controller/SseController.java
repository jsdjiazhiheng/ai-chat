package cn.com.chat.chat.controller;

import cn.com.chat.chat.domain.bo.SseChatBo;
import cn.com.chat.chat.domain.vo.ChatMessageVo;
import cn.com.chat.chat.service.ISseService;
import cn.com.chat.common.core.domain.R;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * AI对话流式处理接口
 *
 * @author JiaZH
 * @date 2024-05-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/sse")
public class SseController {

    private final ISseService sseService;

    /**
     * 发送文本消息
     */
    @PostMapping("/textChat")
    public R<ChatMessageVo> textChat(@RequestBody SseChatBo bo) {
        return R.ok(sseService.textChat(bo));
    }

    /**
     * 发送图片消息
     */
    @PostMapping("/imageChat")
    public R<ChatMessageVo> imageChat(@RequestBody SseChatBo bo) {
        return R.ok(sseService.imageChat(bo));
    }

    /**
     * 文本流式对话
     */
    @PostMapping("/textStreamChat")
    public R<Void> textStreamChat(String sessionId) {
        return sseService.textStreamChat(sessionId);
    }

    /**
     * 订阅消息监听
     */
    @GetMapping(value = "subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(String sessionId) {
        return sseService.subscribe(sessionId);
    }

    /**
     * 取消订阅消息监听
     */
    @GetMapping("unSubscribe")
    public R<Void> unSubscribe(String sessionId) {
        sseService.unSubscribe(sessionId);
        return R.ok();
    }

}
