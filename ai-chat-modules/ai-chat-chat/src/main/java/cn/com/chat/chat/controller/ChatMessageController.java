package cn.com.chat.chat.controller;

import cn.com.chat.chat.domain.bo.ChatMessageBo;
import cn.com.chat.chat.domain.vo.ChatMessageVo;
import cn.com.chat.chat.service.IChatMessageService;
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
 * 对话消息
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/gpt/chat/message")
public class ChatMessageController extends BaseController {

    private final IChatMessageService chatMessageService;

    /**
     * 查询对话消息列表
     */
    @SaCheckPermission("chat:chatMessage:list")
    @GetMapping("/list")
    public TableDataInfo<ChatMessageVo> list(ChatMessageBo bo, PageQuery pageQuery) {
        return chatMessageService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出对话消息列表
     */
    @SaCheckPermission("chat:chatMessage:export")
    @Log(title = "对话消息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ChatMessageBo bo, HttpServletResponse response) {
        List<ChatMessageVo> list = chatMessageService.queryList(bo);
        ExcelUtil.exportExcel(list, "对话消息", ChatMessageVo.class, response);
    }

    /**
     * 获取对话消息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("chat:chatMessage:query")
    @GetMapping("/{id}")
    public R<ChatMessageVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(chatMessageService.queryById(id));
    }

    /**
     * 新增对话消息
     */
    @SaCheckPermission("chat:chatMessage:add")
    @Log(title = "对话消息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ChatMessageBo bo) {
        return toAjax(chatMessageService.insertByBo(bo));
    }

    /**
     * 修改对话消息
     */
    @SaCheckPermission("chat:chatMessage:edit")
    @Log(title = "对话消息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ChatMessageBo bo) {
        return toAjax(chatMessageService.updateByBo(bo));
    }

    /**
     * 删除对话消息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("chat:chatMessage:remove")
    @Log(title = "对话消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(chatMessageService.deleteWithValidByIds(List.of(ids), true));
    }
}
