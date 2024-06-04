package cn.com.chat.chat.chain.function.service.impl;

import cn.com.chat.chat.chain.response.base.text.TextResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.function.service.ICompletionService;
import cn.com.chat.chat.chain.generation.function.FunctionChatService;
import cn.com.chat.chat.chain.generation.text.TextChatService;
import cn.com.chat.common.core.utils.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 任务Service
 *
 * @author JiaZH
 * @date 2024-05-11
 */
@Service
@Slf4j
@AllArgsConstructor
public class CompletionServiceImpl implements ICompletionService {

    private final TextChatService textChatService;
    private final FunctionChatService functionChatService;


    @Override
    public Boolean functionSearch(String content) {
        String fullContent = "判断以下文本是否需要联网查询，你只能回答1个字：是或者否：" + content;
        TextResult result = textChatService.blockCompletion(TextChatType.KIMI, null, null, fullContent, Boolean.FALSE);
        log.info("判断是否需要联网：{}", result.getContent());
        return result.getContent().contains("是");
    }

    @Override
    public Boolean functionSecurity(String content) {
        String fullContent = "判断以下文本是否包含政治、暴力、色情、毒品、恐怖、歧视等敏感内容，你只能回答2个字：安全或者危险：" + content;
        TextResult result = textChatService.blockCompletion(TextChatType.KIMI, null, null, fullContent, Boolean.FALSE);
        log.info("判断输入文本是否安全：{}", result.getContent());
        return result.getContent().contains("安全");
    }

    @Override
    public String functionDrawPrompt(String content) {
        String system =
            """
            - Role: 高级图像生成顾问
            - Background: 用户希望将不连贯的文本转换成图像，同时需要纠正任何常识性错误。
            - Profile: 你是一位经验丰富的图像生成顾问，能够理解并纠正文本中的常识性错误，同时生成高质量的图像提示词。
            - Skills: 高级文本分析、错误纠正、创意图像生成。
            - Goals: 将用户输入的不连贯文本转换成清晰、准确的图像生成提示词，并纠正任何常识性错误。
            - Constrains: 确保转换后的提示词不仅能够清晰表达原文意图，而且符合常识和逻辑。
            - OutputFormat: 优化后的图像生成提示词文本。
            - Workflow:
              1. 分析用户输入的文本，识别并理解内容。
              2. 检查文本中的常识性错误，并进行纠正。
              3. 提取关键信息并转化为图像生成的提示词。
              4. 返回优化后的图像提示词文本。
            - Examples:
              用户输入文本："星空下的城堡，漂浮在空中的鱼，古老的传说，神秘的宝藏"
              纠正后的文本："星空下的城堡，古老的传说，神秘的宝藏"
              生成的图像提示词："一个星空下的城堡，城堡周围环绕着古老的传说和神秘的宝藏。"
            """;
        String fullContent = "将以下内容按照顺序理解输入词语意思，并保证正确性，不能出现常识性错误，并生成最终结果的文生图提示词，你只能回答最终的文生图提示词结果：" + content;
        TextResult result = textChatService.blockCompletion(TextChatType.KIMI, system, null, fullContent, Boolean.FALSE);
        String resultContent = result.getContent();
        log.info("理解图片输入词，最终结果为：{}", resultContent);
        if(resultContent.contains("：")) {
            resultContent = StringUtils.substringAfterLast(resultContent, "：");
        }
        return resultContent;
    }

}
