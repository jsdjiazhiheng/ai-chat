package cn.com.chat.chat.chain.apis;

/**
 * 阿里云接口
 *
 * @author JiaZH
 * @date 2024-06-04
 */
public interface AliyunApis {

    String QWEN_API = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

    String QWEN_AIGC_API = "https://dashscope.aliyuncs.com/api/v1/services/aigc/multimodal-generation/generation";

    String QWEN_LONG_API = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";

    String WANX_V1_API = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text2image/image-synthesis";

    String WANX_STYLE_COSPLAY_V1_API = "https://dashscope.aliyuncs.com/api/v1/services/aigc/image-generation/generation";

    String WANX_STYLE_REPAINT_V1_API = "https://dashscope.aliyuncs.com/api/v1/services/aigc/image-generation/generation";

    String WANX_STYLE_BACKGROUND_V2_API = "https://dashscope.aliyuncs.com/api/v1/services/aigc/background-generation/generation";

    String WANX_SKETCH_TO_IMAGE_LITE_API = "https://dashscope.aliyuncs.com/api/v1/services/aigc/image2image/image-synthesis";

    String WANX_TASK_API = "https://dashscope.aliyuncs.com/api/v1/tasks/";

}
