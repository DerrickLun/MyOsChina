package com.wuxianedu.oschina.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wuxianedu.corelib.util.IntentUtils;
import com.wuxianedu.corelib.util.JSONParseUtils;
import com.wuxianedu.corelib.widget.RefreshLayout;
import com.wuxianedu.corelib.widget.TipInfoLayout;
import com.wuxianedu.oschina.R;
import com.wuxianedu.oschina.adapter.FindSubAdapter;
import com.wuxianedu.oschina.base.BaseActivity;
import com.wuxianedu.oschina.base.BaseFragment;
import com.wuxianedu.oschina.bean.Featured;
import com.wuxianedu.oschina.network.RequestAPI;
import com.wuxianedu.oschina.network.RequestClient;
import com.wuxianedu.oschina.network.RequestConfig;
import com.wuxianedu.oschina.ui.ProjectDetailActivity;
import com.wuxianedu.oschina.util.Constant;
import com.wuxianedu.oschina.util.IntentConstants;
import com.wuxianedu.oschina.util.LoadType;
import com.wuxianedu.oschina.util.SnackbarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发现Fragment
 * Created by lungank on 2016/5/12.
 */
public class FindSubFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener,RefreshLayout.OnLoadListener {

    private static final String TYPE = "type";
    private RefreshLayout refreshLayout;
    private ListView listView;
    private int page = 1;
    private int type;
    private int userId;
    private FindSubAdapter adapter;

    /**
     * 创建Fragment实例
     * @param type
     * @return
     */
    public static Fragment newInstance(int type) {
        Fragment fragment = new FindSubFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment newInstance(int type, int userId) {
        Fragment fragment = new FindSubFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        bundle.putInt(IntentConstants.USER_ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_listview, container, false);
    }

    /**
     * 初始化
     * @param rootView
     */
    @Override
    public void init(View rootView) {
        refreshLayout = (RefreshLayout) rootView.findViewById(R.id.id_refresh);
        tipInfoLayout = (TipInfoLayout) rootView.findViewById(R.id.tip_info_find);

        listView = (ListView) rootView.findViewById(R.id.id_lv);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Featured featured = adapter.getItem(position);
                Intent intent = new Intent(getActivity(), ProjectDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(IntentConstants.PROJECT_DETAIL, featured);
                intent.putExtras(bundle);
                IntentUtils.startActivity(getActivity(), intent);
            }
        });
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
        Bundle bundle = getArguments();
        type = bundle.getInt(TYPE, 0);
        userId = bundle.getInt(IntentConstants.USER_ID);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && adapter == null && getActivity() != null) {
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
    private void loadData(final LoadType loadType) {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("page", String.valueOf(page));
        final BaseActivity activity = ((BaseActivity) getActivity());
        RequestConfig config = new RequestConfig();
        switch (loadType) {
            case FIRST: //第一次加载
                config.setIsCover(true);
                break;
            case REFRESH: //下拉刷新
                config.setIsCover(false);
                break;
            case PAGE: //上拉加载
                config.setIsCover(false);
                break;
        }
        config.setTipInfoLayout(tipInfoLayout).setMainBody(refreshLayout)
                .setIsLoading(adapter == null);

        new RequestClient(activity, config) {
            @Override
            protected void loadSuccess(String result) {
                List<Featured> list = JSONParseUtils.parseArray(result, Featured.class);
                if (list == null || list.isEmpty()) {
                    return;
                }
                page++;

                switch (loadType) {
                    case FIRST: //第一次加载
                        adapter = new FindSubAdapter(activity, list, R.layout.item_project);
                        listView.setAdapter(adapter);
                        break;
                    case REFRESH: //下拉刷新
                        if (!list.isEmpty()) {
                            //之前列表中第一条数据
                            Featured featured = adapter.getItem(0);
                            List<Featured> tempList = new ArrayList<>();
                            for (Featured vo : list) {
                                if (vo.getId() <= featured.getId()) {
                                    break;
                                }
                                tempList.add(vo);
                            }

                            if (!tempList.isEmpty()) {
                                adapter.addAll(0, tempList);
                                SnackbarUtils.showMessage(activity, tipInfoLayout,
                                        "更新了" + tempList.size() + "内容");
                            } else {
                                SnackbarUtils.showMessage(activity, tipInfoLayout,
                                        "暂无内容更新");
                            }
                        } else {
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
            public void loadFail() {
                switch (loadType) {
                    case FIRST: //第一次加载
                        break;
                    case REFRESH: //下拉刷新
                        refreshLayout.setRefreshing(false);
                        break;
                    case PAGE: //上拉加载
                        refreshLayout.setLoading(false);
                        break;
                }
            }
        }.get(getUrl(), requestParams);
    }

    /**
     * 获取请求地址
     * @return 请求地址
     */
    private String getUrl() {
        String url;
        switch (type) {
            case 0:
                url = RequestAPI.FEATURED;
                break;
            case 1:
                url = RequestAPI.POPULAR;
                break;
            case 2:
                url = RequestAPI.LATEST;
                break;
            case Constant.PROJECT:
                url = "user/" + userId + "/projects";
                break;
            case Constant.STAR:
                url = "user/" + userId + "/stared_projects";
                break;
            case Constant.WATCH:
                url = "user/" + userId + "/watched_projects";
                break;
            default:
                url = RequestAPI.FEATURED;
        }
        return url;
    }

    /**
     * 上拉加载
     */
    @Override
    public void onPageLoad() {
        //如果返回不是20条,表示已结到底
        if (adapter.getCount() > 0 && adapter.getCount() % 20 == 0) {
            loadData(LoadType.PAGE);
        } else {
            refreshLayout.setLoading(false);
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        loadData(LoadType.REFRESH);
    }

}
