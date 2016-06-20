package com.wuxianedu.corelib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 扩展GridView,解决与ScrollView冲突不能展开问题
 * @author lungank
 */
public class GridViewForScroll extends GridView{

    public GridViewForScroll(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }

}