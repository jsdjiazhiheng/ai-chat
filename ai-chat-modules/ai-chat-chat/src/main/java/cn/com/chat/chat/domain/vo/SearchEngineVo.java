package cn.com.chat.chat.domain.vo;

import cn.com.chat.chat.domain.SearchEngine;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 搜索引擎视图对象 gpt_search_engine
 *
 * @author JiaZH
 * @date 2024-05-07
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = SearchEngine.class)
public class SearchEngineVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String name;

    /**
     * 值
     */
    @ExcelProperty(value = "值")
    private String value;

    /**
     * 是否使用
     */
    @ExcelProperty(value = "是否使用")
    private Long used;


}
