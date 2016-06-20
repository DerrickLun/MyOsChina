package com.wuxianedu.oschina.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.wuxianedu.corelib.util.CoreUtils;
import com.wuxianedu.corelib.util.SPUtils;
import com.wuxianedu.corelib.widget.TipInfoLayout;
import com.wuxianedu.oschina.util.Constant;
import com.wuxianedu.oschina.R;
import com.wuxianedu.oschina.ui.MainActivity;

import java.lang.reflect.Method;

/**
 * Activity基类
 * Created by lungank on 2016/4/11.
 */
public abstract class BaseActivity extends AppCompatActivity {

    //是否使用模板
    protected boolean isTemplate = true;

    //提示信息
    protected TipInfoLayout tipInfoLayout;

    //页面主体
    protected LinearLayout mainBody;

    //上一个Fragment
    protected Fragment mFragmentContent;

    public LinearLayout getMainBody() {
        return mainBody;
    }

    public TipInfoLayout getTipInfoLayout() {
        return tipInfoLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //修改主题
        setTheme((int) SPUtils.get(this, Constant.THEME_ID, R.color.blue));
        super.onCreate(savedInstanceState);
        CoreUtils.addAppActivity(this);
        //初始化异常处理
//        ExceptionHandler.getInstance().init(this);

        if(isTemplate){
            setContentView(R.layout.core_template);
        }

        if(!(this instanceof MainActivity)){   //首页不需要返回键和进度显示框
            if(getSupportActionBar()!=null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //返回键

            }
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if(layoutResID == R.layout.core_template){
            super.setContentView(layoutResID);
            initViews();
        }else{
            if(mainBody!=null){
                mainBody.removeAllViews();
                mainBody.addView(this.getLayoutInflater().inflate(layoutResID,null),
                        new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                                WindowManager.LayoutParams.MATCH_PARENT));
            }else{
                super.setContentView(layoutResID);
            }
            init();
        }
    }

    /**
     * 查找视图
     * @param id id
     * @param <T> 视图类型
     * @return 视图
     */
    protected <T extends View> T findView(int id){
        return (T)findViewById(id);
    }

    /**
     * 初始化视图
     */
    protected void initViews(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setSubtitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
        tipInfoLayout = (TipInfoLayout)findViewById(R.id.fl_panent_id);
        mainBody = (LinearLayout) findViewById(R.id.view_mainBody_id);
    }

    /**
     * 初始化
     */
    protected abstract void init();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 解决Android5.0默认不显示图标问题
     * enable为true时，菜单添加图标有效，enable为false时无效。4.0系统默认无效
     */
    protected void setIconEnable(Menu menu, boolean enable) {
        try {
            Class<?> clazz = Class.forName("android.support.v7.internal.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible",boolean.class);
            m.setAccessible(true);
            // MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
            m.invoke(menu, enable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置标题
     * @param titleName 标题名称
     */
    public void setPageTitle(String titleName){
        getSupportActionBar().setTitle(titleName);
    }

    /**
     * 设置标题
     * @param strResId 标题资源id
     */
    public void setPageTitle(int strResId){
        getSupportActionBar().setTitle(getString(strResId));
    }

    /**
     * 设置子标题
     * @param subtitleName 子标题名称
     */
    public void setPageSubtitleName(String subtitleName){
        getSupportActionBar().setSubtitle(subtitleName);
    }

    /**
     * 设置子标题
     * @param subtitleName 子标题资源id
     */
    public void setPageSubtitleName(int subtitleName){
        getSupportActionBar().setSubtitle(subtitleName);
    }

    /**
     * 切换Fragment
     * @param to 目标Fragment
     */
    protected void switchFragmentContent(int resId, Fragment to) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(mFragmentContent!=null){
            if (mFragmentContent != to) {
                if (!to.isAdded()) { // 先判断是否被add过
                    transaction.hide(mFragmentContent).add(resId, to, to.getClass().getName()); // 隐藏当前的fragment，add下一个到Activity中
                } else {
                    transaction.hide(mFragmentContent).show(to); // 隐藏当前的fragment，显示下一个
                }
            }
        }else{
            transaction.add(resId, to, to.getClass().getName());
        }
        transaction.commitAllowingStateLoss();  //推荐使用此方法，更安全，更方便
        mFragmentContent = to;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CoreUtils.removeAppActivity(this);
    }

}
