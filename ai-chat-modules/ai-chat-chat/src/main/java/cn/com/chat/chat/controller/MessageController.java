package cn.com.chat.chat.controller;

import lombok.RequiredArgsConstructor;
import cn.com.chat.chat.chain.enums.ImageChatType;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.domain.vo.ChatMessageVo;
import cn.com.chat.chat.domain.vo.ChatVo;
import cn.com.chat.chat.domain.vo.MessageVO;
import cn.com.chat.chat.enums.ContentTypeEnums;
import cn.com.chat.chat.service.IChatMessageService;
import cn.com.chat.chat.service.IChatService;
import cn.com.chat.chat.service.IMessageService;
import cn.com.chat.common.core.domain.R;
import cn.com.chat.common.mybatis.core.page.PageQuery;
import cn.com.chat.common.mybatis.core.page.TableDataInfo;
import cn.com.chat.common.web.core.BaseController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AI对话
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class MessageController extends BaseController {

    private final IMessageService messageService;
    private final IChatService chatService;
    private final IChatMessageService chatMessageService;

    /**
     * 创建聊天对话
     */
    @PostMapping("/createChat")
    public R<ChatVo> chat(String title, ContentTypeEnums contentType) {
        return R.ok(chatService.createChat(title, contentType));
    }

    /**
     * 获取聊天对话列表
     */
    @GetMapping("/getChatList")
    public R<List<ChatVo>> getChatList(ContentTypeEnums contentType) {
        return R.ok(chatService.getChatList(contentType));
    }

    /**
     * 获取聊天对话消息列表
     */
    @GetMapping("/message/{chatId}")
    public TableDataInfo<ChatMessageVo> getMessageList(@PathVariable String chatId, PageQuery pageQuery) {
        return chatMessageService.getMessageList(chatId, pageQuery);
    }


    /**
     * 发送文本消息
     */
    @PostMapping("/textChat")
    public R<MessageVO> textChat(TextChatType type, Long chatId, String content) {
        return R.ok(messageService.textChat(type, chatId, content));
    }

    /**
     * 发送图片消息
     */
    @PostMapping("/imageChat")
    public R<MessageVO> imageChat(ImageChatType type, Long chatId, String content) {
        return R.ok(messageService.imageChat(type, chatId, content));
    }

}
