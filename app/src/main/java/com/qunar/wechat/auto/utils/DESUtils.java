package com.qunar.wechat.auto.utils;


/*
 * $Id: CrcUtils.java 15931 2013-06-13 08:35:41Z yijiu.chen $
 *
 * Copyright (c) 2012 Qunar.com. All Rights Reserved.
 */


import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * 参数加密解密类，会进行简单的crc校验
 */
public final class DESUtils {


    private  static byte[] iv={1,3,6,3,5,2,7,5};
    /**
     *  加密工具类
     * @param seed 密钥
     * @param encryptString 加密字符
     * @return 密文
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String encrypt(String seed, String encryptString) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(seed.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes("UTF-8"));
        return Base64.encodeToString(encryptedData, Base64.DEFAULT);
    }

    /***
     * 解密
     * @param seed
     * @param decryptString
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws NoSuchPaddingException
     * @throws InvalidAlgorithmParameterException
     */
    public static String decrypt(String seed, String decryptString) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException {
        byte[] byteMi = Base64.decode(decryptString, Base64.DEFAULT);
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(seed.getBytes(),"DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        byte decryptedData[] = cipher.doFinal(byteMi);
        return new String(decryptedData,"UTF-8");
    }

}