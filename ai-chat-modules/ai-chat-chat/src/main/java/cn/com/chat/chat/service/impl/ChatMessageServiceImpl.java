package cn.com.chat.chat.service.impl;

import cn.com.chat.chat.chain.enums.Role;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.response.base.image.ImageResult;
import cn.com.chat.chat.chain.response.base.text.TextResult;
import cn.com.chat.chat.chain.utils.ImageUtils;
import cn.com.chat.chat.domain.ChatMessage;
import cn.com.chat.chat.domain.bo.ChatMessageBo;
import cn.com.chat.chat.domain.vo.ChatMessageVo;
import cn.com.chat.chat.enums.ContentTypeEnums;
import cn.com.chat.chat.mapper.ChatMessageMapper;
import cn.com.chat.chat.service.IChatMessageService;
import cn.com.chat.common.core.utils.MapstructUtils;
import cn.com.chat.common.core.utils.StringUtils;
import cn.com.chat.common.json.utils.JsonUtils;
import cn.com.chat.common.mybatis.core.page.PageQuery;
import cn.com.chat.common.mybatis.core.page.TableDataInfo;
import cn.com.chat.common.satoken.utils.LoginHelper;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 对话消息Service业务层处理
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@RequiredArgsConstructor
@Service
public class ChatMessageServiceImpl implements IChatMessageService {

    private final ChatMessageMapper baseMapper;

    /**
     * 查询对话消息
     */
    @Override
    public ChatMessageVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询对话消息列表
     */
    @Override
    public TableDataInfo<ChatMessageVo> queryPageList(ChatMessageBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ChatMessage> lqw = buildQueryWrapper(bo);
        Page<ChatMessageVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        List<ChatMessageVo> records = result.getRecords();
        for (ChatMessageVo record : records) {
            if(Objects.equals(record.getContentType(), ContentTypeEnums.IMAGE.name()) && Objects.equals(record.getRole(), Role.ASSISTANT.getName())) {
                String images = record.getImages();
                List<String> list = JsonUtils.parseArray(images, String.class);
                record.setImageList(list.stream().map(ImageUtils::getImageUrl).toList());
            }
        }
        result.setRecords(records);
        return TableDataInfo.build(result);
    }

    /**
     * 查询对话消息列表
     */
    @Override
    public List<ChatMessageVo> queryList(ChatMessageBo bo) {
        LambdaQueryWrapper<ChatMessage> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ChatMessage> buildQueryWrapper(ChatMessageBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ChatMessage> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getChatId() != null, ChatMessage::getChatId, bo.getChatId());
        lqw.eq(StringUtils.isNotBlank(bo.getMessageId()), ChatMessage::getMessageId, bo.getMessageId());
        lqw.eq(StringUtils.isNotBlank(bo.getParentMessageId()), ChatMessage::getParentMessageId, bo.getParentMessageId());
        lqw.eq(StringUtils.isNotBlank(bo.getModel()), ChatMessage::getModel, bo.getModel());
        lqw.eq(StringUtils.isNotBlank(bo.getRole()), ChatMessage::getRole, bo.getRole());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), ChatMessage::getContent, bo.getContent());
        lqw.eq(StringUtils.isNotBlank(bo.getContentType()), ChatMessage::getContentType, bo.getContentType());
        return lqw;
    }

    /**
     * 新增对话消息
     */
    @Override
    public Boolean insertByBo(ChatMessageBo bo) {
        ChatMessage add = MapstructUtils.convert(bo, ChatMessage.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改对话消息
     */
    @Override
    public Boolean updateByBo(ChatMessageBo bo) {
        ChatMessage update = MapstructUtils.convert(bo, ChatMessage.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ChatMessage entity) {
        Assert.notBlank(entity.getRole(), "角色不能为空");
        if (Objects.equals(entity.getRole(), Role.ASSISTANT.getName())) {
            Assert.notBlank(entity.getParentMessageId(), "父消息Id不能为空");
        }
        Assert.notBlank(entity.getContentType(), "消息类型不能为空");
        if(Objects.isNull(entity.getId())) {
            entity.setVersion(1L);
        }
        if(Objects.equals(Role.ASSISTANT.getName(), entity.getRole())) {
            if(Objects.equals(ContentTypeEnums.TEXT.name(), entity.getContentType())) {
                baseMapper.delete(new LambdaQueryWrapper<ChatMessage>()
                    .eq(ChatMessage::getParentMessageId, entity.getParentMessageId())
                    .eq(ChatMessage::getContentType, ContentTypeEnums.TEXT.name())
                );
            }
        }
    }

    /**
     * 批量删除对话消息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    private ChatMessageVo getChatMessageVoByParentMessageId(String parentMessageId) {
        return baseMapper.selectVoOne(new LambdaQueryWrapper<ChatMessage>()
            .eq(ChatMessage::getParentMessageId, parentMessageId)
            .eq(ChatMessage::getRole, Role.ASSISTANT.getName())
        );
    }

    @Override
    public TableDataInfo<ChatMessageVo> getMessageList(String chatId, PageQuery pageQuery) {
        IPage<ChatMessageVo> page = baseMapper.selectVoPage(pageQuery.build(), Wrappers.<ChatMessage>lambdaQuery()
            .eq(ChatMessage::getChatId, chatId)
            .eq(ChatMessage::getRole, Role.USER.getName())
            .orderByDesc(ChatMessage::getCreateTime)
        );
        List<ChatMessageVo> records = page.getRecords();
        records = CollUtil.reverse(records);
        List<ChatMessageVo> list = new ArrayList<>();
        for (ChatMessageVo record : records) {
            ChatMessageVo messageVo = getChatMessageVoByParentMessageId(record.getMessageId());
            if (Objects.nonNull(messageVo)) {
                if(ContentTypeEnums.IMAGE.name().equals(messageVo.getContentType())) {
                    List<String> imageList = JsonUtils.parseArray(messageVo.getImages(), String.class);
                    imageList = imageList.stream().map(ImageUtils::getImageUrl).toList();
                    messageVo.setImageList(imageList);
                }
                list.add(record);
                list.add(messageVo);
            }
        }
        return TableDataInfo.build(list);
    }

    @Override
    public List<MessageItem> listChatHistory(Long chatId) {
        IPage<ChatMessageVo> page = baseMapper.selectVoPage(new Page<>(1, 5), Wrappers.<ChatMessage>lambdaQuery()
            .eq(ChatMessage::getChatId, chatId)
            .eq(ChatMessage::getRole, Role.USER.getName())
            .orderByDesc(ChatMessage::getCreateTime)
        );
        if (!page.getRecords().isEmpty()) {
            List<ChatMessageVo> records = page.getRecords();
            records = CollUtil.reverse(records);
            List<MessageItem> list = new ArrayList<>();
            for (ChatMessageVo record : records) {
                ChatMessageVo messageVo = getChatMessageVoByParentMessageId(record.getMessageId());
                if (Objects.nonNull(messageVo)) {
                    list.add(MessageItem.buildUser(record.getContent()));
                    list.add(MessageItem.buildAssistant(messageVo.getContent()));
                }
            }
            return list;
        }
        return null;
    }

    @Override
    public ChatMessageBo insertUserMessage(Long chatId, String contentType, String model, String version, String content, Long status) {
        ChatMessageBo message = ChatMessageBo.builder()
            .chatId(chatId)
            .messageId(UUID.fastUUID().toString())
            .model(model)
            .modelVersion(version)
            .role(Role.USER.getName())
            .content(content)
            .contentType(contentType)
            .status(status)
            .userId(LoginHelper.getUserId())
            .build();
        insertByBo(message);
        return message;
    }

    @Override
    public ChatMessageBo insertAssistantMessage(Long chatId, String messageId, String contentType, TextResult textResult) {
        ChatMessageBo message = ChatMessageBo.builder()
            .chatId(chatId)
            .messageId(UUID.fastUUID().toString())
            .parentMessageId(messageId)
            .model(textResult.getModel())
            .modelVersion(textResult.getVersion())
            .role(Role.ASSISTANT.getName())
            .content(textResult.getContent())
            .contentType(contentType)
            .status(2L)
            .totalTokens(textResult.getTotalTokens())
            .finishReason(textResult.getFinishReason())
            .response(textResult.getResponse())
            .userId(LoginHelper.getUserId())
            .build();
        insertByBo(message);
        return message;
    }

    @Override
    public ChatMessageBo insertAssistantMessage(Long chatId, String messageId, String contentType, ImageResult result) {
        ChatMessageBo message = ChatMessageBo.builder()
            .chatId(chatId)
            .messageId(UUID.fastUUID().toString())
            .parentMessageId(messageId)
            .model(result.getModel())
            .modelVersion(result.getVersion())
            .role(Role.ASSISTANT.getName())
            .images(JsonUtils.toJsonString(result.getData()))
            .contentType(contentType)
            .status(1L)
            .totalTokens(result.getTotalTokens())
            .response(result.getResponse())
            .userId(LoginHelper.getUserId())
            .build();
        insertByBo(message);
        return message;
    }

    @Override
    public ChatMessageVo queryByMessageId(String messageId) {
        return baseMapper.selectVoOne(new LambdaQueryWrapper<ChatMessage>().eq(ChatMessage::getMessageId, messageId));
    }

    @Override
    public void updateStatusByMessageId(String messageId, Long status) {
        baseMapper.update(new LambdaUpdateWrapper<ChatMessage>().eq(ChatMessage::getMessageId, messageId).set(ChatMessage::getStatus, status));
    }

}
