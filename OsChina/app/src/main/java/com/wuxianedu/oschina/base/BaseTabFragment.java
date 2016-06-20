package com.wuxianedu.oschina.base;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuxianedu.oschina.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Tab
 * Created by lungank on 2016/4/12.
 */
public abstract class BaseTabFragment extends BaseFragment {

    protected List<Fragment> fragmentList;
    protected List<String> titleList;
    protected TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
    }

    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.core_tab_layout,container,false);
    }

    @Override
    public void init(View rootView) {
        initFragmentList(fragmentList, titleList);

        //关联viewpager和tablayout
        tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPagerAdapter(getFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    protected abstract void initFragmentList(List<Fragment> fragmentList, List<String> titleList);

    class ViewPagerAdapter extends FragmentPagerAdapter{

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
    }

}
