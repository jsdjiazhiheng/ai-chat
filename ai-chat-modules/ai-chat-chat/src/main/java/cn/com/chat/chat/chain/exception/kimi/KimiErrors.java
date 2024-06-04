package cn.com.chat.chat.chain.exception.kimi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-05-09
 */
@AllArgsConstructor
@Getter
public enum KimiErrors {

    CONTENT_FILTER("400 CONTENT_FILTER", new Description[]{
        Description.builder()
            .errorMessage("The request was rejected because it was considered high risk")
            .message("您的输入或生成内容可能包含不安全或敏感内容")
            .build()
    }),
    INVALID_REQUEST_ERROR("400 invalid_request_error", new Description[]{
        Description.builder()
            .errorMessage("Invalid request: ")
            .message("请求无效，通常是您请求格式错误或者缺少必要参数，请检查后重试")
            .build(),
        Description.builder()
            .errorMessage("Input token length too long")
            .message("请求中的 tokens 长度过长，请求不要超过模型 tokens 的最长限制")
            .build(),
        Description.builder()
            .errorMessage("Your request exceeded model token limit :")
            .message("请求的 tokens 数和设置的 max_tokens 加和超过了模型规格长度，请检查请求体的规格或选择合适长度的模型")
            .build(),
        Description.builder()
            .errorMessage("Invalid purpose: only 'file-extract' accepted")
            .message("请求中的目的（purpose）不正确，当前只接受 'file-extract'，请修改后重新请求")
            .build(),
        Description.builder()
            .errorMessage("File size is too large, max file size is 100MB, please confirm and re-upload the file")
            .message("上传的文件大小超过了限制，请重新上传")
            .build(),
        Description.builder()
            .errorMessage("File size is zero, please confirm and re-upload the file")
            .message("上传的文件大小为 0，请重新上传")
            .build(),
        Description.builder()
            .errorMessage("The number of files you have uploaded exceeded the max file count")
            .message("上传的文件总数超限，请删除不用的早期的文件后重新上传")
            .build(),
    }),
    INVALID_AUTHENTICATION_ERROR("401 invalid_authentication_error", new Description[]{
        Description.builder()
            .errorMessage("Invalid Authentication")
            .message("鉴权失败，请检查 apikey 是否正确，请修改后重试")
            .build(),
        Description.builder()
            .errorMessage("Incorrect API key provided")
            .message("鉴权失败，请检查 apikey 是否提供以及 apikey 是否正确，请修改后重试")
            .build(),
    }),
    EXCEEDED_CURRENT_QUOTA_ERROR("403 exceeded_current_quota_error", new Description[]{
        Description.builder()
            .errorMessage("Your account")
            .message("账户异常，请检查您的账户余额")
            .build(),
        Description.builder()
            .errorMessage("The API you are accessing is not open")
            .message("访问的 API 暂未开放")
            .build(),
        Description.builder()
            .errorMessage("You are not allowed to get other user info")
            .message("访问其他用户信息的行为不被允许，请检查")
            .build(),
    }),
    RESOURCE_NOT_FOUND_ERROR("404 resource_not_found_error", new Description[]{
        Description.builder()
            .errorMessage("Not found the model or Permission denied")
            .message("不存在此模型或者没有授权访问此模型，请检查后重试")
            .build(),
        Description.builder()
            .errorMessage("Users ")
            .message("找不到该用户，请检查后重试")
            .build(),
    }),
    ENGINE_OVERLOADED_ERROR("429 engine_overloaded_error", new Description[]{
        Description.builder()
            .errorMessage("The engine is currently overloaded, please try again later")
            .message("当前并发请求过多，节点限流中，请稍后重试")
            .build()
    }),
    ;

    private final String code;

    private final Description[] descriptions;

    @Data
    @Builder
    private static class Description {
        private String errorMessage;
        private String message;
    }

}
