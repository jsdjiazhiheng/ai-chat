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
import cn.com.chat.chat.domain.vo.SearchEngineVo;
import cn.com.chat.chat.domain.bo.SearchEngineBo;
import cn.com.chat.chat.service.ISearchEngineService;
import cn.com.chat.common.mybatis.core.page.TableDataInfo;

/**
 * 搜索引擎
 *
 * @author JiaZH
 * @date 2024-05-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/gpt/search/engine")
public class SearchEngineController extends BaseController {

    private final ISearchEngineService searchEngineService;

    /**
     * 查询搜索引擎列表
     */
    @SaCheckPermission("gpt:searchEngine:list")
    @GetMapping("/list")
    public TableDataInfo<SearchEngineVo> list(SearchEngineBo bo, PageQuery pageQuery) {
        return searchEngineService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出搜索引擎列表
     */
    @SaCheckPermission("gpt:searchEngine:export")
    @Log(title = "搜索引擎", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SearchEngineBo bo, HttpServletResponse response) {
        List<SearchEngineVo> list = searchEngineService.queryList(bo);
        ExcelUtil.exportExcel(list, "搜索引擎", SearchEngineVo.class, response);
    }

    /**
     * 获取搜索引擎详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("gpt:searchEngine:query")
    @GetMapping("/{id}")
    public R<SearchEngineVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(searchEngineService.queryById(id));
    }

    /**
     * 新增搜索引擎
     */
    @SaCheckPermission("gpt:searchEngine:add")
    @Log(title = "搜索引擎", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SearchEngineBo bo) {
        return toAjax(searchEngineService.insertByBo(bo));
    }

    /**
     * 修改搜索引擎
     */
    @SaCheckPermission("gpt:searchEngine:edit")
    @Log(title = "搜索引擎", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SearchEngineBo bo) {
        return toAjax(searchEngineService.updateByBo(bo));
    }

    /**
     * 删除搜索引擎
     *
     * @param ids 主键串
     */
    @SaCheckPermission("gpt:searchEngine:remove")
    @Log(title = "搜索引擎", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(searchEngineService.deleteWithValidByIds(List.of(ids), true));
    }
}
