package com.wuxianedu.oschina.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuxianedu.corelib.util.JSONParseUtils;
import com.wuxianedu.corelib.util.SPUtils;
import com.wuxianedu.corelib.widget.TipInfoLayout;
import com.wuxianedu.oschina.OsChinaApplication;
import com.wuxianedu.oschina.R;
import com.wuxianedu.oschina.adapter.LanguageAdapter;
import com.wuxianedu.oschina.base.BaseFragment;
import com.wuxianedu.oschina.bean.Language;
import com.wuxianedu.oschina.network.RequestAPI;
import com.wuxianedu.oschina.network.RequestClient;
import com.wuxianedu.oschina.network.RequestConfig;
import com.wuxianedu.oschina.util.Constant;
import com.wuxianedu.oschina.util.LoadType;

import java.util.List;

/**
 * 语言块
 * Created by lungank on 2016/5/11.
 */
public class LanguageFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private LanguageAdapter adapter;
    private Context context;

    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_language,container,false);
    }

    @Override
    public void init(View rootView) {
        tipInfoLayout = (TipInfoLayout) rootView.findViewById(R.id.tip_layout);

        context = getActivity();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.id_recycler);

        boolean cardFlag = (boolean) SPUtils.get(context, Constant.CARD_KEY, true);
        if (cardFlag) { //卡片布局
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        } else { //线性布局
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        loadData(LoadType.FIRST);
    }

    /**
     * 加载数据
     */
    private void loadData(final LoadType loadType){
        RequestConfig config = new RequestConfig();
        config.setIsLoading(adapter == null)
                .setTipInfoLayout(tipInfoLayout)
                .setMainBody(recyclerView);
        new RequestClient(context, config){
            @Override
            public void loadSuccess(String responseInfo) {
                List<Language> list = JSONParseUtils.parseArray(responseInfo, Language.class);
                switch (loadType){
                    case FIRST: //第一次加载
                        adapter = new LanguageAdapter(context, list);
                        recyclerView.setAdapter(adapter);
                        break;
                }
            }

    @Override
    public void loadFail() {}
        }.get(RequestAPI.LANGUAGE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(OsChinaApplication.carLayout) {
            Boolean cardFlag = (Boolean) SPUtils.get(context, "card_key", true);
            assert cardFlag != null;
            if (cardFlag) {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            } else {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
            OsChinaApplication.carLayout = false;
        }
    }
}
