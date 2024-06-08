package cn.com.chat.chat.chain.apis;

/**
 * OpenAiApi
 *
 * @author JiaZH
 * @version 1.0
 * @date 2024-06-08
 */
public interface OpenAiApis {

    String TEXT_API = "https://api.openai.com/v1/chat/completions";

    String IMAGE_API = "https://api.openai.com/v1/images/generations";

    String TTS_API = "https://api.openai.com/v1/audio/speech";

    String EMBEDDING_API = "https://api.openai.com/v1/embeddings";

}
