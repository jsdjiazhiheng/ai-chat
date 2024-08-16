package cn.com.chat.chat.service.impl;

import cn.com.chat.chat.chain.enums.ImageChatType;
import cn.com.chat.chat.chain.enums.MessageStatus;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.function.service.ICompletionService;
import cn.com.chat.chat.chain.generation.image.ImageChatService;
import cn.com.chat.chat.chain.generation.text.TextChatService;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.request.base.text.StreamMessage;
import cn.com.chat.chat.chain.response.base.image.ImageResult;
import cn.com.chat.chat.chain.utils.MessageUtils;
import cn.com.chat.chat.domain.bo.ChatMessageBo;
import cn.com.chat.chat.domain.bo.SseChatBo;
import cn.com.chat.chat.domain.vo.ChatMessageVo;
import cn.com.chat.chat.enums.ContentTypeEnums;
import cn.com.chat.chat.service.IAssistantService;
import cn.com.chat.chat.service.IChatMessageService;
import cn.com.chat.chat.service.ISseService;
import cn.com.chat.common.core.domain.R;
import cn.com.chat.common.satoken.utils.LoginHelper;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-05-06
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SseServiceImpl implements ISseService {

    private static final Map<String, SseEmitter> SSE_CACHE = new ConcurrentHashMap<>();

    private final TextChatService textChatService;
    private final ImageChatService imageChatService;
    private final ICompletionService completionService;
    private final IChatMessageService chatMessageService;
    private final IAssistantService assistantService;

    @Override
    public R<Void> textStreamChat(String sessionId) {
        Assert.notBlank(sessionId, "会话ID不能为空");

        SseEmitter sseEmitter = SSE_CACHE.get(sessionId);

        log.info("消息ID为 ： {}, 消息监听是否为空：{}", sessionId, sseEmitter != null);

        if (sseEmitter != null) {

            ChatMessageVo messageVo = chatMessageService.queryByMessageId(sessionId);

            String contentType = messageVo.getContentType();
            String model = messageVo.getModel();
            String content = messageVo.getContent();

            if (ContentTypeEnums.TEXT.name().equals(contentType)) {
                TextChatType type = TextChatType.getByName(model);

                String system = assistantService.getSystemPromptByModel(model);

                List<MessageItem> history = chatMessageService.listChatHistory(messageVo.getChatId());

                StreamMessage message = StreamMessage.builder()
                    .chatId(messageVo.getChatId())
                    .messageId(messageVo.getMessageId())
                    .content(messageVo.getContent())
                    .userId(LoginHelper.getUserId())
                    .tenantId(LoginHelper.getTenantId())
                    .deptId(LoginHelper.getDeptId())
                    .build();

                //boolean useNet = completionService.functionSearch(content);
                boolean useNet = false;

                message.setUseNet(useNet);

                textChatService.streamCompletion(type, sseEmitter, system, history, message);

            } else {

                ImageChatType type = ImageChatType.getByName(model);

                ImageResult result = imageChatService.blockGenImage(type, content);

                String messageId = UUID.fastUUID().toString();

                ChatMessageVo message = MessageUtils.buildImageMessage(messageVo.getChatId(), messageId, messageVo.getMessageId(), result);

                ChatMessageBo messageBo = MessageUtils.buildImageChatMessage(messageVo.getChatId(), messageId, messageVo.getMessageId(), result, LoginHelper.getUserId());
                chatMessageService.insertByBo(messageBo);

                try {
                    sseEmitter.send(message);
                    sseEmitter.send("[END]");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return R.ok();
    }

    @Override
    public SseEmitter subscribe(String sessionId) {
        SseEmitter sseEmitter = new SseEmitter(3600 * 1000L);
        SSE_CACHE.put(sessionId, sseEmitter);
        log.info("add {}", sessionId);
        sseEmitter.onTimeout(() -> {
            log.info("{}超时", sessionId);
            SSE_CACHE.remove(sessionId);
        });
        sseEmitter.onCompletion(() -> log.info("完成！！！"));
        try {
            sseEmitter.send("[START]");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sseEmitter;
    }

    @Override
    public void unSubscribe(String sessionId) {
        SseEmitter sseEmitter = SSE_CACHE.get(sessionId);
        if (sseEmitter != null) {
            sseEmitter.complete();
            log.info("删除 {}", sessionId);
            SSE_CACHE.remove(sessionId);
        }
    }

    @Override
    public ChatMessageVo textChat(SseChatBo bo) {
        ChatMessageBo message = chatMessageService.insertUserMessage(bo.getChatId(), ContentTypeEnums.TEXT.name(), bo.getType(), "", bo.getContent(), null, MessageStatus.WAIT.getStatus());
        return chatMessageService.queryById(message.getId());
    }

    @Override
    public ChatMessageVo imageChat(SseChatBo bo) {
        ChatMessageBo message = chatMessageService.insertUserMessage(bo.getChatId(), ContentTypeEnums.IMAGE.name(), bo.getType(), "", bo.getContent(), null, MessageStatus.WAIT.getStatus());
        return chatMessageService.queryById(message.getId());
    }

}
