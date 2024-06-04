package cn.com.chat.chat.service;

import cn.com.chat.chat.domain.bo.SseChatBo;
import cn.com.chat.chat.domain.vo.ChatMessageVo;
import cn.com.chat.common.core.domain.R;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-05-06
 */
public interface ISseService {

    R<Void> textStreamChat(String sessionId);

    SseEmitter subscribe(String sessionId);

    void unSubscribe(String sessionId);

    ChatMessageVo textChat(SseChatBo bo);

    ChatMessageVo imageChat(SseChatBo bo);

}
