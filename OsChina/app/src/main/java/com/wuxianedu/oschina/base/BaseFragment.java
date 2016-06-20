package com.wuxianedu.oschina.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuxianedu.corelib.widget.TipInfoLayout;

/**
 * Fragment基类
 * @author lungank
 */
public abstract class BaseFragment extends Fragment {

    protected TipInfoLayout tipInfoLayout;
    private View rootView;
    protected Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //解决getActivity()空指针
        this.mActivity = activity;
    }


    /**
     * onCreateView返回的就是fragment要显示的view。
     * @param inflater 布局过滤器
     * @param container 父容器
     * @param savedInstanceState 缓存的数据包
     * @return Fragment视图
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView==null){
            rootView = getRootView(inflater,container,savedInstanceState);
//            tipInfoLayout = (TipInfoLayout)rootView.findViewById(R.id.fl_parent_id);
            init(rootView);
        }else{
            ViewGroup p = (ViewGroup) rootView.getParent();
            if (p != null) {
                p.removeAllViewsInLayout();
            }
        }
        return rootView;
    }

    public abstract View getRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void init(View rootView);

}
