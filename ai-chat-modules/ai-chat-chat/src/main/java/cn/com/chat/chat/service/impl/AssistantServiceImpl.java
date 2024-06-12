package cn.com.chat.chat.service.impl;

import cn.com.chat.chat.domain.Assistant;
import cn.com.chat.chat.domain.bo.AssistantBo;
import cn.com.chat.chat.domain.vo.AssistantVo;
import cn.com.chat.chat.mapper.AssistantMapper;
import cn.com.chat.chat.service.IAssistantService;
import cn.com.chat.common.core.utils.MapstructUtils;
import cn.com.chat.common.core.utils.StringUtils;
import cn.com.chat.common.mybatis.core.page.PageQuery;
import cn.com.chat.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * AI助手Service业务层处理
 *
 * @author JiaZH
 * @date 2024-05-23
 */
@RequiredArgsConstructor
@Service
public class AssistantServiceImpl implements IAssistantService {

    private final AssistantMapper baseMapper;

    /**
     * 查询AI助手
     */
    @Override
    public AssistantVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询AI助手列表
     */
    @Override
    public TableDataInfo<AssistantVo> queryPageList(AssistantBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Assistant> lqw = buildQueryWrapper(bo);
        Page<AssistantVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询AI助手列表
     */
    @Override
    public List<AssistantVo> queryList(AssistantBo bo) {
        LambdaQueryWrapper<Assistant> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Assistant> buildQueryWrapper(AssistantBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Assistant> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), Assistant::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getModel()), Assistant::getModel, bo.getModel());
        return lqw;
    }

    /**
     * 新增AI助手
     */
    @Override
    public Boolean insertByBo(AssistantBo bo) {
        Assistant add = MapstructUtils.convert(bo, Assistant.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改AI助手
     */
    @Override
    public Boolean updateByBo(AssistantBo bo) {
        Assistant update = MapstructUtils.convert(bo, Assistant.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Assistant entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除AI助手
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public String getSystemPromptByModel(String model) {
        Assistant assistant = baseMapper.selectOne(Wrappers.<Assistant>lambdaQuery().eq(Assistant::getModel, model));
        if(Objects.nonNull(assistant)) {
            return assistant.getSystemPrompt();
        }
        return null;
    }
}
