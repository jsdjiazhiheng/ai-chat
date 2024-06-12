package cn.com.chat.chat.service.impl;

import cn.com.chat.chat.domain.Chat;
import cn.com.chat.chat.domain.bo.ChatBo;
import cn.com.chat.chat.domain.vo.ChatVo;
import cn.com.chat.chat.enums.ContentTypeEnums;
import cn.com.chat.chat.mapper.ChatMapper;
import cn.com.chat.chat.service.IChatService;
import cn.com.chat.common.core.utils.MapstructUtils;
import cn.com.chat.common.core.utils.StringUtils;
import cn.com.chat.common.mybatis.core.page.PageQuery;
import cn.com.chat.common.mybatis.core.page.TableDataInfo;
import cn.com.chat.common.satoken.utils.LoginHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 对话Service业务层处理
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements IChatService {

    private final ChatMapper baseMapper;

    /**
     * 查询对话
     */
    @Override
    public ChatVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询对话列表
     */
    @Override
    public TableDataInfo<ChatVo> queryPageList(ChatBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Chat> lqw = buildQueryWrapper(bo);
        Page<ChatVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询对话列表
     */
    @Override
    public List<ChatVo> queryList(ChatBo bo) {
        LambdaQueryWrapper<Chat> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Chat> buildQueryWrapper(ChatBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Chat> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), Chat::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getContentType()), Chat::getContentType, bo.getContentType());
        lqw.orderByDesc(Chat::getCreateTime);
        return lqw;
    }

    /**
     * 新增对话
     */
    @Override
    public ChatBo insertByBo(ChatBo bo) {
        Chat add = MapstructUtils.convert(bo, Chat.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            bo.setCreateTime(add.getCreateTime());
        }
        return bo;
    }

    /**
     * 修改对话
     */
    @Override
    public Boolean updateByBo(ChatBo bo) {
        Chat update = MapstructUtils.convert(bo, Chat.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Chat entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除对话
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public ChatVo createChat(String title, ContentTypeEnums contentType) {
        Chat chat = new Chat();
        chat.setTitle(title);
        chat.setContentType(contentType.name());
        chat.setUserId(LoginHelper.getUserId());
        baseMapper.insert(chat);
        return queryById(chat.getId());
    }

    @Override
    public List<ChatVo> getChatList(ContentTypeEnums contentType) {
        return baseMapper.selectVoList(new LambdaQueryWrapper<Chat>()
            .eq(Chat::getUserId, LoginHelper.getUserId())
            .eq(Chat::getContentType, contentType.name())
            .orderByDesc(Chat::getCreateTime)
        );
    }

}
