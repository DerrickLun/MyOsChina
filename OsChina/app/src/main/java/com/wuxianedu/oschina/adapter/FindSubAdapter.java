package com.wuxianedu.oschina.adapter;

/**
 * Created by lungank on 2016/5/12.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wuxianedu.corelib.adapter.CusAdapter;
import com.wuxianedu.corelib.adapter.ViewHolder;
import com.wuxianedu.corelib.image.ImageLoaderManager;
import com.wuxianedu.corelib.util.TextUtils;
import com.wuxianedu.oschina.R;
import com.wuxianedu.oschina.bean.Featured;
import com.wuxianedu.oschina.bean.Owner;
import com.wuxianedu.oschina.util.IntentConstants;
import com.wuxianedu.oschina.util.TypefaceUtils;
import com.wuxianedu.oschina.widget.RoundImageView;

import java.util.List;

/**
 * 发现Adapter
 */
public class FindSubAdapter extends CusAdapter<Featured> {

    public FindSubAdapter(Context context, List<Featured> list, int itemLayoutRes) {
        super(context, list, itemLayoutRes);
    }

    @Override
    public View getCustomView(final int position, View itemView) {
        //头像
        RoundImageView portraitView = ViewHolder.get(itemView, R.id.iv_portrait);
        //标题
        TextView titleView = ViewHolder.get(itemView, R.id.tv_title);
        //描述
        TextView descriptionView = ViewHolder.get(itemView, R.id.tv_description);
        //浏览次数
        TextView watchView = ViewHolder.get(itemView, R.id.tv_watch);
        //星级
        TextView starView = ViewHolder.get(itemView, R.id.tv_star);
        //分支
        TextView forkView = ViewHolder.get(itemView, R.id.tv_fork);
        //语言
        TextView languageView = ViewHolder.get(itemView, R.id.tv_language);

        final Featured project = getItem(position);
        //标题
        Owner ownerVo = project.getOwner();
        String titleName ="";
        if(ownerVo!=null){
            String userName = ownerVo.getUsername();;
            titleName += userName;
        }
        if(!TextUtils.isEmpty(titleName)){
            titleName +="/";
        }
        titleName+=project.getName();
        titleView.setText(titleName);

        //描述
        String description = project.getDescription();
        if (TextUtils.isEmpty(description)) {
            description = context.getString(R.string.no_description_hint);
        }
        descriptionView.setText(description);

        TypefaceUtils.setIconText(watchView, context.getString(R.string.sem_watch) + " " + project.getWatches_count());
        TypefaceUtils.setIconText(starView, context.getString(R.string.sem_star) + " " + project.getStars_count());
        TypefaceUtils.setIconText(forkView, context.getString(R.string.sem_fork) + " " + project.getForks_count());

        //语言
        String language = project.getLanguage();
        if (TextUtils.isEmpty(language)) {
            languageView.setVisibility(View.GONE);
        } else {
            TypefaceUtils.setIconText(languageView, context.getString(R.string.sem_tag) + " " + project.getLanguage());
        }

        //头像
        String portraitURL = project.getOwner().getNew_portrait();
        if (portraitURL.endsWith("portrait.gif")) {
            portraitView.setImageResource(R.mipmap.mini_avatar);
        } else {
            ImageLoaderManager.getInstance().loadImage(portraitURL,portraitView,
                    R.mipmap.mini_avatar,R.mipmap.mini_avatar);
        }
        portraitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(IntentConstants.USER_ID, project.getOwner().getId());
                bundle.putString(IntentConstants.USER_NAME, project.getOwner().getName());
                intent.putExtras(bundle);
//                IntentUtils.startActivityNetWork(context, UserActivity.class, intent);
            }
        });
        return itemView;
    }

}

