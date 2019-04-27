package com.kenick.sport.common.utils;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
    public static void main(String[] args) {
        String password = md5Encode("admin");
        System.out.println(password);
    }

    /**
     *  对密码进行md5加密
     * @param password 密码字符串
     * @return md5字符串
     */
    public static String md5Encode(String password){
        String algorithm = "MD5";
        char[] encodeHex  = null;
        try {
            MessageDigest instance = MessageDigest.getInstance(algorithm);
            // MD5加密后密文
            byte[] digest = instance.digest(password.getBytes());
            // 十六进制再加密一次
            encodeHex = Hex.encodeHex(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if(encodeHex != null){
            return new String(encodeHex);
        }else{
            return null;
        }
    }
}
