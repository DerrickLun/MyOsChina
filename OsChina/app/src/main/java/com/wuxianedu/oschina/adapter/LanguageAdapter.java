package com.wuxianedu.oschina.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wuxianedu.oschina.R;
import com.wuxianedu.oschina.bean.Language;

import java.util.List;

/**
 * 语言Fragment的适配器
 * Created by lungank on 2016/5/12.
 */
public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>{

    private Context context;
    private List<Language> list;

    public LanguageAdapter(Context context, List<Language> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public LanguageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new LanguageViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_fragment_language, viewGroup,false));
    }

    @Override
    public void onBindViewHolder(LanguageViewHolder languageViewHolder, int position) {
        Language languageVo = list.get(position);
        languageViewHolder.languageName.setText(languageVo.getName());
        String numStr = context.getString(R.string.theme_num,languageVo.getProjects_count());
        languageViewHolder.languageNum.setText(numStr);
    }

    class LanguageViewHolder extends RecyclerView.ViewHolder{
        TextView languageName;
        TextView languageNum;

        public LanguageViewHolder(View itemView) {
            super(itemView);
            languageName = (TextView) itemView.findViewById(R.id.id_name);
            languageNum = (TextView) itemView.findViewById(R.id.id_num);
        }
    }

}
