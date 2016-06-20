package com.wuxianedu.corelib.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * 屏幕相关工具类
 * Created by lungank on 2016/4/9.
 */
public class ScreenUtils {

    /**
     * 获取分辨率
     * @param context 上下文对象
     * @return 分辨率数组（0：X,1:Y）
     */
    private static int[] getScreen(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int[] display = new int[2];
        display[0] = outMetrics.widthPixels;
        display[1] = outMetrics.heightPixels;
        return display;
    }

    /**
     * 获取状态栏的高度
     * @param context  上下文对象
     * @return 状态栏高度
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     * @param activity 当前页面
     * @return 页面截图
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreen(activity)[0];
        int height = getScreen(activity)[1];
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     * @param activity 当前页面
     * @return 页面截图
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreen(activity)[0];
        int height = getScreen(activity)[1];
        Bitmap bp;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 获取View在屏幕的坐标
     * @param view 视图
     * @return 坐标数组（0：X,1:Y）
     */
    public static int[] getViewXYonScreen(View view){
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location;
    }

}
