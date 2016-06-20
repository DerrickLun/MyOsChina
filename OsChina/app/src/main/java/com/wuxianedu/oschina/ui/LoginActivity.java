package com.wuxianedu.oschina.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.wuxianedu.corelib.util.IntentUtils;
import com.wuxianedu.corelib.util.JSONParseUtils;
import com.wuxianedu.corelib.util.TextUtils;
import com.wuxianedu.corelib.widget.ClearEditText;
import com.wuxianedu.oschina.OsChinaApplication;
import com.wuxianedu.oschina.R;
import com.wuxianedu.oschina.base.BaseActivity;
import com.wuxianedu.oschina.bean.Member;
import com.wuxianedu.oschina.network.RequestAPI;
import com.wuxianedu.oschina.network.RequestClient;
import com.wuxianedu.oschina.network.RequestConfig;
import com.wuxianedu.oschina.util.Constant;
import com.wuxianedu.oschina.util.IntentConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录页面
 * Created by lungank on 2016/5/12.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private ClearEditText userNameView;
    private ClearEditText passwordView;
    private CheckBox checkPwd;
    private Button loginButton;
    private TextInputLayout userNameTextInputLayout;
    private TextInputLayout passwordTextInputLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setPageTitle(R.string.login_title);
        getTipInfoLayout().hideLoading();
    }

    @Override
    protected void init() {
        userNameTextInputLayout= (TextInputLayout) findViewById(R.id.username);
        userNameView = (ClearEditText)userNameTextInputLayout.getEditText();
        passwordTextInputLayout = (TextInputLayout) findViewById(R.id.password);
        passwordView = (ClearEditText) passwordTextInputLayout.getEditText();
        checkPwd = (CheckBox) findViewById(R.id.checkbox_id);
        loginButton= (Button) findViewById(R.id.bt_login);
        loginButton.setOnClickListener(this);
    }

    /**
     * 登录
     */
    private void login(){
        final String username = userNameView.getText().toString();
        if(TextUtils.isEmpty(username)){
            userNameTextInputLayout.setError(getString(R.string.msg_login_username_null));
            return;
        }
        final String pwd= passwordView.getText().toString();
        if(TextUtils.isEmpty(pwd)){
            passwordTextInputLayout.setError(getString(R.string.msg_login_passwork_null));
            return;
        }

        RequestConfig config = new RequestConfig();
        config.setTipInfoLayout(getTipInfoLayout())
                .setMainBody(getMainBody())
                .setIsCover(false)
                .setShowProgressDialog(true);

        Map<String,String> requestParams = new HashMap<String,String>();
        requestParams.put("email",username);
        requestParams.put("password",pwd);

        new RequestClient(LoginActivity.this, config){
            @Override
            public void loadSuccess(String result) {
                Member member = JSONParseUtils.parseObject(result, Member.class);
                OsChinaApplication.member = member;
                if(checkPwd.isChecked()){
                    OsChinaApplication.instance.saveMember(member);
                }

                //发送广播，更新主视图
                Intent loginIntent = new Intent();
                Bundle loginBundle = new Bundle();
                loginBundle.putInt(IntentConstants.TYPE, Constant.TYPE_LOGIN);
                loginIntent.putExtras(loginBundle);
                loginIntent.setAction(IntentConstants.LOGIN_BROADCAST_ACTION);
                LocalBroadcastManager.getInstance(LoginActivity.this).sendBroadcast(loginIntent);

                //页面跳转处理
                Bundle bundle=getIntent().getExtras();
                if(bundle!=null){
                    boolean isReturn=bundle.getBoolean(IntentConstants.LOGIN_RETURN, false);
                    boolean isValidateLogin=bundle.getBoolean(IntentConstants.LOGIN_VALIDATE, false);
                    if(isReturn){  //登录成功，返回resultCode
                        int resultCode=bundle.getInt(IntentConstants.LOGIN_RESULT_CODE, 0);
                        setResult(resultCode);
                    }else if(isValidateLogin){   //判断用户是否从其它页面需要用户登录才能打开的页面
                        //登录成功去往真实目标页面
                        Class<?> targetClazz=(Class<?>) bundle.getSerializable(IntentConstants.LOGIN_TARGET_CLASS);
                        Intent intent = getIntent();   //保留Intent不变，因此Intent有上一个页面有值需要传给最终页面，
                        intent.setClass(LoginActivity.this, targetClazz);
                        IntentUtils.startActivity(LoginActivity.this, intent);
                    }else{   //从用户登录页而来，应进入个人中心页
                        IntentUtils.startActivity(LoginActivity.this, MemberActivity.class);
                    }
                }else{  //从用户登录页而来，应进入个人中心页
                    IntentUtils.startActivity(LoginActivity.this, MemberActivity.class);
                }

                finish();
            }

            @Override
            public void loadFail() {}
        }.post(RequestAPI.LOGIN, requestParams);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                login();
                break;
        }
    }

}
