package com.wuxianedu.corelib.network;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.wuxianedu.corelib.util.L;

/**
 * 网络工具类
 * Created by lungank on 2016/4/9.
 */
public class NetWorkUtils {

    //是否有网络，true有网络，false无网络
    public static boolean NETWORK = false;

    /**
     * 初始化网络状态
     * @param context 上下文对象
     */
    public static void initNetStatus(Context context){
        ConnectivityManager connectivityManager =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (null != networkInfo && networkInfo.isConnected()) {
            NETWORK=true;
            L.e("当前网络名称：" + networkInfo.getTypeName());
        } else {
            NETWORK=false;
            L.e("没有可用网络");
        }
    }

    /**
     * 判断是否是wifi连接
     * @param context 上下文对象
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 打开网络设置界面
     * @param activity 当前页面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

}
