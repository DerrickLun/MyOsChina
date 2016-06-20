package com.wuxianedu.corelib.util;

import android.util.Log;

import com.wuxianedu.corelib.CoreApplication;

/**
 * 日志工具类
 * @author lungank
 */
public class L {

    //标签
    private static final String TAG = "main";

    //Log输出所在类
    private static String className;

    //Log输出所在方法
    private static String methodName;

    //Log输出所行号
    private static int lineNumber;

    /**
     * 获取输出所在位置的信息className methodName lineNumber
     * @param elements 异常信息数组
     */
    private static void getDetail(StackTraceElement[] elements){
        className = elements[1].getFileName().split("\\.")[0];
        methodName = elements[1].getMethodName();
        lineNumber = elements[1].getLineNumber();
    }

    /**
     * 创建Log输出的基本信息
     * @param log 要输出的日志内容
     * @return 带有日志信息日志内容
     */
    private static String createLog(String log){
        StringBuilder buffer = new StringBuilder();
        buffer.append("[");
        buffer.append(className);
        buffer.append(".java ");
        buffer.append(methodName);
        buffer.append("()");
        buffer.append(" line:");
        buffer.append(lineNumber);
        buffer.append("] ");
        buffer.append(log);
        return buffer.toString();
    }

    public static void v(String message){
        if (CoreApplication.IS_DEBUG) {
            getDetail(new Throwable().getStackTrace());
            Log.v(TAG, createLog(message));
        }
    }

    public static void i(String message){
        if (CoreApplication.IS_DEBUG) {
            getDetail(new Throwable().getStackTrace());
            Log.i(TAG, createLog(message));
        }
    }

    public static void d(String message){
        if (CoreApplication.IS_DEBUG) {
            getDetail(new Throwable().getStackTrace());
            Log.d(TAG, createLog(message));
        }
    }

    public static void w(String message){
        if (CoreApplication.IS_DEBUG) {
            getDetail(new Throwable().getStackTrace());
            Log.w(TAG, createLog(message));
        }
    }

    public static void e(String message){
        if (CoreApplication.IS_DEBUG) {
            getDetail(new Throwable().getStackTrace());
            Log.e(TAG, createLog(message));
        }
    }

    //自定义TAG
    public static void v(String tag, String message){
        if (CoreApplication.IS_DEBUG) {
            getDetail(new Throwable().getStackTrace());
            Log.v(tag, createLog(message));
        }
    }

    public static void i(String tag, String message){
        if (CoreApplication.IS_DEBUG) {
            getDetail(new Throwable().getStackTrace());
            Log.i(tag, createLog(message));
        }
    }

    public static void d(String tag, String message){
        if (CoreApplication.IS_DEBUG) {
            getDetail(new Throwable().getStackTrace());
            Log.d(tag, createLog(message));
        }
    }

    public static void w(String tag, String message){
        if (CoreApplication.IS_DEBUG) {
            getDetail(new Throwable().getStackTrace());
            Log.w(tag, createLog(message));
        }
    }

    public static void e(String tag, String message){
        if (CoreApplication.IS_DEBUG) {
            getDetail(new Throwable().getStackTrace());
            Log.e(tag, createLog(message));
        }
    }

}
