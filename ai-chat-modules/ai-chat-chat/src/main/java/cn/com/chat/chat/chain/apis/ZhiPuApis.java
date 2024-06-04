package cn.com.chat.chat.chain.apis;

/**
 * 智谱API接口地址
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-05-15
 */
public interface ZhiPuApis {

    /**
     * 通用大模型、超拟人大模型请求地址
     */
    String CHAT_API = "https://open.bigmodel.cn/api/paas/v4/chat/completions";

    /**
     * 图形大模型请求地址
     */
    String IMAGE_API = "https://open.bigmodel.cn/api/paas/v4/images/generations";

    /**
     * 向量模型请求地址
     */
    String EMBEDDING_API = "https://open.bigmodel.cn/api/paas/v4/embeddings";

    /**
     * 文件管理请求地址
     */
    String FILE_URL = "https://open.bigmodel.cn/api/paas/v4/files";

}
