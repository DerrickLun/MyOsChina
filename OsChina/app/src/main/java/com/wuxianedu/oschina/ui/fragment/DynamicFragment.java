package com.wuxianedu.oschina.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wuxianedu.corelib.util.JSONParseUtils;
import com.wuxianedu.corelib.widget.RefreshLayout;
import com.wuxianedu.oschina.R;
import com.wuxianedu.oschina.adapter.DynamicAdapter;
import com.wuxianedu.oschina.base.BaseActivity;
import com.wuxianedu.oschina.base.BaseFragment;
import com.wuxianedu.oschina.bean.Dynamic;
import com.wuxianedu.oschina.network.RequestAPI;
import com.wuxianedu.oschina.network.RequestClient;
import com.wuxianedu.oschina.network.RequestConfig;
import com.wuxianedu.oschina.util.IntentConstants;
import com.wuxianedu.oschina.util.LoadType;
import com.wuxianedu.oschina.util.SnackbarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态Fragment
 * Created by lungank on 2016/5/12.
 */
public class DynamicFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener,RefreshLayout.OnLoadListener{

    private RefreshLayout refreshLayout;
    private ListView listView;
    private int page =1;
    private int userId;
    private DynamicAdapter adapter;

    public static Fragment newInstance(int userId){
        Fragment fragment = new DynamicFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IntentConstants.USER_ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_listview,container,false);
    }

    @Override
    public void init(View rootView) {
        refreshLayout = (RefreshLayout) rootView.findViewById(R.id.id_refresh);
        listView = (ListView) rootView.findViewById(R.id.id_lv);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dynamic eventVo = adapter.getItem(position);
            }
        });
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser && adapter==null && getActivity()!=null ){
            Bundle bundle = getArguments();
            userId = bundle.getInt(IntentConstants.USER_ID);
            loadData(LoadType.FIRST);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUserVisibleHint(getUserVisibleHint());
    }

    /**
     * 加载数据
     */
    private void loadData(final LoadType loadType){
        Map<String,String> requestParams = new HashMap<String,String>();
        requestParams.put("page",String.valueOf(page));
        final BaseActivity activity = ((BaseActivity)getActivity());

        RequestConfig config = new RequestConfig();
        config.setTipInfoLayout(tipInfoLayout)
                .setMainBody(refreshLayout)
                .setIsCover(false)
                .setIsLoading(adapter == null);
        new RequestClient(activity, config){
            @Override
            public void loadSuccess(String responseInfo) {
                List<Dynamic> list = JSONParseUtils.parseArray(responseInfo, Dynamic.class);
                if(list==null || list.isEmpty()){
                    return;
                }
                page++;

                switch (loadType){
                    case FIRST: //第一次加载
                        adapter = new DynamicAdapter(getActivity(),list, R.layout.item_event);
                        listView.setAdapter(adapter);
                        break;
                    case REFRESH: //下拉刷新
                        if(!list.isEmpty()){
                            //之前列表中第一条数据
                            Dynamic eventVo = adapter.getItem(0);
                            List<Dynamic> tempList = new ArrayList<>();
                            for (Dynamic vo : list){
                                if(vo.getId()<=eventVo.getId()){
                                    break;
                                }
                                tempList.add(vo);
                            }

                            if(!tempList.isEmpty()){
                                adapter.addAll(0, tempList);
                                SnackbarUtils.showMessage(activity, tipInfoLayout,
                                        "更新了" +tempList.size()+ "内容");
                            }else{
                                SnackbarUtils.showMessage(activity, tipInfoLayout,
                                        "暂无内容更新");
                            }

                        }else{
                            SnackbarUtils.showMessage(activity, tipInfoLayout,
                                    "暂无内容更新");
                        }

                        refreshLayout.setRefreshing(false);
                        break;
                    case PAGE: //上拉加载
                        adapter.addAll(list);
                        refreshLayout.setLoading(false);
                        break;
                }

            }

            @Override
            protected void loadFail() {
                switch (loadType){
                    case FIRST:
                        break;
                    case REFRESH:
                        refreshLayout.setRefreshing(false);
                        break;
                    case PAGE:
                        refreshLayout.setLoading(false);
                        break;
                }
            }
        }.get(RequestAPI.DYNAMIC + userId, requestParams);
    }

    /**
     * 上拉加载
     */
    @Override
    public void onPageLoad() {
        loadData(LoadType.PAGE);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page =1;
        loadData(LoadType.REFRESH);
    }

}
