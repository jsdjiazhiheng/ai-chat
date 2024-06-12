package cn.com.chat.chat.service;

import cn.com.chat.chat.domain.bo.SearchEngineBo;
import cn.com.chat.chat.domain.vo.SearchEngineVo;
import cn.com.chat.common.mybatis.core.page.PageQuery;
import cn.com.chat.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 搜索引擎Service接口
 *
 * @author JiaZH
 * @date 2024-05-07
 */
public interface ISearchEngineService {

    /**
     * 查询搜索引擎
     */
    SearchEngineVo queryById(Long id);

    /**
     * 查询搜索引擎列表
     */
    TableDataInfo<SearchEngineVo> queryPageList(SearchEngineBo bo, PageQuery pageQuery);

    /**
     * 查询搜索引擎列表
     */
    List<SearchEngineVo> queryList(SearchEngineBo bo);

    /**
     * 新增搜索引擎
     */
    Boolean insertByBo(SearchEngineBo bo);

    /**
     * 修改搜索引擎
     */
    Boolean updateByBo(SearchEngineBo bo);

    /**
     * 校验并批量删除搜索引擎信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    String getDefaultSearchEngine();

}
