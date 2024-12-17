package cn.com.chat.chat.service;

import cn.com.chat.chat.domain.OpenKey;
import cn.com.chat.chat.domain.vo.OpenKeyVo;
import cn.com.chat.chat.domain.bo.OpenKeyBo;
import cn.com.chat.common.mybatis.core.page.TableDataInfo;
import cn.com.chat.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 模型配置Service接口
 *
 * @author JiaZH
 * @date 2024-12-12
 */
public interface IOpenKeyService {

    OpenKeyVo queryByKey(String key, Integer type);

    /**
     * 查询模型配置
     */
    OpenKeyVo queryById(Long id);

    /**
     * 查询模型配置列表
     */
    TableDataInfo<OpenKeyVo> queryPageList(OpenKeyBo bo, PageQuery pageQuery);

    /**
     * 查询模型配置列表
     */
    List<OpenKeyVo> queryList(OpenKeyBo bo);

    /**
     * 新增模型配置
     */
    Boolean insertByBo(OpenKeyBo bo);

    /**
     * 修改模型配置
     */
    Boolean updateByBo(OpenKeyBo bo);

    /**
     * 校验并批量删除模型配置信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
