package com.udofly.utils.encrypt;

import android.content.Context;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import Decoder.BASE64Decoder;

/**
 * Describe: Rsa公钥解密
 * Created by udofly on 2021/12/24.
 */
public class RSAUtils {

    /**
     * 参考文档
     * https://blog.csdn.net/qq_41566219/article/details/116302087
     */
    public static final String RSA               = "RSA";// 非对称加密密钥算法
    public static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";//加密填充方式
    public static       String strPublicKey      = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCdFkdYqgiheS00S9bPnAojaf+e\n" +
            "wGcy0CAYunEhqFD8kLK626EBR+E+dG6Hqk6U+1lbu9l6M7fKIkSejQZ7P6fUaIPz\n" +
            "E1cJSZxtDGF0Gxt7JzXibTD4BqOHKKJcWH+yTW/AykMHykZv0uUptfrMAbasz86/\n" +
            "yVDI47rGP6fk9ocaJwIDAQAB";


    /**
     * Rsa公钥解密
     *
     * @param context
     * @param miWen
     */
    public static String rsaDecodeByPublicKey(Context context, String miWen) {
        PublicKey publicKey = (RSAPublicKey) getPubKey(context, strPublicKey);
        String    mingWen   = "";
        try {
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[]       cipherText = Base64.decode(miWen, Base64.DEFAULT);
            List<byte[]> listByte   = getListByte(cipherText);

            //获取加密后的字节数组
            List<byte[]> listByteDe = new ArrayList<>();
            for (int i = 0; i < listByte.size(); i++) {
                byte[] decryptText = cipher.doFinal(listByte.get(i));
                listByteDe.add(decryptText);
            }

            //确定数组长度
            int length_byte = 0;
            for (int i = 0; i < listByteDe.size(); i++) {
                length_byte += listByteDe.get(i).length;
            }
            byte[] all_byte = new byte[length_byte];

            //解密后.拼成一个完成的字节数组后再转字符串(避免出现中文乱码)
            int countLength = 0;
            for (int i = 0; i < listByteDe.size(); i++) {
                byte[] b = listByteDe.get(i);
                System.arraycopy(b, 0, all_byte, countLength, b.length);
                countLength += b.length;
            }
            mingWen = new String(all_byte, "UTF-8");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mingWen;

    }

    /**
     * 将字节数组按128长度分段
     *
     * @param data
     * @return
     */
    public static List<byte[]> getListByte(byte[] data) {
        List<byte[]> list  = new ArrayList<>();
        int          index = data.length / 128;

        for (int i = 0; i < index; i++) {
            list.add(getByteSub(data, i * 128, 128));
        }

        if (index * 128 < data.length) {
            list.add(getByteSub(data, index * 128, 128));
        }
        return list;
    }

    /**
     * 数据分段
     *
     * @param data
     * @param start
     * @param length
     * @return
     */
    public static byte[] getByteSub(byte[] data, int start, int length) {
        byte[] bt = new byte[length];

        if (start + length > data.length) {
            bt = new byte[data.length - start];
        }
        for (int i = 0; i < length && (i + start) < data.length; i++) {
            bt[i] = data[i + start];
        }
        return bt;
    }


    /**
     * 实例化公钥
     *
     * @return
     */
    public static PublicKey getPubKey(Context context, String strPublicKey) {
        PublicKey publicKey = null;

        //获取方式1
        try {
            // 自己的公钥(测试)
            X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(new BASE64Decoder().decodeBuffer(strPublicKey));
            // RSA对称加密算法
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            // 取公钥匙对象
            publicKey = keyFactory.generatePublic(bobPubKeySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //获取方式2

//        try {
//            InputStream inPublic = context.getResources().getAssets().open("rsa_public_key.pem");
//            publicKey = loadPublicKey(inPublic);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        return publicKey;
    }


    /**
     * 从文件中输入流中加载公钥
     *
     * @param in 公钥输入流
     * @throws Exception 加载公钥时产生的异常
     */
    public static PublicKey loadPublicKey(InputStream in) throws Exception {
        try {
            return loadPublicKey(readKey(in));
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }

    /**
     * 读取密钥信息
     *
     * @param in
     * @return
     * @throws IOException
     */
    private static String readKey(InputStream in) throws IOException {
        BufferedReader br       = new BufferedReader(new InputStreamReader(in));
        String         readLine = null;
        StringBuilder  sb       = new StringBuilder();
        while ((readLine = br.readLine()) != null) {
            if (readLine.charAt(0) == '-') {
                continue;
            } else {
                sb.append(readLine);
                sb.append('\r');
            }
        }

        return sb.toString();
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public static PublicKey loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[]             buffer     = Base64.decode(publicKeyStr, 0);
            KeyFactory         keyFactory = KeyFactory.getInstance(RSA);
            X509EncodedKeySpec keySpec    = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~以下需要验证正确性~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    private static Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥

    /**
     * 随机生成密钥对
     *
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair       keyPair         = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey      = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey  publicKey       = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String        publicKeyString = Base64.encodeToString(publicKey.getEncoded(), 0);
        // 得到私钥字符串
        String privateKeyString = Base64.encodeToString((privateKey.getEncoded()), 0);
        // 将公钥和私钥保存到Map
        keyMap.put(0, publicKeyString);  //0表示公钥
        keyMap.put(1, privateKeyString);  //1表示私钥
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String rsaEncrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[]       decoded = Base64.decode(publicKey, 0);
        RSAPublicKey pubKey  = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeToString(cipher.doFinal(str.getBytes("UTF-8")), 0);
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String rsaDecrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decode(str.getBytes("UTF-8"), 0);
        //base64编码的私钥
        byte[]        decoded = Base64.decode(privateKey, 0);
        RSAPrivateKey priKey  = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

}
