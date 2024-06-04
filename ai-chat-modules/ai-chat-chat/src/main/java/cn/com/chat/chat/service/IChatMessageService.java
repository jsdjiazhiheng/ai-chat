package cn.com.chat.chat.service;

import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.response.base.image.ImageResult;
import cn.com.chat.chat.chain.response.base.text.TextResult;
import cn.com.chat.chat.domain.bo.ChatMessageBo;
import cn.com.chat.chat.domain.vo.ChatMessageVo;
import cn.com.chat.common.mybatis.core.page.PageQuery;
import cn.com.chat.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 对话消息Service接口
 *
 * @author JiaZH
 * @date 2024-05-22
 */
public interface IChatMessageService {

    /**
     * 查询对话消息
     */
    ChatMessageVo queryById(Long id);

    /**
     * 查询对话消息列表
     */
    TableDataInfo<ChatMessageVo> queryPageList(ChatMessageBo bo, PageQuery pageQuery);

    /**
     * 查询对话消息列表
     */
    List<ChatMessageVo> queryList(ChatMessageBo bo);

    /**
     * 新增对话消息
     */
    Boolean insertByBo(ChatMessageBo bo);

    /**
     * 修改对话消息
     */
    Boolean updateByBo(ChatMessageBo bo);

    /**
     * 校验并批量删除对话消息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    TableDataInfo<ChatMessageVo> getMessageList(String chatId, PageQuery pageQuery);

    List<MessageItem> listChatHistory(Long chatId);

    ChatMessageBo insertUserMessage(Long chatId, String contentType, String model, String version, String content, Long status);

    ChatMessageBo insertAssistantMessage(Long chatId, String messageId, String contentType, TextResult textResult);

    ChatMessageBo insertAssistantMessage(Long chatId, String messageId, String contentType, ImageResult result);

    ChatMessageVo queryByMessageId(String messageId);

    void updateStatusByMessageId(String messageId, Integer status);

}
