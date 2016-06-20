package com.wuxianedu.corelib.util;

import android.content.Context;
import android.content.res.TypedArray;

/**
 * 属性工具类
 * @author lungank
 */
public class AttrUtils {

    /**
     * 获取属性颜色值
     * @param context 上下文对象
     * @param attrs 属性数组
     * @param attrValue 属性值
     * @return 颜色值
     */
    public static int getValueOfColorAttr(Context context,int[] attrs,int attrValue){
        TypedArray a = context.obtainStyledAttributes(attrs);
        return a.getColor(attrValue, 0);
    }

}


