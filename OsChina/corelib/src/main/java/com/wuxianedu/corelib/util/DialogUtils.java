package com.wuxianedu.corelib.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * 提示框工具类
 * Created by lungank on 2016/4/11.
 */
public class DialogUtils {

    /**
     * 显示提示框
     * @param context 上下文对象
     * @param message 提示信息
     * @param sureText 确认按钮文本
     * @param cancelText 取消按钮文本
     * @param sureListener 确认监听器
     * @param cancelListener 取消监听器
     * @param bo 是否可取消
     */
    public static void showDialog(Context context, String message,
                                  String sureText, String cancelText,
                                  DialogInterface.OnClickListener sureListener,
                                  DialogInterface.OnClickListener cancelListener, boolean bo){
        new AlertDialog.Builder(context).setMessage(message)
                .setPositiveButton(sureText,sureListener)
                .setNegativeButton(cancelText,cancelListener).show();
    }
    public static void showDialog(Context context, int message,
                                  int sureText, int cancelText,
                                  DialogInterface.OnClickListener sureListener,
                                  DialogInterface.OnClickListener cancelListener, boolean bo){
        new AlertDialog.Builder(context).setMessage(context.getString(message))
                .setPositiveButton(context.getString(sureText),sureListener)
                .setNegativeButton(context.getString(cancelText),cancelListener).show();
    }

    /**
     * 显示提示框
     * @param context 上下文对象
     * @param message 提示信息
     * @param sureText 确认按钮文本
     * @param cancelText 取消按钮文本
     * @param sureListener 确认监听器
     * @param cancelListener 取消监听器
     */
    public static void showDialog(Context context, String message,
                                  String sureText, String cancelText,
                                  DialogInterface.OnClickListener sureListener,
                                  DialogInterface.OnClickListener cancelListener){
        new AlertDialog.Builder(context).setMessage(message)
                .setPositiveButton(sureText,sureListener)
                .setNegativeButton(cancelText,cancelListener).show();
    }
    public static void showDialog(Context context, int message,
                                  int sureText, int cancelText,
                                  DialogInterface.OnClickListener sureListener,
                                  DialogInterface.OnClickListener cancelListener){
        new AlertDialog.Builder(context).setMessage(context.getString(message))
                .setPositiveButton(context.getString(sureText),sureListener)
                .setNegativeButton(context.getString(cancelText),cancelListener).show();
    }

    /**
     * 显示进度对话框
     * @param context 上下文对象
     * @param messageId 文本资源id
     * @return
     */
    public static ProgressDialog showProgressDialog(Context context,int messageId){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(messageId));
        progressDialog.show();
        return  progressDialog;
    }

}
