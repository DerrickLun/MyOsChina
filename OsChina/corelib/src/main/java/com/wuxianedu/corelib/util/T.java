package com.wuxianedu.corelib.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

/**
 * Toast提示管理类
 * Created by lungank on 2016/4/9.
 */
public class T {

    /**
     * 短时间显示Toast
     * @param context 上下文对象
     * @param message 要显示的消息
     */
    public static void showShort(Context context, int message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 短时间显示Toast
     * @param context 上下文对象
     * @param message 要显示的消息
     */
    public static void showShort(Context context, CharSequence message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     * @param context 上下文对象
     * @param message 要显示的消息
     */
    public static void showLong(Context context, int message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast
     * @param context 上下文对象
     * @param message 要显示的消息
     */
    public static void showLong(Context context,@NonNull CharSequence message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 在非UI线程中显示Toast消息
     * @param activity 当前Activity对象
     * @param message 要显示的消息
     */
    public static void showOnUi(final Activity activity,final String message){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
