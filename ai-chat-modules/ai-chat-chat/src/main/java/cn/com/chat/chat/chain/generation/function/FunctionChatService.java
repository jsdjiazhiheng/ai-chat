package cn.com.chat.chat.chain.generation.function;

import cn.com.chat.chat.chain.generation.function.entity.ChatFunctionObject;
import cn.com.chat.chat.chain.generation.function.entity.FunctionCompletionResult;

import java.util.List;

/**
 * 函数调用服务接口
 *
 * @author JiaZH
 * @date 2024-05-11
 */
public interface FunctionChatService {

    /**
     * 函数调用：输出结构化参数数据
     *
     * @param content 输入内容
     * @return 结果
     */
    List<FunctionCompletionResult> functionCompletion(String content, List<ChatFunctionObject> functionObjectList);

}
