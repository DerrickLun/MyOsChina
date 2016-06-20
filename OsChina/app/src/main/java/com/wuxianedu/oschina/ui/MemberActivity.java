package com.wuxianedu.oschina.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wuxianedu.corelib.image.ImageLoaderManager;
import com.wuxianedu.corelib.util.DialogUtils;
import com.wuxianedu.corelib.util.T;
import com.wuxianedu.corelib.util.TextUtils;
import com.wuxianedu.corelib.widget.ListViewForScroll;
import com.wuxianedu.oschina.OsChinaApplication;
import com.wuxianedu.oschina.R;
import com.wuxianedu.oschina.base.BaseActivity;
import com.wuxianedu.oschina.bean.Member;
import com.wuxianedu.oschina.util.Constant;
import com.wuxianedu.oschina.util.IntentConstants;
import com.wuxianedu.oschina.widget.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员页面
 * Created by lungank on 2016/5/12.
 */
public class MemberActivity extends BaseActivity implements View.OnClickListener{

    private RoundImageView portrait;
    private TextView userNameView;
    private TextView description;
    private ListViewForScroll listview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        setPageTitle("个人中心");
    }

    @Override
    public void init() {
        portrait = findView(R.id.portrait);
        userNameView = findView(R.id.username);
        description = findView(R.id.description);
        listview = findView(R.id.listview);
        findViewById(R.id.bt_logout).setOnClickListener(this);
        loadMemberInf();
    }

    /**
     * 加载会员信息
     */
    public void loadMemberInf(){
        Member member = OsChinaApplication.instance.getMember();
        userNameView.setText(member.getName());
        if (!TextUtils.isEmpty(member.getBio())) {
            description.setText(member.getBio());
        } else {
            description.setText(getString(R.string.no_content));
        }
        ImageLoaderManager.getInstance().loadImage(member.getNew_portrait(), portrait);

        List<String> mData = new ArrayList<String>();
        String email = member.getEmail();
        if (TextUtils.isEmpty(email)) {
            email = getString(R.string.no_content);
        }
        String weibo = member.getWeibo();
        if (TextUtils.isEmpty(weibo)) {
            weibo = getString(R.string.no_content);
        }
        String blog = member.getBlog();
        if (TextUtils.isEmpty(blog)) {
            blog = getString(R.string.no_content);
        }

        mData.add(getString(R.string.email) + email);
        mData.add(getString(R.string.weibo) + weibo);
        mData.add(getString(R.string.blog) + blog);
        mData.add(getString(R.string.followersTitle) + member.getFollow().getFollowers());
        mData.add(getString(R.string.followingTitle) + member.getFollow().getFollowing());
        mData.add(getString(R.string.watchedTitle) + member.getFollow().getWatched());
        mData.add(getString(R.string.starredTitle) + member.getFollow().getStarred());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.item_listview_activity_member, mData);
        listview.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_logout:
                DialogUtils.showDialog(MemberActivity.this, R.string.sure_logout, R.string.ok,
                        R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final boolean flag = OsChinaApplication.instance.logout();
                                if (flag) {
                                    //发送广播，更新主视图
                                    Intent memberIntent = new Intent();
                                    Bundle memberBundle = new Bundle();
                                    memberBundle.putInt(IntentConstants.TYPE, Constant.TYPE_LOGOUT);
                                    memberIntent.putExtras(memberBundle);
                                    memberIntent.setAction(IntentConstants.LOGIN_BROADCAST_ACTION);
                                    LocalBroadcastManager.getInstance(MemberActivity.this).sendBroadcast(memberIntent);
                                    T.showShort(MemberActivity.this, "注销成功");
                                    finish();
                                }
                            }
                        }, null);
                break;
        }
    }

}
