package com.wuxianedu.corelib.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 * @author lungank
 */
public class DateUtils {

    public static final SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    public static final SimpleDateFormat df2 = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
    public static final SimpleDateFormat df3 = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
    public static final SimpleDateFormat df4 =
            new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒", Locale.CHINA);
    public static final SimpleDateFormat df5 = new SimpleDateFormat("Gyyyy年MM月dd日", Locale.CHINA);
    public static final SimpleDateFormat df6 = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);

    /**
     * 将Date转为字符串日期
     * @param df 日期格式
     * @param date 日期
     * @return 日期文本
     */
    public static String data2String(DateFormat df,Date date){
        return df.format(date);
    }

    /**
     * 将字符串日期转为Date
     * @param df 日期格式
     * @param date 日期文本
     * @return 日期
     */
    public static Date string2date(DateFormat df,String date){
        try {
            return df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
