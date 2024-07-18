package cn.com.chat.chat.chain.utils;

import cn.com.chat.common.core.utils.StringUtils;
import cn.com.chat.common.oss.core.OssClient;
import cn.com.chat.common.oss.entity.UploadResult;
import cn.com.chat.common.oss.factory.OssFactory;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * 图片生成处理工具类
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Slf4j
public class ImageUtils {

    /**
     * 将base64转换为本地url
     *
     * @param module 模块
     * @param base64 base64图片
     * @return 本地图片名称
     */
    public static String base64ToUrl(String module, String base64) {
        byte[] bytes = Base64Decoder.decode(base64);
        OssClient storage = OssFactory.instance();
        UploadResult result = storage.upload(bytes, module + File.separator + UUID.fastUUID() + ".png", "image/png");
        return result.getFilename();
    }

    /**
     * 将url转换为本地url
     *
     * @param module 模块
     * @param url    url
     * @return 本地图片名称
     */
    public static String urlToUrl(String module, String url) {
        byte[] bytes = HttpUtil.downloadBytes(url);
        OssClient storage = OssFactory.instance();
        UploadResult result = storage.upload(bytes, module + File.separator + UUID.fastUUID() + ".png", "image/png");
        return result.getFilename();
    }

    /**
     * 获取图片url 7天有效期
     */
    public static String getImageUrl(String imageUrl) {
        OssClient client = OssFactory.instance();
        return client.getPrivateUrl(imageUrl, 60 * 60 * 24 * 7);
    }

    public static String urlToBase64(String url, boolean isContainDataStr) {
        byte[] bytes = compressImage(url, 500, 500, 1024 * 1024, 0.8);
        if (isContainDataStr) {
            return StringUtils.format("data:image/jpeg;base64,{}", Base64.encode(bytes));
        } else {
            return Base64.encode(bytes);
        }
    }

    public static byte[] compressImage(String url, int maxWidth, int maxHeight, long destFileSize, double accuracy) {
        byte[] imageBytes = HttpUtil.downloadBytes(url);
        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
        BufferedImage image = ImgUtil.read(bais);

        // 检查图片尺寸
        if (image.getWidth() > maxWidth || image.getHeight() > maxHeight) {
            Image scaledImage = ImgUtil.scale(image, maxWidth, maxHeight);
            image = ImgUtil.toBufferedImage(scaledImage, "jpg");
        }

        // 检查图片大小
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImgUtil.writeJpg(image, baos);
        while (baos.size() > destFileSize) {
            Image scaledImage = ImgUtil.scale(image, (int) (image.getWidth() * accuracy), (int) (image.getHeight() * accuracy));
            image = ImgUtil.toBufferedImage(scaledImage, "jpg");
            baos.reset();
            ImgUtil.writeJpg(image, baos);
        }

        return baos.toByteArray();
    }

}
