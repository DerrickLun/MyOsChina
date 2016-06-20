package com.wuxianedu.corelib.util;

import java.io.UnsupportedEncodingException;

/**
 * 文本工具类
 * Created by lungank on 2016/4/9.
 */
public class TextUtils {

    /**
     * 获取文本内容的长度(中文算一个字符，英文算半个字符，包括标点符号)
     * @param str 文本
     * @return 文本字符数
     */
    public static int getTextLengthes(String str){
        int number=getTextLength(str);
        int length=number/2;
        if(number % 2 != 0){
            length+=1;
        }
        return length;
    }

    /**
     * 获取文本内容的长度(中文算两个字符，英文算一个字符)
     * @param str 文本
     * @return 文本字符数
     */
    public static int getTextLength(String str){
        int length=0;
        try {
            str=new String(str.getBytes("GBK"), "ISO8859_1");
            length=str.length();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return length;
    }

    /**
     * 格式化,保留二位小数
     * @param value 数字
     * @return 格式化后的文本
     */
    public static String format(double value){
        return String.format("%.2f", value);
    }


    /**
     * 判断文本是否为空
     * @param str 要检测的文本
     * @return 是否为空
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * 判断文本是否为空
     * @param str 要检测的文本
     * @return 是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
}
