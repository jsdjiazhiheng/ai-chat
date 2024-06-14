package cn.com.chat.chat.chain.apis;

/**
 * 画宇宙API
 *
 * @author JiaZH
 * @date 2024-06-13
 */
public interface NoLiPixApis {

    String GET_PROMPT_URL = "https://open.nolibox.com/prod-open-aigc/engine/get_prompt";

    String DRAW_URL = "https://open.nolibox.com/prod-open-aigc/engine/push";

    String TASK_URL = "https://open.nolibox.com/prod-open-aigc/engine/status/{}";

}
