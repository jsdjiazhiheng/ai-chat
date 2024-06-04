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
import cn.com.chat.chat.domain.vo.ChatVo;
import cn.com.chat.chat.domain.bo.ChatBo;
import cn.com.chat.chat.service.IChatService;
import cn.com.chat.common.mybatis.core.page.TableDataInfo;

/**
 * 对话
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/gpt/chat")
public class ChatController extends BaseController {

    private final IChatService chatService;

    /**
     * 查询对话列表
     */
    @SaCheckPermission("chat:chat:list")
    @GetMapping("/list")
    public TableDataInfo<ChatVo> list(ChatBo bo, PageQuery pageQuery) {
        return chatService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出对话列表
     */
    @SaCheckPermission("chat:chat:export")
    @Log(title = "对话", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ChatBo bo, HttpServletResponse response) {
        List<ChatVo> list = chatService.queryList(bo);
        ExcelUtil.exportExcel(list, "对话", ChatVo.class, response);
    }

    /**
     * 获取对话详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("chat:chat:query")
    @GetMapping("/{id}")
    public R<ChatVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(chatService.queryById(id));
    }

    /**
     * 新增对话
     */
    @SaCheckPermission("chat:chat:add")
    @Log(title = "对话", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<ChatBo> add(@Validated(AddGroup.class) @RequestBody ChatBo bo) {
        return R.ok(chatService.insertByBo(bo));
    }

    /**
     * 修改对话
     */
    @SaCheckPermission("chat:chat:edit")
    @Log(title = "对话", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ChatBo bo) {
        return toAjax(chatService.updateByBo(bo));
    }

    /**
     * 删除对话
     *
     * @param ids 主键串
     */
    @SaCheckPermission("chat:chat:remove")
    @Log(title = "对话", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(chatService.deleteWithValidByIds(List.of(ids), true));
    }
}
