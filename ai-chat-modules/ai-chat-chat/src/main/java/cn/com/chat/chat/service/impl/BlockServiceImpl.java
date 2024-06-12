package cn.com.chat.chat.service.impl;

import cn.com.chat.chat.chain.enums.ImageChatType;
import cn.com.chat.chat.chain.enums.TextChatType;
import cn.com.chat.chat.chain.function.service.ICompletionService;
import cn.com.chat.chat.chain.generation.image.ImageChatService;
import cn.com.chat.chat.chain.generation.text.TextChatService;
import cn.com.chat.chat.chain.request.base.text.MessageItem;
import cn.com.chat.chat.chain.response.base.image.ImageResult;
import cn.com.chat.chat.chain.response.base.text.TextResult;
import cn.com.chat.chat.chain.utils.ImageUtils;
import cn.com.chat.chat.domain.bo.ChatMessageBo;
import cn.com.chat.chat.domain.vo.ChatMessageVo;
import cn.com.chat.chat.domain.vo.MessageVO;
import cn.com.chat.chat.enums.ContentTypeEnums;
import cn.com.chat.chat.service.IAssistantService;
import cn.com.chat.chat.service.IBlockService;
import cn.com.chat.chat.service.IChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BlockServiceImpl implements IBlockService {

    private final TextChatService textChatService;
    private final ImageChatService imageChatService;
    private final ICompletionService completionService;
    private final IChatMessageService chatMessageService;
    private final IAssistantService assistantService;


    @Override
    public MessageVO textChat(TextChatType type, Long chatId, String content) {
        /*String system = "扮演一个名叫\"童童AI31号\"的幼儿园小朋友,以下是这个角色的一些设定:\n" +
            "性格:天真活泼、好奇爱学习、有趣幽默。\n" +
            "外表:一个看起来3-4岁大小的卡通形象,穿着幼儿园园服,头发以及眼睛的颜色可以随机变化。\n" +
            "语气:用诙谐有趣的语气说话,时不时会冒出一些வenance巧妙或者白话的词语,就像个顽皮的小孩。\n" +
            "喜好:喜欢讲故事、唱儿歌、做游戏、认识事物。\n" +
            "和小朋友的互动:像个小朋友一样,以小朋友的视角思考和表达,用通俗易懂的语言解答小朋友的各种问题,内容尽量简单生动。即使遇到说不清的复杂问题,也会用有趣的比喻或拆解的方式讲解。\n" +
            "例子:\n" +
            "小朋友问:\"地球是怎么形成的?\"\n" +
            "童童31号: \"哦,地球有好几百万年的年纪呢!它以前是一个火热热的大火球,慢慢慢慢冷却下来,外面形成了坚硬的地壳,中间是熔岩,就像是一个大馅饼!后来上面下起了雨雪才有了大气和海洋,最后长出了树木和小动物,才变成现在这个漂亮的地球家园!\"\n" +
            "总之童童31号会尽量用生动有趣的方式,让3-6岁的小朋友能更好地理解一些简单的科学常识及生活问题。努力扮演好这个角色,符合小朋友的性格和认知特点。";*/

        String system = assistantService.getSystemPromptByModel(type.name());

        List<MessageItem> history = chatMessageService.listChatHistory(chatId);

        boolean useNet = completionService.functionSearch(content);

        TextResult textResult = textChatService.blockCompletion(type, system, history, content, useNet);

        ChatMessageBo userMessage = chatMessageService.insertUserMessage(chatId, ContentTypeEnums.TEXT.name(), textResult.getModel(), textResult.getVersion(), content, 2L);

        ChatMessageBo assistantMessage = chatMessageService.insertAssistantMessage(chatId, userMessage.getMessageId(), ContentTypeEnums.TEXT.name(), textResult);

        MessageVO messageVO = new MessageVO();
        messageVO.setUserMessage(chatMessageService.queryById(userMessage.getId()));
        messageVO.setAssistantMessage(chatMessageService.queryById(assistantMessage.getId()));

        return messageVO;
    }

    @Override
    public MessageVO imageChat(ImageChatType type, Long chatId, String content) {
        ImageResult result = imageChatService.blockGenImage(type, content);

        ChatMessageBo userMessage = chatMessageService.insertUserMessage(chatId, ContentTypeEnums.IMAGE.name(), result.getModel(), result.getVersion(), content, 2L);

        ChatMessageBo assistantMessage = chatMessageService.insertAssistantMessage(chatId, userMessage.getMessageId(), ContentTypeEnums.IMAGE.name(), result);

        MessageVO messageVO = new MessageVO();
        messageVO.setUserMessage(chatMessageService.queryById(userMessage.getId()));
        ChatMessageVo messageVo = chatMessageService.queryById(assistantMessage.getId());

        List<String> list = result.getData().stream().map(ImageUtils::getImageUrl).toList();

        messageVo.setImageList(list);
        messageVO.setAssistantMessage(messageVo);

        return messageVO;
    }

}
