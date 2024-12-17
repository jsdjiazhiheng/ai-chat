package cn.com.chat.chat.domain.vo;

import cn.com.chat.chat.domain.OpenKey;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import cn.com.chat.common.excel.annotation.ExcelDictFormat;
import cn.com.chat.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 模型配置视图对象 gpt_open_key
 *
 * @author JiaZH
 * @date 2024-12-12
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = OpenKey.class)
public class OpenKeyVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 模型类型（1-文本，2-图像，3-视觉，4-语音）
     */
    @ExcelProperty(value = "模型类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=-文本，2-图像，3-视觉，4-语音")
    private Long type;

    /**
     * 模型值
     */
    @ExcelProperty(value = "模型值")
    private String value;

    /**
     * 模型名称
     */
    @ExcelProperty(value = "模型名称")
    private String name;

    /**
     * AppID
     */
    @ExcelProperty(value = "AppID")
    private String appId;

    /**
     * AppKey
     */
    @ExcelProperty(value = "AppKey")
    private String appKey;

    /**
     * App密钥
     */
    @ExcelProperty(value = "App密钥")
    private String appSecret;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Long sort;


}
