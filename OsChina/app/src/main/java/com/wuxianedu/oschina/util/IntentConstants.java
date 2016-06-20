package com.wuxianedu.oschina.util;

/**
 * Intent常量
 */
public class IntentConstants {

    //类型
    public static final String TYPE = "type";
    //广播ACTION，监听用户登录及退出
    public static final String LOGIN_BROADCAST_ACTION ="com.wuxianedu.oschina.action.login";
    public static final String LOGIN_RETURN="loginReturn"; //登录成功需要返回
    public static final String LOGIN_VALIDATE= "loginValidate"; //验证用户是否登录
    public static final String LOGIN_RESULT_CODE="loginResultCode"; //登录成功，返回的Resultcode的值
    public static final String LOGIN_TARGET_CLASS = "loginTargetClass"; //登录成功后目标Class页面

    public static final String USER_ID = "userId";   //用户id
    public static final String USER_NAME = "userName";   //用户名

    public static final String PROJECT_DETAIL = "projectDetail"; //项目详情
    public static final String WAP_CONTENT = "wapContent"; 	//wap内容
    public static final String URL = "url";
    public static final String TITLE = "title"; //标题
    public static final String TITLE_SUB = "titleSub"; //副标题
    public static final String IMAGE_URL = "imageUrl";
    public static final String IMAGE_LIST = "imageList";

}
