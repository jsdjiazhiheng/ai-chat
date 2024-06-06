package cn.com.chat.chat.chain.generation.text;

import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.plugins.search.WebSearchEngine;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.response.base.text.TextResult;
import cn.com.chat.chat.chain.utils.MessageUtils;
import cn.com.chat.chat.domain.bo.ChatMessageBo;
import cn.com.chat.chat.service.IChatMessageService;
import cn.com.chat.common.core.exception.ServiceException;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Objects;

/**
 * TODO
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-01
 */
@Slf4j
@Service
@Primary
@AllArgsConstructor
public class TextChatServiceWrapper implements TextChatService {

    private final TextChatServiceFactory textChatServiceFactory;
    private final WebSearchEngine webSearchEngine;
    private final IChatMessageService chatMessageService;

    @Override
    public TextResult blockCompletion(TextChatType textChatType, String system, List<MessageItem> history, String content, Boolean netWork) {
        TextChatService textChatService = textChatServiceFactory.getTextChatService(textChatType);

        if (Objects.isNull(textChatService)) {
            throw new ServiceException("不支持的文本模型服务");
        }

        String model = textChatServiceFactory.getTextChatType(textChatType);
        if (netWork) {
            content = searchContent(content);
        }
        return textChatService.blockCompletion(model, system, history, content);
    }

    @Override
    public void streamCompletion(TextChatType textChatType, SseEmitter sseEmitter, String system, List<MessageItem> history, StreamMessage message) {
        TextChatService textChatService = textChatServiceFactory.getTextChatService(textChatType);
        String model = textChatServiceFactory.getTextChatType(textChatType);

        if (message.getUseNet()) {
            message.setContent(searchContent(message.getContent()));
        }

        textChatService.streamCompletion(model, sseEmitter, system, history, message);
    }

    private String searchContent(String content) {
        try {
            String searchContent = webSearchEngine.search(content);
            if (StrUtil.isNotBlank(searchContent) && !"error".equalsIgnoreCase(searchContent)) {

                /*SummaryComputer computer = new SummaryComputer(500, false, "", searchContent);
                Summary summary = computer.toSummary();

                log.info("内容摘要：{}", summary.getSummary());

                searchContent = summary.getSummary();*/

                content = searchContent + "\n" + "请根据上下文信息：\n\n" + content;
            }
        } catch (Exception e) {
            log.error("发生异常：", e);
            int retry = 0;
            while (retry < 3) {
                try {
                    Thread.sleep(1000);
                    content = searchContent(content);
                    break;
                } catch (Exception e1) {
                    log.error("发生异常：", e);
                    retry++;
                }
            }
        }
        return content;
    }

    @Override
    public void saveSuccessMessage(StreamMessage message, String messageId, TextResult result) {
        ChatMessageBo messageBo = MessageUtils.buildTextChatMessage(message.getChatId(), messageId, message.getMessageId(), result, message.getUserId());
        chatMessageService.insertByBo(messageBo);
        chatMessageService.updateStatusByMessageId(message.getMessageId(), 2);
    }

    @Override
    public void saveFailMessage(StreamMessage message, String messageId, String errorMessage) {
        chatMessageService.updateStatusByMessageId(message.getMessageId(), 3);
    }
}
