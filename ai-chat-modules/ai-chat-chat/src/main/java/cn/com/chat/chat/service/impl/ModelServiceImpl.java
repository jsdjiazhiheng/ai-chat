package cn.com.chat.chat.service.impl;

import cn.com.chat.chat.service.IModelService;
import cn.hutool.core.util.StrUtil;
import cn.com.chat.chat.chain.utils.ImageUtils;
import cn.com.chat.common.core.service.OssService;
import cn.com.chat.common.core.utils.MapstructUtils;
import cn.com.chat.common.core.utils.StringUtils;
import cn.com.chat.common.mybatis.core.page.TableDataInfo;
import cn.com.chat.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import cn.com.chat.chat.domain.bo.ModelBo;
import cn.com.chat.chat.domain.vo.ModelVo;
import cn.com.chat.chat.domain.Model;
import cn.com.chat.chat.mapper.ModelMapper;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.Objects;

/**
 * 模型信息Service业务层处理
 *
 * @author JiaZH
 * @date 2024-05-07
 */
@RequiredArgsConstructor
@Service
public class ModelServiceImpl implements IModelService {

    private final ModelMapper baseMapper;
    private final OssService ossService;

    /**
     * 查询模型信息
     */
    @Override
    public ModelVo queryById(Long id) {
        ModelVo modelVo = baseMapper.selectVoById(id);
        setVoValue(modelVo);
        return modelVo;
    }

    private void setVoValue(ModelVo modelVo) {
        if (modelVo != null && Objects.nonNull(modelVo.getIcon())) {
            String fileName = ossService.selectFileNameByIds(modelVo.getIcon());
            if(StrUtil.isNotBlank(fileName)) {
                modelVo.setUrl(ImageUtils.getImageUrl(fileName));
            }
        }
    }

    /**
     * 查询模型信息列表
     */
    @Override
    public TableDataInfo<ModelVo> queryPageList(ModelBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Model> lqw = buildQueryWrapper(bo);
        Page<ModelVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        result.getRecords().forEach(this::setVoValue);
        return TableDataInfo.build(result);
    }

    /**
     * 查询模型信息列表
     */
    @Override
    public List<ModelVo> queryList(ModelBo bo) {
        LambdaQueryWrapper<Model> lqw = buildQueryWrapper(bo);
        List<ModelVo> list = baseMapper.selectVoList(lqw);
        list.forEach(this::setVoValue);
        return list;
    }

    private LambdaQueryWrapper<Model> buildQueryWrapper(ModelBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Model> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), Model::getName, bo.getName());
        lqw.eq(bo.getDeptId() != null, Model::getDeptId, bo.getDeptId());
        lqw.eq(bo.getUserId() != null, Model::getUserId, bo.getUserId());
        return lqw;
    }

    /**
     * 新增模型信息
     */
    @Override
    public Boolean insertByBo(ModelBo bo) {
        Model add = MapstructUtils.convert(bo, Model.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改模型信息
     */
    @Override
    public Boolean updateByBo(ModelBo bo) {
        Model update = MapstructUtils.convert(bo, Model.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Model entity) {
        if (Objects.isNull(entity.getId())) {
            entity.setDelFlag(0L);
        }
    }

    /**
     * 批量删除模型信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<ModelVo> getModelList() {
        ModelBo bo = new ModelBo();
        LambdaQueryWrapper<Model> lqw = buildQueryWrapper(bo);
        lqw.orderByAsc(Model::getId);
        List<ModelVo> list = baseMapper.selectVoList(lqw);
        list.forEach(this::setVoValue);
        return list;
    }

}
