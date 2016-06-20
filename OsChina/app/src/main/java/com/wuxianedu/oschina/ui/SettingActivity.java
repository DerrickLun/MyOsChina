package com.wuxianedu.oschina.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.wuxianedu.corelib.util.L;
import com.wuxianedu.corelib.util.SPUtils;
import com.wuxianedu.corelib.util.file.AppDataManager;
import com.wuxianedu.corelib.util.file.SDCardManager;
import com.wuxianedu.oschina.OsChinaApplication;
import com.wuxianedu.oschina.R;
import com.wuxianedu.oschina.base.BaseActivity;
import com.wuxianedu.oschina.util.Constant;

/**
 * 设置页面
 * Created by lungank on 2016/4/13.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private String cachePath;
    private AlertDialog dialog;
    private TextView cacheTV;
    private AppCompatCheckBox checkBox;
    private boolean isCardType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isTemplate = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        cachePath = SDCardManager.getSDCacheDir(this);
        cacheTV = (TextView) findViewById(R.id.tv_cache);
    }

    @Override
    protected void init() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_setting));
        ActionBar toolbar = getSupportActionBar();
        assert toolbar != null;
        toolbar.setTitle(getResources().getString(R.string.setting));
        toolbar.setDisplayHomeAsUpEnabled(true);

        TextView clearCacheTV = (TextView) findViewById(R.id.tv_clear_cache);
        clearCacheTV.setOnClickListener(this);
        TextView feedbackTV = (TextView) findViewById(R.id.tv_feedback);
        feedbackTV.setOnClickListener(this);
        TextView give_markTV = (TextView) findViewById(R.id.tv_give_mark);
        give_markTV.setOnClickListener(this);

        checkBox = (AppCompatCheckBox) findViewById(R.id.cb_language_mode);
        boolean isCard = (boolean) SPUtils.get(this, Constant.CARD_KEY, true);
        checkBox.setChecked(isCard);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtils.put(SettingActivity.this, Constant.CARD_KEY, isChecked);
                OsChinaApplication.carLayout = !OsChinaApplication.carLayout;
            }
        });
        refreshCache();
    }

    /**
     * 更新缓存
     */
    private void refreshCache(){
        //获取缓存
        try {
            String cache = AppDataManager.getCacheSize(this,cachePath);
            cacheTV.setText(cache);
        } catch (Exception e) {
            e.printStackTrace();
            L.e("无法获取缓存大小");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_clear_cache: //清除缓存
                AppDataManager.cleanCache(this, cachePath);
                refreshCache();
                if (dialog == null){
                    dialog = new AlertDialog.Builder(this)
                            .setMessage(getResources().getString(R.string.clear_cache_success))
                            .setPositiveButton(getResources().getString(R.string.sure),
                                    new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                }
                dialog.show();
                break;
            case R.id.tv_feedback: //意见反馈
                break;
            case R.id.tv_give_mark: //给个好评
                break;
        }
    }

    @Override
    protected void onDestroy() {
//        SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.swi_right_mode);
//        boolean isRightMode = switchCompat.isChecked();
//        bundle.putBoolean(Constant.IS_RIGHT_MODE,isRightMode);


        super.onDestroy();
    }
}
