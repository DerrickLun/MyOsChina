package com.wuxianedu.oschina.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.wuxianedu.oschina.R;
import com.wuxianedu.oschina.base.BaseTabFragment;

import java.util.List;

/**
 * 发现Fragment
 * Created by lungank on 2016/4/13.
 */
public class FindFragment extends BaseTabFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initFragmentList(List<Fragment> fragmentList, List<String> titleList) {
        titleList.add(getResources().getString(R.string.recommend_proj));
        titleList.add(getResources().getString(R.string.hot_proj));
        titleList.add(getResources().getString(R.string.recent_update));

        fragmentList.add(FindSubFragment.newInstance(0));
        fragmentList.add(FindSubFragment.newInstance(1));
        fragmentList.add(FindSubFragment.newInstance(2));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            int position = tabLayout.getSelectedTabPosition();
            fragmentList.get(position).setUserVisibleHint(true);
        }
    }

}
