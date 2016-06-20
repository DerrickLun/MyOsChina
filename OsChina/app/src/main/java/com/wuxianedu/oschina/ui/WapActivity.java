package com.wuxianedu.oschina.ui;

import android.os.Bundle;

import com.wuxianedu.corelib.util.TextUtils;
import com.wuxianedu.oschina.R;
import com.wuxianedu.oschina.base.BaseActivity;
import com.wuxianedu.oschina.ui.fragment.WapFragment;
import com.wuxianedu.oschina.util.IntentConstants;

/**
 * 网页显示页面
 * Created by lungank on 2016/5/12.
 */
public class WapActivity extends BaseActivity {

    private String title,titleSub,content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_framelayout);

        if(savedInstanceState==null){
            savedInstanceState = getIntent().getExtras();
        }
        title=savedInstanceState.getString(IntentConstants.TITLE);
        titleSub=savedInstanceState.getString(IntentConstants.TITLE_SUB);
        content=savedInstanceState.getString(IntentConstants.WAP_CONTENT);

        mainBody.setBackgroundResource(android.R.color.white);
        WapFragment fragment = new WapFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentConstants.WAP_CONTENT, content);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_id, fragment).commit();

        setPageTitle(title);
        if(TextUtils.isEmpty(titleSub)){
            setPageSubtitleName(titleSub);
        }
    }

    @Override
    protected void init() {}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(IntentConstants.TITLE, title);
        outState.putString(IntentConstants.TITLE_SUB, titleSub);
        outState.putString(IntentConstants.WAP_CONTENT, content);
    }

}
