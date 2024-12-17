package cn.com.chat.chat.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import cn.com.chat.common.idempotent.annotation.RepeatSubmit;
import cn.com.chat.common.log.annotation.Log;
import cn.com.chat.common.web.core.BaseController;
import cn.com.chat.common.mybatis.core.page.PageQuery;
import cn.com.chat.common.core.domain.R;
import cn.com.chat.common.core.validate.AddGroup;
import cn.com.chat.common.core.validate.EditGroup;
import cn.com.chat.common.log.enums.BusinessType;
import cn.com.chat.common.excel.utils.ExcelUtil;
import cn.com.chat.chat.domain.vo.OpenKeyVo;
import cn.com.chat.chat.domain.bo.OpenKeyBo;
import cn.com.chat.chat.service.IOpenKeyService;
import cn.com.chat.common.mybatis.core.page.TableDataInfo;

/**
 * 模型配置
 *
 * @author JiaZH
 * @date 2024-12-12
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/gpt/open/key")
public class OpenKeyController extends BaseController {

    private final IOpenKeyService openKeyService;

    /**
     * 查询模型配置列表
     */
    @SaCheckPermission("gpt:openKey:list")
    @GetMapping("/list")
    public TableDataInfo<OpenKeyVo> list(OpenKeyBo bo, PageQuery pageQuery) {
        return openKeyService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出模型配置列表
     */
    @SaCheckPermission("gpt:openKey:export")
    @Log(title = "模型配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(OpenKeyBo bo, HttpServletResponse response) {
        List<OpenKeyVo> list = openKeyService.queryList(bo);
        ExcelUtil.exportExcel(list, "模型配置", OpenKeyVo.class, response);
    }

    /**
     * 获取模型配置详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("gpt:openKey:query")
    @GetMapping("/{id}")
    public R<OpenKeyVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(openKeyService.queryById(id));
    }

    /**
     * 新增模型配置
     */
    @SaCheckPermission("gpt:openKey:add")
    @Log(title = "模型配置", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody OpenKeyBo bo) {
        return toAjax(openKeyService.insertByBo(bo));
    }

    /**
     * 修改模型配置
     */
    @SaCheckPermission("gpt:openKey:edit")
    @Log(title = "模型配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody OpenKeyBo bo) {
        return toAjax(openKeyService.updateByBo(bo));
    }

    /**
     * 删除模型配置
     *
     * @param ids 主键串
     */
    @SaCheckPermission("gpt:openKey:remove")
    @Log(title = "模型配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(openKeyService.deleteWithValidByIds(List.of(ids), true));
    }
}
