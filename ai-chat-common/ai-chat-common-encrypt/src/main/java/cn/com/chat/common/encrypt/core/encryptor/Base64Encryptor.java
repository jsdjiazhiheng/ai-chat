package cn.com.chat.common.encrypt.core.encryptor;

import cn.com.chat.common.encrypt.core.EncryptContext;
import cn.com.chat.common.encrypt.enumd.AlgorithmType;
import cn.com.chat.common.encrypt.utils.EncryptUtils;
import cn.com.chat.common.encrypt.enumd.EncodeType;

/**
 * Base64算法实现
 *
 * @author 老马
 * @version 4.6.0
 */
public class Base64Encryptor extends AbstractEncryptor {

    public Base64Encryptor(EncryptContext context) {
        super(context);
    }

    /**
     * 获得当前算法
     */
    @Override
    public AlgorithmType algorithm() {
        return AlgorithmType.BASE64;
    }

    /**
     * 加密
     *
     * @param value      待加密字符串
     * @param encodeType 加密后的编码格式
     */
    @Override
    public String encrypt(String value, EncodeType encodeType) {
        return EncryptUtils.encryptByBase64(value);
    }

    /**
     * 解密
     *
     * @param value      待加密字符串
     */
    @Override
    public String decrypt(String value) {
        return EncryptUtils.decryptByBase64(value);
    }
}
