package com.wuxianedu.oschina.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wuxianedu.corelib.adapter.CusAdapter;
import com.wuxianedu.corelib.adapter.ViewHolder;
import com.wuxianedu.oschina.R;
import com.wuxianedu.oschina.util.TypefaceUtils;

import java.util.List;

/**
 * 项目详情Adapter
 * Created by lungank on 2016/5/12.
 */
public class ProjectAdapter extends CusAdapter<String> {

    public ProjectAdapter(Context context, List<String> list, int itemLayoutRes) {
        super(context, list, itemLayoutRes);
    }

    @Override
    public View getCustomView(int position, View itemView) {
        TextView textView = ViewHolder.get(itemView, R.id.textview);
        TypefaceUtils.setIconText(textView, getItem(position));
        return itemView;
    }

}
