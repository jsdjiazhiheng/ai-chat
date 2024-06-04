package cn.com.chat.chat.domain.vo;

import cn.com.chat.chat.domain.ChatMessage;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import cn.com.chat.common.excel.annotation.ExcelDictFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


/**
 * 对话消息视图对象 gpt_chat_message
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ChatMessage.class)
public class ChatMessageVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 对话ID
     */
    @ExcelProperty(value = "对话ID")
    private Long chatId;

    /**
     * 消息ID
     */
    @ExcelProperty(value = "消息ID")
    private String messageId;

    /**
     * 回复消息ID
     */
    @ExcelProperty(value = "回复消息ID")
    private String parentMessageId;

    /**
     * 模型
     */
    @ExcelProperty(value = "模型")
    private String model;

    /**
     * 模型版本
     */
    @ExcelProperty(value = "模型版本")
    private String modelVersion;

    /**
     * 角色
     */
    @ExcelProperty(value = "角色")
    private String role;

    /**
     * 文本内容
     */
    @ExcelProperty(value = "文本内容")
    private String content;

    /**
     * 图片内容
     */
    @JsonIgnore
    @ExcelProperty(value = "图片内容")
    private String images;

    /**
     * 图片内容列表
     */
    private List<String> imageList;

    /**
     * 内容类型：text：文字 image : 图片
     */
    @ExcelProperty(value = "内容类型")
    @ExcelDictFormat(readConverterExp = "text=文字,image=图片")
    private String contentType;

    /**
     * 结束原因
     */
    @JsonIgnore
    @ExcelProperty(value = "结束原因")
    private String finishReason;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private Long status;

    /**
     * 使用token
     */
    @JsonIgnore
    @ExcelProperty(value = "使用token")
    private Long totalTokens;


}
