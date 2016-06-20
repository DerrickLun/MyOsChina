package com.wuxianedu.oschina.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wuxianedu.corelib.util.IntentUtils;
import com.wuxianedu.corelib.util.JSONParseUtils;
import com.wuxianedu.corelib.util.TextUtils;
import com.wuxianedu.corelib.widget.GridViewForScroll;
import com.wuxianedu.corelib.widget.ListViewForScroll;
import com.wuxianedu.oschina.R;
import com.wuxianedu.oschina.adapter.ProjectAdapter;
import com.wuxianedu.oschina.base.BaseActivity;
import com.wuxianedu.oschina.bean.Featured;
import com.wuxianedu.oschina.bean.Readme;
import com.wuxianedu.oschina.network.RequestAPI;
import com.wuxianedu.oschina.network.RequestClient;
import com.wuxianedu.oschina.network.RequestConfig;
import com.wuxianedu.oschina.util.IntentConstants;
import com.wuxianedu.oschina.util.SnackbarUtils;
import com.wuxianedu.oschina.util.TimeUtils;
import com.wuxianedu.oschina.util.TypefaceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lungank on 2016/5/12.
 * 项目详情页面
 */
public class ProjectDetailActivity extends BaseActivity {

    private Featured project;
    //项目名称
    private TextView projectName;
    //项目更新时间
    private TextView projectUpdateTime;
    //项目描述
    private TextView projectDescription;
    private TextView projectStarStared;
    private TextView projectStarText;
    private  TextView projectStarnum;
    private TextView projectWatchStared;
    private  TextView projectWatchText;
    private TextView projectWatchnum;
    private ListViewForScroll projectCodeListView;
    private GridViewForScroll gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_project);
        Bundle bundle = getIntent().getExtras();
        if (bundle!= null) {
            project = (Featured) bundle.getParcelable(IntentConstants.PROJECT_DETAIL);
            setPageTitle(project.getName());
            setPageSubtitleName(project.getOwner().getName());
            showVlaue();
        }
    }

    @Override
    protected void init() {
        projectName = (TextView) findViewById(R.id.project_name);
        projectUpdateTime = (TextView) findViewById(R.id.tv_updatetime);
        projectDescription = (TextView) findViewById(R.id.project_description);
        projectStarStared = (TextView) findViewById(R.id.project_star_stared);
        projectStarText = (TextView) findViewById(R.id.project_star_text);
        projectStarnum = (TextView) findViewById(R.id.project_starnum);
        projectWatchStared = (TextView) findViewById(R.id.project_watch_stared);
        projectWatchText = (TextView) findViewById(R.id.project_watch_text);
        projectWatchnum = (TextView) findViewById(R.id.project_watchnum);
        projectCodeListView = (ListViewForScroll) findViewById(R.id.project_code_listview);
        gridView= (GridViewForScroll) findViewById(R.id.gridview);
    }

    /**
     * 显示值
     */
    private void showVlaue(){
        initGridview();
        projectName.setText(project.getName());
        projectUpdateTime.setText(getUpdateTime());
        projectDescription.setText(project.getDescription());
        projectStarnum.setText(project.getStars_count() + "");
        projectWatchnum.setText(project.getWatches_count() + "");
        setStared(project.isStared());
        setWatched(project.isWatched());

        ArrayAdapter<String> projectCode = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        projectCode.add(getString(R.string.project_code_readme));
        projectCode.add(getString(R.string.project_code_codes));
        projectCode.add(getString(R.string.project_code_commits));
        projectCode.add(getString(R.string.project_code_issues));
        projectCodeListView.setAdapter(projectCode);
        projectCodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    getProjectReadMe();
                } else {
                    String message = parent.getAdapter().getItem(position).toString();
                    SnackbarUtils.showMessage(ProjectDetailActivity.this, tipInfoLayout,
                            message);
                }
            }
        });
    }

    /**
     * 初始化GridView
     */
    private void initGridview() {
        List<String> mData = new ArrayList<>();
        mData.add(getString(R.string.fa_clock_o) + " " + TimeUtils.friendlyFormat(this, project.getCreated_at()));
        mData.add(getString(R.string.sem_fork) + " " + project.getForks_count());
        String language = project.getLanguage();
        if (TextUtils.isEmpty(language)) {
            language = getString(R.string.no_point);
        }
        mData.add(getString(R.string.sem_tag) + " " + language);
        mData.add(getString(R.string.sem_user) + " " + project.getOwner().getName());
        gridView.setAdapter(new ProjectAdapter(this,mData,
                R.layout.item_gridview_activity_detail_project));
    }

    private void setStared(boolean stared) {
        int textRes;
        if (stared) {
            textRes = R.string.sem_star;
            projectStarText.setText("unstar");
        } else {
            textRes = R.string.sem_empty_star;
            projectStarText.setText("star");
        }
        TypefaceUtils.setIconText(projectStarStared, getString(textRes));
    }

    private void setWatched(boolean watched) {
        int textRes;
        if (watched) {
            textRes = R.string.sem_watch;
            projectWatchText.setText("unwatch");
        } else {
            textRes = R.string.sem_empty_watch;
            projectWatchText.setText("watch");
        }
        TypefaceUtils.setIconText(projectWatchStared, getString(textRes));
    }

    /**
     * 获取更新时间
     * @return
     */
    private String getUpdateTime() {
        if (project.getLast_push_at() == null) {
            return getString(R.string.update_head_title, TimeUtils.friendlyFormat(this, project.getCreated_at()));
        } else {
            return getString(R.string.update_head_title, TimeUtils.friendlyFormat(this, project.getLast_push_at()));
        }
    }

    /**
     * 加载ReadMe
     */
    private void getProjectReadMe(){
        RequestConfig config = new RequestConfig();
        config.setTipInfoLayout(getTipInfoLayout())
                .setMainBody(getMainBody())
                .setIsCover(false)
                .setShowProgressDialog(true);
        new RequestClient(ProjectDetailActivity.this, config){
            @Override
            public void loadSuccess(String responseInfo) {
                if(TextUtils.isEmpty(responseInfo)){
                    SnackbarUtils.showMessage(ProjectDetailActivity.this, getTipInfoLayout(),
                            R.string.no_readme_hint);
                    return;
                }else{
                    Readme readmeBean= JSONParseUtils.parseObject(responseInfo, Readme.class);
                    if(TextUtils.isEmpty(readmeBean.getContent())){
                        SnackbarUtils.showMessage(ProjectDetailActivity.this, getTipInfoLayout(),
                                R.string.no_readme_hint);
                        return;
                    }
                    Intent intent = new Intent(ProjectDetailActivity.this,WapActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString(IntentConstants.TITLE,project.getName());
                    bundle.putString(IntentConstants.TITLE_SUB,"README.md");
                    bundle.putString(IntentConstants.WAP_CONTENT,readmeBean.getContent());
                    intent.putExtras(bundle);
                    IntentUtils.startActivityNetWork(ProjectDetailActivity.this, intent);
                }
            }

            @Override
            public void loadFail() {}
        }.get(RequestAPI.getReadmeURL(project.getId()));
    }

}

