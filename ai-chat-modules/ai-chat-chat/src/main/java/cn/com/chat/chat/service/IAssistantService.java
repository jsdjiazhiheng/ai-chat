package cn.com.chat.chat.service;

import cn.com.chat.chat.domain.bo.AssistantBo;
import cn.com.chat.chat.domain.vo.AssistantVo;
import cn.com.chat.common.mybatis.core.page.PageQuery;
import cn.com.chat.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * AI助手Service接口
 *
 * @author JiaZH
 * @date 2024-05-23
 */
public interface IAssistantService {

    /**
     * 查询AI助手
     */
    AssistantVo queryById(String id);

    /**
     * 查询AI助手列表
     */
    TableDataInfo<AssistantVo> queryPageList(AssistantBo bo, PageQuery pageQuery);

    /**
     * 查询AI助手列表
     */
    List<AssistantVo> queryList(AssistantBo bo);

    /**
     * 新增AI助手
     */
    Boolean insertByBo(AssistantBo bo);

    /**
     * 修改AI助手
     */
    Boolean updateByBo(AssistantBo bo);

    /**
     * 校验并批量删除AI助手信息
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);

    String getSystemPromptByModel(String model);

}
