package com.wuxianedu.oschina.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.wuxianedu.corelib.image.ImageLoaderManager;
import com.wuxianedu.corelib.util.CoreUtils;
import com.wuxianedu.corelib.util.IntentUtils;
import com.wuxianedu.corelib.util.SPUtils;
import com.wuxianedu.oschina.OsChinaApplication;
import com.wuxianedu.oschina.R;
import com.wuxianedu.oschina.adapter.ThemeRoundAdapter;
import com.wuxianedu.oschina.base.BaseActivity;
import com.wuxianedu.oschina.bean.Member;
import com.wuxianedu.oschina.ui.fragment.FindFragment;
import com.wuxianedu.oschina.ui.fragment.LanguageFragment;
import com.wuxianedu.oschina.ui.fragment.ShakeFragment;
import com.wuxianedu.oschina.ui.fragment.UserFragment;
import com.wuxianedu.oschina.util.Constant;
import com.wuxianedu.oschina.util.IntentConstant;
import com.wuxianedu.oschina.util.SnackbarUtils;
import com.wuxianedu.oschina.widget.RoundImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener{

    private List<Integer> themeList;
    private List<Integer> colorDrawableList;
    private Fragment[] fragments ;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    //退出时间
    private long exitTime = 0L;
    private int index = 0;
    private boolean menuClick;
    private TextView userNameTextView;
    private RoundImageView headIconView;
    private AlertDialog dialog;

    {
        fragments = new Fragment[]{new FindFragment(),new UserFragment(),
                new LanguageFragment(), new ShakeFragment()};

        themeList = new ArrayList<>();
        themeList.add(R.style.BlueTheme);
        themeList.add(R.style.BrownTheme);
        themeList.add(R.style.RedTheme);
        themeList.add(R.style.BlueGreyTheme);
        themeList.add(R.style.YellowTheme);
        themeList.add(R.style.DeepPurpleTheme);
        themeList.add(R.style.PinkTheme);
        themeList.add(R.style.GreenTheme);

        colorDrawableList = new ArrayList<>();
        colorDrawableList.add(R.drawable.d_theme_round_blue);
        colorDrawableList.add(R.drawable.d_theme_round_brown);
        colorDrawableList.add(R.drawable.d_theme_round_red);
        colorDrawableList.add(R.drawable.d_theme_round_blue_grey);
        colorDrawableList.add(R.drawable.d_theme_round_yellow);
        colorDrawableList.add(R.drawable.d_theme_round_deep_purple);
        colorDrawableList.add(R.drawable.d_theme_round_pink);
        colorDrawableList.add(R.drawable.d_theme_round_green);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isTemplate = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tipInfoLayout.showNoMessage();
    }

    @Override
    protected void init() {

        //初始化ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle(R.string.find);
        setSupportActionBar(toolbar);

        //初始化navigation
        navigationView = (NavigationView) findViewById(R.id.navigation_id);
        userNameTextView = (TextView) navigationView.findViewById(R.id.tv_head_username);
        headIconView = (RoundImageView) navigationView.findViewById(R.id.iv_head);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.findViewById(R.id.id_ll_mine).setOnClickListener(this);
        navigationView.setForegroundGravity(Gravity.END);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                if(menuClick){ //菜单优化 ，在收缩导航栏之后再执行切换Fragment或者打开页面等操作
                    showFragment();
                    menuClick = false;
                }
                super.onDrawerClosed(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        showMemberInfo();
        showDefaultMenu();
        registerBroadcastReceiver();
    }

    /**
     * 显示会员信息
     */
    private void showMemberInfo(){
        Member member = OsChinaApplication.instance.getMember();

        if(member != null){
            userNameTextView.setText(member.getName());
            ImageLoaderManager.getInstance().loadImage(member.getNew_portrait(),
                    headIconView, R.mipmap.mini_avatar, R.mipmap.mini_avatar);
        }
    }

    /**
     * 注册广播(会员登录及退出广播接收器)
     */
    private void registerBroadcastReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IntentConstant.LOGIN_BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(memberBroadcastReceiver, intentFilter);
    }

    /**
     * 会员登录及退出广播接收器
     */
    private BroadcastReceiver memberBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int type = bundle.getInt(IntentConstant.TYPE);
            switch (type){
                case Constant.TYPE_LOGIN:  //登录成功
                    showMemberInfo();
                    break;
                case Constant.TYPE_LOGOUT:  //退出登录
                    userNameTextView.setText(getString(R.string.no_login_hint));
                    headIconView.setImageResource(R.mipmap.mini_avatar);
                    break;
            }
        }
    };

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuClick = true;
        //关闭菜单
        itemClick(menuItem);
        closeDrawer();
        return true;
    }

    private void itemClick(final MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.item_nav_find: //发现
                index = Constant.CLICK_ITEM_EXPLORE;
                setPageTitle(menuItem.getTitle().toString());
                break;
            case R.id.item_nav_mine: //我的
                if(OsChinaApplication.instance.isMemberLogin()) {
                    index = Constant.CLICK_ITEM_MY;
                    setPageTitle(menuItem.getTitle().toString());
                }else{
                    index = Constant.CLICK_ITEM_MY_NOT_LOGIN;
                }
                break;
            case R.id.item_nav_language: //语言
                index = Constant.CLICK_ITEM_LANGUAGE;
                setPageTitle(menuItem.getTitle().toString());
                break;
            case R.id.item_nav_shake: //摇一摇
                index = Constant.CLICK_ITEM_SHAKE;
                setPageTitle(menuItem.getTitle().toString());
                break;
            case R.id.item_change_theme: //切换主题
                index = Constant.CLICK_ITEM_THEME;
                break;
            case R.id.item_setting: //设置
                index = Constant.CLICK_ITEM_SET;
                break;
            default:
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_ll_mine:   //个人中心或登录
                menuClick = true;
                closeDrawer();
                if(OsChinaApplication.instance.isMemberLogin()){
                    //已登录
                    index = Constant.CLICK_ITEM_HEAD_LOGINED;
                }else{
                    //未登录
                    index = Constant.CLICK_ITEM_HEAD_NOT_LOGIN;
                }
                break;
        }
    }

    /**
     * 显示对应的Fragment
     */
    public void showFragment(){
        Bundle bundle;
        switch (index){
            case Constant.CLICK_ITEM_MY_NOT_LOGIN: //我的-未登录
                Intent intent = new Intent(this, LoginActivity.class);
                bundle = new Bundle();
                bundle.putBoolean(IntentConstant.LOGIN_RETURN, true);
                bundle.putInt(IntentConstant.LOGIN_RESULT_CODE, Constant.LOGIN_RESULT_CODE);
                intent.putExtras(bundle);
                IntentUtils.startActivityForResult(this, intent, Constant.LOGIN_REQUEST_CODE);
                break;
            case Constant.CLICK_ITEM_THEME: //切换主题
                chooseTheme();
                break;
            case Constant.CLICK_ITEM_SET: //设置
                Intent settingIntent = new Intent(this,SettingActivity.class);
                IntentUtils.startActivityForResult(this,settingIntent,Constant.SETTING_REQUEST_CODE);
                break;
            case Constant.CLICK_ITEM_EXPLORE: //发现
            case Constant.CLICK_ITEM_MY: //我的
            case Constant.CLICK_ITEM_LANGUAGE: //语言
            case Constant.CLICK_ITEM_SHAKE: //摇一摇
                Fragment fragment = fragments[index];
                if (index==Constant.CLICK_ITEM_MY && fragment.getArguments() == null) {
                    bundle = new Bundle();
                    bundle.putInt(IntentConstant.USER_ID, OsChinaApplication.instance
                            .getMember().getId());
                    fragment.setArguments(bundle);
                }
                switchFragmentContent(R.id.fl_parent_id, fragment);
                break;
            case Constant.CLICK_ITEM_HEAD_LOGINED: //点击头像，已登录
                IntentUtils.startActivityNetWork(MainActivity.this,
                        MemberActivity.class);
                break;
            case Constant.CLICK_ITEM_HEAD_NOT_LOGIN: //点击头像，未登录
                IntentUtils.startActivityNetWork(MainActivity.this,
                        LoginActivity.class);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constant.LOGIN_REQUEST_CODE
                && resultCode == Constant.LOGIN_RESULT_CODE){
            //获取我的MenuItem
            Menu menu = navigationView.getMenu();
            MenuItem menuItem =menu.findItem(R.id.item_nav_mine);
            itemClick(menuItem);
            showFragment();
        }
    }

    /**
     * 选择主题
     */
    private void chooseTheme(){
        if (dialog == null){ //第一次打开更改主题
            GridView gridView = (GridView) LayoutInflater
                    .from(this).inflate(R.layout.gridview_change_theme,null);
            int currentThemeId = (int) SPUtils.get(this,Constant.THEME_ID,R.style.BlueTheme);
            int position = themeList.indexOf(currentThemeId);
            ThemeRoundAdapter adapter = new ThemeRoundAdapter(this,colorDrawableList,position);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    changeTheme(position);
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("更改主题");
            builder.setView(gridView);
            dialog = builder.create();
        }
        dialog.show();
    }

    /**
     * 更改主题
     */
    private void changeTheme(int position) {
        //保存颜色
        int themeId = themeList.get(position);
        SPUtils.put(this,Constant.THEME_ID,themeId);
        //重启当前界面
        finish();
        IntentUtils.startActivity(this, MainActivity.class);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(navigationView);
        }else {
            if(mFragmentContent instanceof FindFragment) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    SnackbarUtils.showMessage(this, tipInfoLayout, R.string.sure_exit);
                    exitTime = System.currentTimeMillis();
                    return;
                } else {
                    CoreUtils.exitApp(this);
                }
            }else{
                index = 0;
                showDefaultMenu();
            }
        }
        super.onBackPressed();
    }

    /**
     * 显示默认菜单
     */
    private void showDefaultMenu(){
        //获取第一个MenuItem
        Menu menu = navigationView.getMenu();
        MenuItem menuItem =menu.findItem(R.id.item_nav_find);
        menuItem.setChecked(true);
        setPageTitle(menuItem.getTitle().toString());
        showFragment();
    }

    /**
     * 关闭drawerLayout
     */
    private void closeDrawer(){
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(navigationView);
        }
    }

    @Override
    protected void onDestroy() {
        //注销会员登录及退出广播接收器
        LocalBroadcastManager.getInstance(this).unregisterReceiver(memberBroadcastReceiver);
        super.onDestroy();
    }

}
