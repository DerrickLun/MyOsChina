package com.wuxianedu.corelib.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * App工具类
 * Created by lungank on 2016/4/9.
 */
public class AppUtils {

    /**
     * 获取应用程序包信息(包含版本名称和版本号);
     * @param context 上下文对象
     * @return 当前应用的版本名称
     */
    public static PackageInfo getPackageInfo(Context context) {
        try {
            //packageInfo.versionName、packageInfo.AttrUtils
            PackageManager packageManager = context.getPackageManager();
            return packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

//    /**
//     * 检查某个应用是否安装
//     * @param context 上下文对象
//     * @param packageName 包名
//     * @return 是否安装
//     */
//    public static boolean checkAPP(Context context, String packageName) {
//        if (packageName == null || "".equals(packageName))
//            return false;
//        try {
//            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
//                PackageManager.GET_UNINSTALLED_PACKAGES);
//            return true;
//        } catch (PackageManager.NameNotFoundException e) {
//            return false;
//        }
//    }

    /**
     * 获取应用程序名称
     * @param context 上下文对象
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
