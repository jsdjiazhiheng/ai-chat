package cn.com.chat.chat.service;

import cn.com.chat.chat.domain.bo.ModelBo;
import cn.com.chat.chat.domain.vo.ModelVo;
import cn.com.chat.common.mybatis.core.page.PageQuery;
import cn.com.chat.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 模型信息Service接口
 *
 * @author JiaZH
 * @date 2024-05-07
 */
public interface IModelService {

    /**
     * 查询模型信息
     */
    ModelVo queryById(Long id);

    /**
     * 查询模型信息列表
     */
    TableDataInfo<ModelVo> queryPageList(ModelBo bo, PageQuery pageQuery);

    /**
     * 查询模型信息列表
     */
    List<ModelVo> queryList(ModelBo bo);

    /**
     * 新增模型信息
     */
    Boolean insertByBo(ModelBo bo);

    /**
     * 修改模型信息
     */
    Boolean updateByBo(ModelBo bo);

    /**
     * 校验并批量删除模型信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<ModelVo> getModelList();

}
