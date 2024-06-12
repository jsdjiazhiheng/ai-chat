package cn.com.chat.chat.service.impl;

import cn.com.chat.chat.domain.SearchEngine;
import cn.com.chat.chat.domain.bo.SearchEngineBo;
import cn.com.chat.chat.domain.vo.SearchEngineVo;
import cn.com.chat.chat.mapper.SearchEngineMapper;
import cn.com.chat.chat.service.ISearchEngineService;
import cn.com.chat.common.core.utils.MapstructUtils;
import cn.com.chat.common.core.utils.StringUtils;
import cn.com.chat.common.mybatis.core.page.PageQuery;
import cn.com.chat.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 搜索引擎Service业务层处理
 *
 * @author JiaZH
 * @date 2024-05-07
 */
@RequiredArgsConstructor
@Service
public class SearchEngineServiceImpl implements ISearchEngineService {

    private final SearchEngineMapper baseMapper;

    /**
     * 查询搜索引擎
     */
    @Override
    public SearchEngineVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询搜索引擎列表
     */
    @Override
    public TableDataInfo<SearchEngineVo> queryPageList(SearchEngineBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SearchEngine> lqw = buildQueryWrapper(bo);
        Page<SearchEngineVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询搜索引擎列表
     */
    @Override
    public List<SearchEngineVo> queryList(SearchEngineBo bo) {
        LambdaQueryWrapper<SearchEngine> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SearchEngine> buildQueryWrapper(SearchEngineBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SearchEngine> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), SearchEngine::getName, bo.getName());
        lqw.eq(bo.getDeptId() != null, SearchEngine::getDeptId, bo.getDeptId());
        lqw.eq(bo.getUserId() != null, SearchEngine::getUserId, bo.getUserId());
        return lqw;
    }

    /**
     * 新增搜索引擎
     */
    @Override
    public Boolean insertByBo(SearchEngineBo bo) {
        SearchEngine add = MapstructUtils.convert(bo, SearchEngine.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改搜索引擎
     */
    @Override
    public Boolean updateByBo(SearchEngineBo bo) {
        SearchEngine update = MapstructUtils.convert(bo, SearchEngine.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SearchEngine entity) {
        if (Objects.isNull(entity.getId())) {
            entity.setDelFlag(0L);
        }
        if (entity.getUsed() == 1) {
            baseMapper.update(new LambdaUpdateWrapper<SearchEngine>()
                .set(SearchEngine::getUsed, 0)
                .eq(SearchEngine::getUsed, 1)
            );
        }
    }

    /**
     * 批量删除搜索引擎
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            Long count = baseMapper.selectCount(new LambdaQueryWrapper<SearchEngine>().in(SearchEngine::getId, ids).eq(SearchEngine::getUsed, 1));

            if (count > 0) {
                SearchEngine engine = baseMapper.selectOne(new LambdaQueryWrapper<SearchEngine>()
                    .notIn(SearchEngine::getId, ids)
                    .eq(SearchEngine::getUsed, 0)
                    .orderByDesc(SearchEngine::getId)
                    .last("limit 1")
                );
                engine.setUsed(1L);
                baseMapper.updateById(engine);
            }
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public String getDefaultSearchEngine() {
        SearchEngine engine = baseMapper.selectOne(new LambdaQueryWrapper<SearchEngine>().eq(SearchEngine::getUsed, 1));
        if (Objects.nonNull(engine)) {
            return engine.getValue();
        }
        return "";
    }

}
