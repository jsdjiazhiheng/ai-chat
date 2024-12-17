package cn.com.chat.chat.service.impl;

import cn.com.chat.common.core.utils.MapstructUtils;
import cn.com.chat.common.core.utils.StringUtils;
import cn.com.chat.common.mybatis.core.page.TableDataInfo;
import cn.com.chat.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import cn.com.chat.chat.domain.bo.OpenKeyBo;
import cn.com.chat.chat.domain.vo.OpenKeyVo;
import cn.com.chat.chat.domain.OpenKey;
import cn.com.chat.chat.mapper.OpenKeyMapper;
import cn.com.chat.chat.service.IOpenKeyService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 模型配置Service业务层处理
 *
 * @author JiaZH
 * @date 2024-12-12
 */
@RequiredArgsConstructor
@Service
public class OpenKeyServiceImpl implements IOpenKeyService {

    private final OpenKeyMapper baseMapper;

    @Cacheable(cacheNames = "openKey", key = "#key + '-' + #type", unless = "#result == null")
    @Override
    public OpenKeyVo queryByKey(String key, Integer type) {
        return baseMapper.selectVoOne(new LambdaQueryWrapper<OpenKey>()
            .eq(OpenKey::getValue, key)
            .eq(OpenKey::getType, type)
        );
    }

    /**
     * 查询模型配置
     */
    @Override
    public OpenKeyVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询模型配置列表
     */
    @Override
    public TableDataInfo<OpenKeyVo> queryPageList(OpenKeyBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OpenKey> lqw = buildQueryWrapper(bo);
        Page<OpenKeyVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询模型配置列表
     */
    @Override
    public List<OpenKeyVo> queryList(OpenKeyBo bo) {
        LambdaQueryWrapper<OpenKey> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<OpenKey> buildQueryWrapper(OpenKeyBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<OpenKey> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getType() != null, OpenKey::getType, bo.getType());
        lqw.eq(StringUtils.isNotBlank(bo.getValue()), OpenKey::getValue, bo.getValue());
        lqw.like(StringUtils.isNotBlank(bo.getName()), OpenKey::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getAppId()), OpenKey::getAppId, bo.getAppId());
        lqw.eq(StringUtils.isNotBlank(bo.getAppKey()), OpenKey::getAppKey, bo.getAppKey());
        lqw.eq(StringUtils.isNotBlank(bo.getAppSecret()), OpenKey::getAppSecret, bo.getAppSecret());
        lqw.eq(bo.getSort() != null, OpenKey::getSort, bo.getSort());
        lqw.orderByAsc(OpenKey::getSort);
        return lqw;
    }

    /**
     * 新增模型配置
     */
    @Override
    public Boolean insertByBo(OpenKeyBo bo) {
        OpenKey add = MapstructUtils.convert(bo, OpenKey.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改模型配置
     */
    @Override
    public Boolean updateByBo(OpenKeyBo bo) {
        OpenKey update = MapstructUtils.convert(bo, OpenKey.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(OpenKey entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除模型配置
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
