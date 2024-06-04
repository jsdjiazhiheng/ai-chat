package cn.com.chat.chat.domain.vo;

import cn.com.chat.chat.domain.Chat;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * 对话视图对象 gpt_chat
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Chat.class)
public class ChatVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 内容类型：text：文字 image : 图片
     */
    @ExcelProperty(value = "内容类型：text：文字 image : 图片")
    private String contentType;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8", locale = "zh")
    private Date createTime;

}
