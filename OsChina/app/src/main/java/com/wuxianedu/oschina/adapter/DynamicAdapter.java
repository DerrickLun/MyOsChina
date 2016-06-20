package com.wuxianedu.oschina.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wuxianedu.corelib.adapter.CusAdapter;
import com.wuxianedu.corelib.adapter.ViewHolder;
import com.wuxianedu.corelib.image.ImageLoaderManager;
import com.wuxianedu.corelib.util.TextUtils;
import com.wuxianedu.oschina.R;
import com.wuxianedu.oschina.bean.Dynamic;
import com.wuxianedu.oschina.util.DynamicUtils;
import com.wuxianedu.oschina.util.TimeUtils;
import com.wuxianedu.oschina.widget.RoundImageView;

import java.util.List;

/**
 * 动态适配器
 * Created by lungank on 2016/5/12.
 */
public class DynamicAdapter extends CusAdapter<Dynamic> {

    public DynamicAdapter(Context context, List<Dynamic> list, int itemLayoutRes) {
        super(context, list, itemLayoutRes);
    }

    @Override
    public View getCustomView(int position, View itemView) {
        //头像
        RoundImageView eventPortraitView = ViewHolder.get(itemView, R.id.event_portrait);
        //标题
        TextView titleView = ViewHolder.get(itemView, R.id.event_title);
        //描述
        TextView descriptionView = ViewHolder.get(itemView, R.id.event_description);
        //所有提交列表
        LinearLayout allCommintsListView = ViewHolder.get(itemView, R.id.event_all_commits_list);
        //动态日期
        TextView eventDateView = ViewHolder.get(itemView, R.id.event_date);

        Dynamic event = getItem(position);
        String title = DynamicUtils.parseDynamicTitle(context, event
                .getAuthor().getName(), event.getProject().getOwner().getName()
                + "/" + event.getProject().getName(), event);
        titleView.setText(title);

        if (event.getUpdated_at()!=null) {
            String updateTime = TimeUtils.friendlyFormat(context, event.getUpdated_at());
            if (!TextUtils.isEmpty(updateTime)) {
                eventDateView.setText(updateTime);
            }
        }

        allCommintsListView.setVisibility(View.GONE);
        allCommintsListView.removeAllViews();
        if (event.getData() != null) {
            List<Dynamic.DataEntity.CommitsEntity> commits = event.getData().getCommits();
            if (commits != null && commits.size() > 0) {
                showCommitInfo(allCommintsListView, commits);
                allCommintsListView.setVisibility(View.VISIBLE);
            }
        }

        String portraitURL = event.getAuthor().getNew_portrait();
        if (portraitURL.endsWith("portrait.gif")) {
            eventPortraitView.setImageResource(R.mipmap.mini_avatar);
        } else {
            ImageLoaderManager.getInstance().loadImage(portraitURL,eventPortraitView);
        }
        return itemView;
    }

    /**
     * 显示提交信息
     * @param layout
     * @param commits
     */
    private void showCommitInfo(LinearLayout layout, List<Dynamic.DataEntity.CommitsEntity> commits) {
        if (commits.size() >= 2) {
            addCommitItem(layout, commits.get(0));
            addCommitItem(layout, commits.get(1));
        } else {
            for (Dynamic.DataEntity.CommitsEntity commit : commits) {
                addCommitItem(layout, commit);
            }
        }
    }

    /**
     * 添加commit项
     * @param layout
     * @param commit
     */
    private void addCommitItem(LinearLayout layout, Dynamic.DataEntity.CommitsEntity commit) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event_commits, null);
        ((TextView) view.findViewById(R.id.event_commits_listitem_commitid))
                .setText(commit.getId());
        ((TextView) view.findViewById(R.id.event_commits_listitem_username))
                .setText(commit.getAuthor().getName());
        ((TextView) view.findViewById(R.id.event_commits_listitem_message))
                .setText(commit.getMessage());
        layout.addView(view);
    }

}
