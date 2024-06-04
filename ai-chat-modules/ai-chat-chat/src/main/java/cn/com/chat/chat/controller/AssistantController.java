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
import cn.com.chat.chat.domain.vo.AssistantVo;
import cn.com.chat.chat.domain.bo.AssistantBo;
import cn.com.chat.chat.service.IAssistantService;
import cn.com.chat.common.mybatis.core.page.TableDataInfo;

/**
 * AI助手
 *
 * @author JiaZH
 * @date 2024-05-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/gpt/assistant")
public class AssistantController extends BaseController {

    private final IAssistantService assistantService;

    /**
     * 查询AI助手列表
     */
    @SaCheckPermission("gpt:assistant:list")
    @GetMapping("/list")
    public TableDataInfo<AssistantVo> list(AssistantBo bo, PageQuery pageQuery) {
        return assistantService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出AI助手列表
     */
    @SaCheckPermission("gpt:assistant:export")
    @Log(title = "AI助手", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AssistantBo bo, HttpServletResponse response) {
        List<AssistantVo> list = assistantService.queryList(bo);
        ExcelUtil.exportExcel(list, "AI助手", AssistantVo.class, response);
    }

    /**
     * 获取AI助手详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("gpt:assistant:query")
    @GetMapping("/{id}")
    public R<AssistantVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(assistantService.queryById(id));
    }

    /**
     * 新增AI助手
     */
    @SaCheckPermission("gpt:assistant:add")
    @Log(title = "AI助手", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AssistantBo bo) {
        return toAjax(assistantService.insertByBo(bo));
    }

    /**
     * 修改AI助手
     */
    @SaCheckPermission("gpt:assistant:edit")
    @Log(title = "AI助手", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AssistantBo bo) {
        return toAjax(assistantService.updateByBo(bo));
    }

    /**
     * 删除AI助手
     *
     * @param ids 主键串
     */
    @SaCheckPermission("gpt:assistant:remove")
    @Log(title = "AI助手", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(assistantService.deleteWithValidByIds(List.of(ids), true));
    }
}
