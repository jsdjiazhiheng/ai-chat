package cn.com.chat.chat.service;

import cn.com.chat.chat.domain.bo.ChatBo;
import cn.com.chat.chat.domain.vo.ChatVo;
import cn.com.chat.chat.enums.ContentTypeEnums;
import cn.com.chat.common.mybatis.core.page.PageQuery;
import cn.com.chat.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 对话Service接口
 *
 * @author JiaZH
 * @date 2024-05-22
 */
public interface IChatService {

    /**
     * 查询对话
     */
    ChatVo queryById(Long id);

    /**
     * 查询对话列表
     */
    TableDataInfo<ChatVo> queryPageList(ChatBo bo, PageQuery pageQuery);

    /**
     * 查询对话列表
     */
    List<ChatVo> queryList(ChatBo bo);

    /**
     * 新增对话
     */
    ChatBo insertByBo(ChatBo bo);

    /**
     * 修改对话
     */
    Boolean updateByBo(ChatBo bo);

    /**
     * 校验并批量删除对话信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    ChatVo createChat(String title, ContentTypeEnums contentType);

    List<ChatVo> getChatList(ContentTypeEnums contentType);

}
