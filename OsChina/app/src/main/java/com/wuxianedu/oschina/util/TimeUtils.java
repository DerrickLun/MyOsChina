package com.wuxianedu.oschina.util;

import android.content.Context;

import com.wuxianedu.oschina.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 */
public class TimeUtils {

    public static SimpleDateFormat hourMinDateFormat = new SimpleDateFormat("HH:mm");

    public static String friendlyFormat(Context context, Date date) {
        Date now = new Date(System.currentTimeMillis());
        if (now.getYear() == date.getYear()) {
            if (now.getMonth() == date.getMonth()) {
                if (now.getDate() == date.getDate()) {
                    return context.getString(R.string.today, hourMinDateFormat.format(date));
                    //return hourMinDateFormat.format(date);
                } else if (now.getDate() - date.getDate() == 1) {
                    return context.getString(R.string.first_before_dat, hourMinDateFormat.format(date));
                } else if (now.getDate() - date.getDate() == 2) {
                    return context.getString(R.string.secode_before_dat, hourMinDateFormat.format(date));
                } else {
                    return String.format(context.getString(R.string.before_day), now.getDate() - date.getDate(), hourMinDateFormat.format(date));
                }
            } else {
                return context.getString(R.string.before_month, now.getMonth() - date.getMonth());
            }
        } else {
            return context.getString(R.string.before_year, now.getYear() - date.getYear());
        }
    }

}
