package com.wuxianedu.oschina.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wuxianedu.corelib.network.NetWorkUtils;
import com.wuxianedu.corelib.network.RequestManager;
import com.wuxianedu.corelib.util.DialogUtils;
import com.wuxianedu.corelib.util.L;
import com.wuxianedu.corelib.widget.TipInfoLayout;
import com.wuxianedu.oschina.R;
import com.wuxianedu.oschina.util.SnackbarUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 网络请求
 * Created by lungank on 2016/4/11.
 */
public abstract class RequestClient {

    private Context context;

    private RequestConfig config;

    private ProgressDialog dialog;

    public RequestClient(Context context, RequestConfig config){
        this.context = context;
        this.config = config;
    }

    /**
     * get请求
     * @param url url
     */
    public void get(String url){
        request(false, url, null);
    }

    /**
     * get请求
     * @param url url
     */
    public void get(String url, Map<String,String> params){
        request(false, url, params);
    }

    /**
     * post请求
     * @param url url
     * @param params 请求参数
     */
    public void post(String url, Map<String,String> params){
        request(true, url, params);
    }

    /**
     * 网络请求
     * @param isPost 是否post请求
     * @param url 请求url
     * @param params 请求参数
     */
    private void request(boolean isPost, String url, Map<String,String> params){
        //判断是否有网络
        if(!NetWorkUtils.NETWORK){
            SnackbarUtils.showMessage(context, config.getTipInfoLayout(), R.string.not_network);
        }

        url = RequestAPI.getAbsoluteUrl(url);

        if(config.isCover() && config.getMainBody()!=null){
            config.getMainBody().setVisibility(View.GONE);
        }

        if(config.isShowProgressDialog()){
            //显示进度对话框
            dialog = DialogUtils.showProgressDialog(context,R.string.pull_to_loading);
        }else {
            //是否显示加载进度
            if (config.isLoading()) {
                TipInfoLayout temp = config.getTipInfoLayout();
                temp.showLoading();
            }
        }

        if(isPost){
            L.e("url==="+url);
            RequestManager.getInstance().post(url,responseListener,errorListener,params);
        }else{
            // StringBuilder是用来组拼请求地址和参数
            if(params!=null) {
                StringBuilder sb = new StringBuilder();
                sb.append(url).append("?");
                if (params.size() != 0) {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        // 如果请求参数中有中文，需要进行URLEncoder编码,gbk/utf8
                        try {
                            sb.append(entry.getKey()).append("=").append(URLEncoder.
                                encode(entry.getValue(), "utf-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        sb.append("&");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                }
                url = sb.toString();
            }
            L.e("url==="+url);
            RequestManager.getInstance().get(url, responseListener,errorListener);
        }

    }

    Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String result) {
            config.getMainBody().setVisibility(View.VISIBLE);
            config.getTipInfoLayout().hideLoading();
            if(dialog!=null && dialog.isShowing()){
                dialog.dismiss();
            }
            loadSuccess(result);
        }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            config.getMainBody().setVisibility(View.VISIBLE);
            config.getTipInfoLayout().hideLoading();
            if(dialog!=null && dialog.isShowing()){
                dialog.dismiss();
            }
            SnackbarUtils.showMessage(context, config.getTipInfoLayout(), R.string.failure);
            loadFail();
        }
    };

    /**
     * 加载成功
     * @param result 返回数据
     */
    protected abstract void loadSuccess(String result);

    /**
     * 加载失败
     */
    protected abstract void loadFail();

}
