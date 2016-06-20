package com.wuxianedu.oschina.ui;

import android.os.Bundle;

import com.wuxianedu.oschina.R;
import com.wuxianedu.oschina.base.BaseActivity;
import com.wuxianedu.oschina.ui.fragment.UserFragment;
import com.wuxianedu.oschina.util.IntentConstants;

/**
 * 用户页面
 * Created by lungank on 2016/5/12.
 */
public class UserActivity extends BaseActivity {

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_framelayout);
    }

    @Override
    protected void init() {
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            userId = bundle.getInt(IntentConstants.USER_ID);
            String userName = bundle.getString(IntentConstants.USER_NAME);
            setPageTitle(userName);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_id, UserFragment.newInstance(userId))
                .commitAllowingStateLoss();
    }

}

