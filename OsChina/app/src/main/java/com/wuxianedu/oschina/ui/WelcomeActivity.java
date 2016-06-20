package com.wuxianedu.oschina.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.wuxianedu.corelib.network.NetWorkStateService;
import com.wuxianedu.corelib.network.NetWorkUtils;
import com.wuxianedu.corelib.util.DialogUtils;
import com.wuxianedu.corelib.util.IntentUtils;
import com.wuxianedu.corelib.util.L;
import com.wuxianedu.oschina.R;

/**
 * 欢迎页面
 * Created by lungank on 2016/4/11.
 */
public class WelcomeActivity extends AppCompatActivity{

    //设置requestCode
    private static final int SETTING_NETWORK_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        //启动网络监听
        startService(new Intent(this, NetWorkStateService.class));
        ImageView loadingIv = (ImageView) this.findViewById(R.id.iv_id);
        loadingIv.animate().alpha(1.0f).rotation(359.0f).setDuration(1500L)
        .withEndAction(new Runnable() {
            @Override
            public void run() {
                if (!NetWorkUtils.NETWORK) {
                    L.e("监听结果---没有网络");
                    //没有网络
                    DialogUtils.showDialog(WelcomeActivity.this, R.string.setnetwork,
                            R.string.setting_yes, R.string.setting_no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //跳转到设置网络页面
                                    IntentUtils.startActivityForResult(WelcomeActivity.this,
                                            new Intent(Settings.ACTION_SETTINGS), SETTING_NETWORK_CODE);
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    WelcomeActivity.this.finish();
                                }
                            });
                } else{
                    //有网络则跳转到主页
                    L.e("监听结果---有网络");
                    IntentUtils.startActivity(WelcomeActivity.this, MainActivity.class);
                    WelcomeActivity.this.finish();
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SETTING_NETWORK_CODE){
            init();
        }
    }

}
