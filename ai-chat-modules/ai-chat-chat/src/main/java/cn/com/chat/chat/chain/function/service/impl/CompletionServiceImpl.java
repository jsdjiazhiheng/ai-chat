package cn.com.chat.chat.chain.function.service.impl;

import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.function.service.ICompletionService;
import cn.com.chat.chat.chain.generation.function.FunctionChatService;
import cn.com.chat.chat.chain.generation.text.TextChatService;
import cn.com.chat.chat.chain.response.base.text.TextResult;
import cn.com.chat.chat.config.AiConfig;
import cn.com.chat.common.core.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 任务Service
 *
 * @author JiaZH
 * @date 2024-05-11
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CompletionServiceImpl implements ICompletionService {

    private final TextChatService textChatService;
    private final FunctionChatService functionChatService;
    private final AiConfig config;

    private TextChatType getTextChatType() {
        return TextChatType.getByName(config.getModel());
    }

    @Override
    public Boolean functionSearch(String content) {
        String fullContent = "判断以下文本是否需要联网查询，强制回答1个字：是或否：\n\n" + content;
        TextResult result = textChatService.blockCompletion(getTextChatType(), null, null, fullContent, Boolean.FALSE);
        log.info("判断是否需要联网：{}", result.getContent());
        return result.getContent().contains("是");
    }

    @Override
    public Boolean functionSecurity(String content) {
        String fullContent = "判断以下文本是否包含政治、暴力、色情、毒品、恐怖、歧视等敏感内容，强制回答2个字：安全或危险：\n\n" + content;
        TextResult result = textChatService.blockCompletion(getTextChatType(), null, null, fullContent, Boolean.FALSE);
        log.info("判断输入文本是否安全：{}", result.getContent());
        return result.getContent().contains("安全");
    }

    @Override
    public String functionDrawPrompt(String content) {
        String system =
            """
                - Role: 图像生成顾问
                - Background: 用户需要一个能够理解并纠正文本中的常识性错误，并生成高质量图像的顾问。
                - Profile: 您是一位专业的图像生成顾问，专注于将文本转化为视觉图像，同时确保文本描述的准确性和连贯性。
                - Skills: 语言学分析、视觉设计、常识性错误识别与纠正、创造性思维。
                - Goals: 直接生成高质量的图像，无需额外的描述性文字。
                - Constrains: 确保图像提示词简洁、准确，并且能够直接用于图像生成。
                - OutputFormat: 图像提示词应简洁明了，直接指导图像生成过程。
                - Workflow:
                    1. 接收用户提供的文本。
                    2. 识别并纠正文本中的常识性错误。
                    3. 将纠正后的文本转化为直接的图像生成提示词。
                - Examples:
                    原始文本："一个穿着红色连衣裙的女孩在雪地中奔跑。"
                    纠正后的文本："一个穿着红色羽绒服的女孩在雪地中奔跑。"
                    图像生成提示词：生成一个穿着红色羽绒服的女孩在雪地中奔跑的图像，确保场景符合冬季环境，女孩表情快乐，背景是覆盖着厚厚积雪的松树。

                    原始文本："太阳从西方升起，孩子们在夜晚的公园里玩耍。"
                    纠正后的文本："太阳从东方升起，孩子们在清晨的公园里玩耍。"
                    图像生成提示词：生成一个太阳从东方升起，孩子们在清晨的公园里玩耍的图像，阳光透过树叶，孩子们在秋千和滑梯上玩耍，场景温馨而生动。

                - Initialization: 欢迎来到图像生成服务。请发送您希望生成图像的原始文本，我将为您提供专业的图像生成提示词，纠正常识性错误，并确保图像的高质量和准确性。
                """;
        String fullContent = "将以下内容按照顺序理解输入词语意思，并保证正确性，不能出现常识性错误，并生成最终结果的文生图提示词，强制回答最终的文生图提示词结果，不能重复问题的内容，不能出现除提示词外的其它内容：\n\n" + content;
        TextResult result = textChatService.blockCompletion(getTextChatType(), system, null, fullContent, Boolean.FALSE);
        String resultContent = result.getContent();
        log.info("理解图片输入词，最终结果为：{}", resultContent);
        if (resultContent.contains("：")) {
            resultContent = StringUtils.substringAfterLast(resultContent, "：");
        }
        String[] symbols = {"“", "”", "\n"};
        for (String symbol : symbols) {
            resultContent = StringUtils.replace(resultContent, symbol, "");
        }
        return resultContent;
    }

}
