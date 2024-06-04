package cn.com.chat.chat.chain.generation.function;

import cn.com.chat.chat.chain.generation.function.entity.ChatFunctionObject;
import cn.com.chat.chat.chain.generation.function.entity.FunctionCompletionResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 函数调用服务接口
 *
 * @author JiaZH
 * @date 2024-05-11
 */
@Slf4j
@Service
@Primary
@AllArgsConstructor
public class FunctionChatServiceWrapper implements FunctionChatService {

    private final FunctionChatServiceFactory functionChatServiceFactory;

    @Override
    public List<FunctionCompletionResult> functionCompletion(String content, List<ChatFunctionObject> list) {
        FunctionChatService functionChatService = functionChatServiceFactory.getFunctionChatService();
        return functionChatService.functionCompletion(content, list);
    }

}
