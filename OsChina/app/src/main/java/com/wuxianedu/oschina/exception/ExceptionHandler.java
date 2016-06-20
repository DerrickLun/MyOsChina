package com.wuxianedu.oschina.exception;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Looper;

import com.alibaba.fastjson.util.IOUtils;
import com.wuxianedu.corelib.CoreApplication;
import com.wuxianedu.corelib.util.AppUtils;
import com.wuxianedu.corelib.util.CoreUtils;
import com.wuxianedu.corelib.util.DateUtils;
import com.wuxianedu.corelib.util.DialogUtils;
import com.wuxianedu.corelib.util.L;
import com.wuxianedu.corelib.util.file.SDCardManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常处理
 * Created by sks on 2016/4/11.
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler{

    private static ExceptionHandler instance = null;
    private Context context;
    // 系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //用来存储设备信息和异常信息
    private Map<String , String> logInfo = new HashMap<>() ;

    private ExceptionHandler(){}

    public static ExceptionHandler getInstance(){
        if (instance == null) {
            synchronized (ExceptionHandler.class) {
                if (instance == null) {
                    instance = new ExceptionHandler();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化
     * @param context 上下文对象
     */
    public void init(Context context){
        this.context = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 处理 UncaughtException
     */
    public void uncaughtException(Thread thread , Throwable ex) {
        if(!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        }else{
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace() ;
            }
        }
    }

    /**
     * 自定义异常处理
     * @param ex 异常
     * @return true处理了该异常信息;否则返回false
     */
    public boolean handleException(final Throwable ex) {
        if(ex == null){
            return false ;
        }
        new Thread() {
            public void run() {
                Looper.prepare() ;
                DialogUtils.showDialog(context, "应用程序出错了", "发送错误报告", "退出应用",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //发送错误报告给服务器
                        }
                    },
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CoreUtils.exitApp(context);
                        }
                    }, false);
                Looper.loop() ;
            }
        }.start() ;

        // 获取设备参数信息
        getDeviceInfo(context) ;
        // 保存日志文件
        saveLogToFile(ex) ;
        return true ;
    }

    /**
     * 收集程序崩溃的设备信息
     * @param context 上下文对象
     */
    public void getDeviceInfo(Context context) {
        PackageInfo pi = AppUtils.getPackageInfo(context);
        if(pi != null) {
            String versionName = pi.versionName == null ? "null": pi.versionName ;
            String versionCode = pi.versionCode + "" ;
            logInfo.put("versionName" , versionName) ;
            logInfo.put("versionCode" , versionCode) ;
        }

        // 使用反射来收集设备信息.在Build类中包含各种设备信息,例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
        Field[] mFields = Build.class.getDeclaredFields() ;
        for(Field field : mFields) {
            try {
                // 通过设置Accessible属性为true,才能对私有变量进行访问
                field.setAccessible(true) ;
                logInfo.put(field.getName() , field.get("").toString()) ;
            }catch(IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace() ;
            }
        }
    }

    /**
     * 保存异常信息到文件中
     * @param ex 异常
     * @return 异常信息
     */
    private String saveLogToFile(Throwable ex) {
        String filePath = null;
        StringBuilder mStringBuffer = new StringBuilder() ;
        for(Map.Entry<String , String> entry : logInfo.entrySet()) {
            String key = entry.getKey() ;
            String value = entry.getValue() ;
            mStringBuffer.append(key).append("=").append(value).append("\r\n");
        }
        Writer mWriter = new StringWriter() ;
        PrintWriter mPrintWriter = new PrintWriter(mWriter) ;
        ex.printStackTrace(mPrintWriter) ;
        Throwable mThrowable = ex.getCause() ;
        while(mThrowable != null) {
            mThrowable.printStackTrace(mPrintWriter) ;
            mPrintWriter.append("\r\n") ;
            mThrowable = mThrowable.getCause() ;
        }
        mPrintWriter.close() ;
        String mResult = mWriter.toString() ;
        mStringBuffer.append(mResult) ;

        L.e(mStringBuffer.toString());
        String time = DateUtils.data2String(DateUtils.df4, new Date());
        String fileName = "error_" + time + ".log" ;
        FileOutputStream mFileOutputStream = null;
        if(SDCardManager.isExistSD()) {
            try {
                filePath = CoreApplication.LOG_DIR + fileName;
                L.e("异常文件路径：" + filePath);
                mFileOutputStream = new FileOutputStream(filePath) ;
                mFileOutputStream.write(mStringBuffer.toString().getBytes()) ;
                return fileName ;
            } catch(IOException e) {
                e.printStackTrace() ;
            } finally {
                IOUtils.close(mFileOutputStream);
            }
        }
        return filePath ;
    }

}
