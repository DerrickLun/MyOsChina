package com.wuxianedu.corelib.util;

import java.security.MessageDigest;

/**
 * MD5工具类
 * Created by lungank on 2016/4/9.
 */
public class MD5Utils {

    /**
     * 用md5算法对字符串进行加密
     * @param url 文件地址
     * @return 加密后的文件地址
     */
    public static String md5(String url) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(url.getBytes("UTF-8"));
            byte messageDigest[] = md5.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String t = Integer.toHexString(0xFF & aMessageDigest);
                if (t.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(t);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
