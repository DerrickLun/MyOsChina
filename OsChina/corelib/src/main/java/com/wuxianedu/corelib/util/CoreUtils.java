package com.wuxianedu.corelib.util;

import android.app.Activity;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * 核心工具类
 * Created by lungank on 2016/4/9.
 */
public class CoreUtils {

    //Activity列表
    public static ArrayList<Activity> activityList = new ArrayList<>();

    /**
     * 添加Activity到列表中
     * @param activity 要加入的Activity
     */
    public static void addAppActivity(Activity activity){
        if(!activityList.contains(activity)){
            activityList.add(activity);
        }
    }

    /**
     * 从列表移除Activity
     * @param activity  要移除的Activity
     */
    public static void removeAppActivity(Activity activity){
        if(activityList.contains(activity)){
            activityList.remove(activity);
        }
    }

    /**
     * 退出应用程序
     * @param context 上下文对象
     */
    public static void exitApp(Context context){
        L.d("销毁Activity size:" + activityList.size());
        for (Activity ac : activityList) {
            if(!ac.isFinishing()){
                ac.finish();
            }
        }
        activityList.clear();
        //TODO 注销service
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 清空List集合
     * @param list Activity集合
     */
    public static void clearList(List<?> list){
        if(list!=null){
            list.clear();
        }
    }

}
