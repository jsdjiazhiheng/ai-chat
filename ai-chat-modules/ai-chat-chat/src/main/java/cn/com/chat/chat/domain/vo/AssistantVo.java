package cn.com.chat.chat.domain.vo;

import cn.com.chat.chat.domain.Assistant;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * AI助手视图对象 gpt_assistant
 *
 * @author JiaZH
 * @date 2024-05-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Assistant.class)
public class AssistantVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private String id;

    /**
     * 助手名称
     */
    @ExcelProperty(value = "助手名称")
    private String name;

    /**
     * 图标
     */
    @ExcelProperty(value = "图标")
    private String icon;

    /**
     * 所属模型
     */
    @ExcelProperty(value = "所属模型")
    private String model;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String description;

    /**
     * 招呼语
     */
    @ExcelProperty(value = "招呼语")
    private String greet;

    /**
     * 系统提示
     */
    @ExcelProperty(value = "系统提示")
    private String systemPrompt;


}
