package com.wuxianedu.oschina.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.wuxianedu.oschina.R;
import com.wuxianedu.oschina.base.BaseTabFragment;
import com.wuxianedu.oschina.util.Constant;
import com.wuxianedu.oschina.util.IntentConstant;

import java.util.List;

/**
 * 用户页面
 * Created by lungank on 2016/5/12.
 */
public class UserFragment extends BaseTabFragment {

    //用户ID
    private int userId;

    /**
     * 创建Fragment实例
     * @param userId
     * @return
     */
    public static Fragment newInstance(int userId){
        Fragment fragment = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IntentConstant.USER_ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    protected void initFragmentList(List<Fragment> fragmentList, List<String> titleList) {
        Bundle bundle = getArguments();
        userId = bundle.getInt(IntentConstant.USER_ID);

        titleList.add(getString(R.string.dynamic));
        fragmentList.add(DynamicFragment.newInstance(userId));

        titleList.add(getString(R.string.project));
        fragmentList.add(FindSubFragment.newInstance(Constant.PROJECT, userId));

        titleList.add(getString(R.string.Star));
        fragmentList.add(FindSubFragment.newInstance(Constant.STAR, userId));

        titleList.add(getString(R.string.Watch));
        fragmentList.add(FindSubFragment.newInstance(Constant.WATCH, userId));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            int position = tabLayout.getSelectedTabPosition();
            fragmentList.get(position).setUserVisibleHint(true);
        }
    }

}

