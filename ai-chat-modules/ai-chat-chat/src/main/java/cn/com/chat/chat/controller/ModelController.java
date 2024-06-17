package cn.com.chat.chat.controller;

import cn.com.chat.chat.domain.bo.ModelBo;
import cn.com.chat.chat.domain.vo.ModelVo;
import cn.com.chat.chat.service.IModelService;
import cn.com.chat.common.core.domain.R;
import cn.com.chat.common.core.validate.AddGroup;
import cn.com.chat.common.core.validate.EditGroup;
import cn.com.chat.common.excel.utils.ExcelUtil;
import cn.com.chat.common.idempotent.annotation.RepeatSubmit;
import cn.com.chat.common.log.annotation.Log;
import cn.com.chat.common.log.enums.BusinessType;
import cn.com.chat.common.mybatis.core.page.PageQuery;
import cn.com.chat.common.mybatis.core.page.TableDataInfo;
import cn.com.chat.common.web.core.BaseController;
import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模型信息
 *
 * @author JiaZH
 * @date 2024-05-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/gpt/model")
public class ModelController extends BaseController {

    private final IModelService modelService;

    /**
     * 获取模型列表
     */
    @GetMapping("/getModelList")
    public R<List<ModelVo>> getModelList(String type) {
        return R.ok(modelService.getModelList(type));
    }

    /**
     * 查询模型信息列表
     */
    @SaCheckPermission("gpt:model:list")
    @GetMapping("/list")
    public TableDataInfo<ModelVo> list(ModelBo bo, PageQuery pageQuery) {
        return modelService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出模型信息列表
     */
    @SaCheckPermission("gpt:model:export")
    @Log(title = "模型信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ModelBo bo, HttpServletResponse response) {
        List<ModelVo> list = modelService.queryList(bo);
        ExcelUtil.exportExcel(list, "模型信息", ModelVo.class, response);
    }

    /**
     * 获取模型信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("gpt:model:query")
    @GetMapping("/{id}")
    public R<ModelVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(modelService.queryById(id));
    }

    /**
     * 新增模型信息
     */
    @SaCheckPermission("gpt:model:add")
    @Log(title = "模型信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ModelBo bo) {
        return toAjax(modelService.insertByBo(bo));
    }

    /**
     * 修改模型信息
     */
    @SaCheckPermission("gpt:model:edit")
    @Log(title = "模型信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ModelBo bo) {
        return toAjax(modelService.updateByBo(bo));
    }

    /**
     * 删除模型信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("gpt:model:remove")
    @Log(title = "模型信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(modelService.deleteWithValidByIds(List.of(ids), true));
    }
}
