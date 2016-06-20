package com.wuxianedu.oschina.util;

/**
 * 常量接口
 * Created by lungank on 2016/4/13.
 */
public interface Constant {

    String MEMBER="member";   //会员
    int TYPE_LOGIN= 101; //登录
    int TYPE_LOGOUT= 102;//注销
    int LOGIN_RESULT_CODE=103;  //用户登录响应码
    int LOGIN_REQUEST_CODE=104;  //用户登录请求码
    int PROJECT = 100;
    int STAR = 200 ;
    int WATCH =300;

    //key
    String THEME_ID = "themeId"; //主题颜色
    String CARD_KEY = "card_key";

    String IS_RIGHT_MODE = "isRightMode";
    String IS_CARD_TYPE = "isCardType";

    int CLICK_ITEM_EXPLORE = 0; //发现
    int CLICK_ITEM_MY = 1; //我的,已登录
    int CLICK_ITEM_LANGUAGE = 2; //语言
    int CLICK_ITEM_SHAKE= 3; //摇一摇
    int CLICK_ITEM_MY_NOT_LOGIN = 4; //我的,未登录
    int CLICK_ITEM_THEME = 5; //切换主题
    int CLICK_ITEM_SET = 6; //设置
    int CLICK_ITEM_HEAD_LOGINED = 7; //点击头像，已登录
    int CLICK_ITEM_HEAD_NOT_LOGIN = 8; //点击头像，未登录
    int SETTING_REQUEST_CODE = 100; //打开设置界面请求码


}
