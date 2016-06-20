package com.wuxianedu.oschina.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.wuxianedu.oschina.R;

/**
 * Snackbar工具类
 */
public class SnackbarUtils {

    //颜色
    public static int colorResId = R.color.blue;

    /**
     * 显示消息
     * @param context
     * @param view
     * @param messageId
     */
    public static void showMessage(Context context,View view, int messageId){
        final Snackbar snackbar = Snackbar.make(view, context.getString(messageId),
            Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(context.getResources().getColor(colorResId));
        snackbar.show();
    }

    /**
     * 显示消息
     * @param context
     * @param view
     * @param message
     */
    public static void showMessage(Context context,View view, String message){
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(context.getResources().getColor(colorResId));
        snackbar.show();
    }

}
