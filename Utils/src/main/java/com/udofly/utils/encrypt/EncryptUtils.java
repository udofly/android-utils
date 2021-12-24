package com.udofly.utils.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Describe: 通用的加密类
 * Created by udofly on 2021/12/24.
 */
public class EncryptUtils {

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * MD5、SHA1加密
     *
     * @param algorithm 加密类型（MD5、SHA1）
     * @param original  加密的原文
     * @return 返回加密后的字符串
     */
    public static String encrypt(String algorithm, String original) {
        MessageDigest mMessageDigest = null;
        byte[]        data           = original.getBytes();
        try {
            mMessageDigest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //加载数据
        mMessageDigest.update(data);

        //处理数据
        byte[]        digest  = mMessageDigest.digest();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            int result = digest[i] & 0xFF;
            if (result <= 0x0F) {
                builder.append("0");
            }
//将result转换成十六进制表示
            String hexResult = Integer.toHexString(result);
            builder.append(hexResult);
        }
        return builder.toString();
    }

}
