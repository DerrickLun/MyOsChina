package com.wuxianedu.corelib.adapter;

import android.util.SparseArray;
import android.view.View;

/**
 * ViewHolder类
 * @author lungank
 */
public class ViewHolder {

    public static <T extends View> T get(View view,int id){
        SparseArray<View> viewHolder = (SparseArray<View>)view.getTag();
        if(view.getTag()==null){
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if(childView==null){
            childView = view.findViewById(id);
            viewHolder.put(id,childView);
        }
        return (T)childView;
    }

}