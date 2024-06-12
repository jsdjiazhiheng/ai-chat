package cn.com.chat.chat.chain.utils;

import cn.com.chat.common.oss.core.OssClient;
import cn.com.chat.common.oss.entity.UploadResult;
import cn.com.chat.common.oss.factory.OssFactory;
import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.lang.UUID;
import cn.hutool.http.HttpUtil;

import java.io.File;

/**
 * 图片生成处理工具类
 *
 * @author JiaZH
 * @date 2024-05-22
 */
public class ImageUtils {

    /**
     * 将base64转换为本地url
     * @param module 模块
     * @param base64 base64图片
     * @return 本地图片名称
     */
    public static String base64ToUrl(String module, String base64) {
        byte[] bytes = Base64Decoder.decode(base64);
        OssClient storage = OssFactory.instance();
        UploadResult result = storage.upload(bytes, module + File.separator  + UUID.fastUUID() + ".png", "image/png");
        return result.getFilename();
    }

    /**
     * 将url转换为本地url
     * @param module 模块
     * @param url url
     * @return 本地图片名称
     */
    public static String urlToUrl(String module, String url) {
        byte[] bytes = HttpUtil.downloadBytes(url);
        OssClient storage = OssFactory.instance();
        UploadResult result = storage.upload(bytes, module + File.separator  + UUID.fastUUID() + ".png", "image/png");
        return result.getFilename();
    }

    /**
     * 获取图片url 7天有效期
     */
    public static String getImageUrl(String imageUrl) {
        OssClient client = OssFactory.instance();
        return client.getPrivateUrl(imageUrl, 60 * 60 * 24 * 7);
    }

}
