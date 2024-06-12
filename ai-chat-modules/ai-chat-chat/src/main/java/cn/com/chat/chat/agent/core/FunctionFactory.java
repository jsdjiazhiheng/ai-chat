package cn.com.chat.chat.agent.core;

import cn.com.chat.chat.agent.core.function.AiFunctionType;
import cn.com.chat.chat.agent.core.function.FunctionType;
import cn.com.chat.chat.agent.core.function.ToolFunctionType;
import cn.com.chat.chat.agent.prompt.function.FunctionTypePrompt;
import cn.com.chat.chat.agent.prompt.function.functionObj.FunctionTypeFuncObj;
import cn.com.chat.chat.chain.function.service.ICompletionService;
import cn.com.chat.common.json.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Function;

/**
 * 能力工厂：根据任务获取不同能力
 *
 * @author JiaZH
 * @date 2024-05-11
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FunctionFactory {

    private final ICompletionService completionService;

    public Function getFunction(String content) {

        String role = "";
        String stepName = "";

        FunctionType functionType = getFunctionType(role, stepName, content);
        switch (functionType) {
            case AI:
                AiFunctionType aiFunctionType = getAiFunctionType(role, stepName, content);
                if (aiFunctionType != null) {
                    runAiFunction(aiFunctionType, content);
                }
            case TOOL:
                ToolFunctionType toolFunctionType = getToolFunctionType(role, stepName, content);
                if (toolFunctionType != null) {
                    runToolFunction(toolFunctionType, content);
                }
            default:
                System.out.println("DEFAULT");
                break;
        }


        return null;
    }

    private ToolFunctionType getToolFunctionType(String role, String stepName, String description) {
        return null;
    }

    private AiFunctionType getAiFunctionType(String role, String stepName, String description) {
        return null;
    }

    private FunctionType getFunctionType(String role, String stepName, String description) {

        String prompt = FunctionTypePrompt.prompt(role, stepName, description);


        FunctionTypeFuncObj funcObj = JsonUtils.parseObject(prompt, FunctionTypeFuncObj.class);

        return Objects.requireNonNull(funcObj).getFunctionType();
    }

    private void runAiFunction(AiFunctionType aiFunctionType, String content) {
        switch (aiFunctionType) {
            case TEXT_GEN:
                break;
            case IMAGE_GEN:
                break;
            case VIDEO_GEN:
                break;
            default:
                System.out.println("DEFAULT");
        }
    }

    private void runToolFunction(ToolFunctionType toolFunctionType, String content) {
        switch (toolFunctionType) {
            case CRAWL:
                break;
            case FILE:
                break;
            case BASH:
                break;
            case API_MAIL:
                break;
            case SEARCH:
                break;
            case ANALYSIS:
                break;
            case API_PAY:
                break;
            case API_WEATHER:
                break;
            case API_SMS:
                break;
            case API_ORDER:
                break;
            default:
                System.out.println("DEFAULT");
        }
    }

}
