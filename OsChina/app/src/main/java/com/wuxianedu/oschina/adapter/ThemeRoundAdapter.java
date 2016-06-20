package com.wuxianedu.oschina.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.wuxianedu.oschina.R;

import java.util.List;

/**
 * 更换主题对话框中主题圆框列表的适配器
 * Created by lungank on 2016/5/10.
 */
public class ThemeRoundAdapter extends BaseAdapter{

    private Context context;
    private List<Integer> colorDrawableList;
    private int position;

    public ThemeRoundAdapter (Context context,List<Integer> themeList,int position){
        this.context = context;
        this.colorDrawableList = themeList;
        this.position = position;
    }

    @Override
    public int getCount() {
        return colorDrawableList.size();
    }

    @Override
    public Object getItem(int position) {
        return colorDrawableList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_change_theme,null);
            vh.bgIV = (ImageView) convertView.findViewById(R.id.img_bg);
            vh.selIV = (ImageView) convertView.findViewById(R.id.img_sel);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.bgIV.setImageResource(colorDrawableList.get(position));
        if (this.position == position){
            vh.selIV.setImageResource(R.mipmap.ic_done_white);
        }
        return convertView;
    }

    class ViewHolder{
        ImageView bgIV,selIV;
    }

}
