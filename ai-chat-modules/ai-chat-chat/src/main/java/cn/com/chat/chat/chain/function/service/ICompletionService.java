package cn.com.chat.chat.chain.function.service;

/**
 * TODO
 *
 * @author JiaZH
 * @date 2024-05-11
 */
public interface ICompletionService {

    Boolean functionSearch(String content);

    Boolean functionSecurity(String content);

    String functionDrawPrompt(String content);

}
