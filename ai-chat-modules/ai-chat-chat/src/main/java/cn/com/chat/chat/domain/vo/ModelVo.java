package cn.com.chat.chat.domain.vo;

import cn.com.chat.chat.domain.Model;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 模型信息视图对象 gpt_model
 *
 * @author JiaZH
 * @date 2024-05-07
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Model.class)
public class ModelVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 模型类型
     */
    private Integer type;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String name;

    /**
     * 图标
     */
    @ExcelProperty(value = "图标")
    private Long icon;

    /**
     * 模型值
     */
    @ExcelProperty(value = "模型值")
    private String value;

    /**
     * 图标Url
     */
    private String url;


}
