package cn.com.chat.chat.chain.utils;

import cn.com.chat.common.json.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 日志输出工具类
 *
 * @author JiaZH
 * @date 2024-06-13
 */
@Slf4j
public class ChatLogUtils {


    public static void printRequestLog(Class<?> classz, Object response) {
        printLog(classz, "请求参数", JsonUtils.toFormatJsonString(response));
    }

    public static void printResponseLog(Class<?> classz, Object response) {
        printLog(classz, "请求结果", JsonUtils.toFormatJsonString(response));
    }

    public static void printResultLog(Class<?> classz, Object response) {
        printLog(classz, "返回结果", JsonUtils.toFormatJsonString(response));
    }

    public static void printLog(Class<?> classz, String type, String response) {
        log.info("{} -> {} ： \n{}", classz.getSimpleName(), type, response);
    }

}
