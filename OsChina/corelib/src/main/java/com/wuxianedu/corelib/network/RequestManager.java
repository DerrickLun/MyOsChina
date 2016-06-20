package com.wuxianedu.corelib.network;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

/**
 * 网络请求管理类
 * Created by lungank on 2016/4/11.
 */
public class RequestManager {

    private static RequestManager requestManager = null;

    private static RequestQueue requestQueue = null;

    private RequestManager(){}

    /**
     * 初始化请求队列
     * @param context 上下文对象
     */
    public void init(Context context){
        requestQueue = Volley.newRequestQueue(context);
    }

    /**
     * 获取RequestManager实例
     * @return RequestManager实例
     */
    public static RequestManager getInstance(){
        if (requestManager == null) {
            synchronized (RequestManager.class) {
                if (requestManager == null) {
                    requestManager = new RequestManager();
                }
            }
        }
        return requestManager;
    }

    /**
     * 添加Request到队列
     * @param request 请求
     * @param <T> 请求类型
     */
    private <T> void addRequestQueue(Request<T> request){
        requestQueue.add(request);
    }

    /**
     * 添加Request到队列【带标签】
     * @param request 请求
     * @param tag 请求标签
     * @param <T> 请求类型
     */
    private <T> void addRequestQueue(Request<T> request,String tag){
        if(!TextUtils.isEmpty(tag)){
            request.setTag(tag);
        }
        addRequestQueue(request);
    }

    /**
     * get请求
     * @param url 请求地址
     * @param listener 请求完成监听器
     * @param errorListener 请求错误监听器
     * @return get请求
     */
    public Request get(String url, Response.Listener<String> listener,
           Response.ErrorListener errorListener){
        StringRequest stringRequest = new StringRequest(url,listener,errorListener);
        addRequestQueue(stringRequest);
        return stringRequest;
    }

    /**
     * post请求
     * @param url 请求地址
     * @param listener 请求监听器
     * @param errorListener 请求错误监听器
     * @return post请求
     */
    public Request post(String url, Response.Listener<String> listener,
               Response.ErrorListener errorListener,Map<String,String> params){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,listener,errorListener,params);
        addRequestQueue(stringRequest);
        return stringRequest;
    }

    /**
     * 根据标签取消请求
     * @param tag 请求标签
     */
    public void cancelRequest(String tag){
        requestQueue.cancelAll(tag);
    }

}
