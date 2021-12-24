package com.udofly.utils.encrypt;


import android.util.Base64;

import com.udofly.utils.StringUtil;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Describe:
 * Created by Gao Chunfa on 解密.
 * Company: Hainan DaDi(Jinan) Network Technology Co. Ltd
 */
public class AesEcb256Utils {

    static String key = "iDtag1t9rNzUWGpFojnrdwceow8TlkI8";

    /**
     * AesEcb256加密
     *
     * @param mingWen
     * @return
     */
    public static String aesEcb256Encrypt(String myKey,String mingWen) {
        if (StringUtil.isNull(mingWen) || StringUtil.isNull(myKey)) {
            return "";
        }
        byte[] encrypted = new byte[0];
        try {
            byte[]        raw      = myKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher        cipher   = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] bytes_mingwen = mingWen.getBytes("utf-8");
            encrypted = cipher.doFinal(bytes_mingwen);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        String s = Base64.encodeToString(encrypted, Base64.DEFAULT);
        return s;//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    /**
     * AesEcb256解密
     *
     * @param miwen
     * @return
     */
    public static String aesEcb256Decrypt(String myKey,String miwen) {

        if (StringUtil.isNull(miwen) || StringUtil.isNull(myKey)) {
            return "";
        }

        try {

            byte[] cryptograph = miwen.getBytes("utf-8");

            byte[]        raw      = myKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
//            SecretKey secretKey = kgen.generateKey();
//            byte[] enCodeFormat = skeySpec.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(raw, "AES");
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] content = cipher.doFinal(Base64.decode(cryptograph, Base64.DEFAULT));
            String s       = new String(content);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




}
