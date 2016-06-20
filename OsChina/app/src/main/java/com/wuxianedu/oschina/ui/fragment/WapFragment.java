package com.wuxianedu.oschina.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wuxianedu.corelib.util.TextUtils;
import com.wuxianedu.oschina.R;
import com.wuxianedu.oschina.base.BaseFragment;
import com.wuxianedu.oschina.bean.ImageUrl;
import com.wuxianedu.oschina.ui.ImageBrowserActivity;
import com.wuxianedu.oschina.ui.WapActivity;
import com.wuxianedu.oschina.util.IntentConstants;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 网页显示页面
 * Created by lungank on 2016/5/12.
 */
@SuppressLint("JavascriptInterface")
public class WapFragment extends BaseFragment {

    private WebView webview;
    private List<String> urlList;
    private String content;
    private String url; // 网页url
    private static final String OSCHINA_START="<div class=\"white\"><div class=\"highlight\">";
    private static final String OSCHINA_END="";

    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_wap, container, false);
    }

    @Override
    public void init(View rootView) {
        webview = (WebView) rootView.findViewById(R.id.webview_id);
        // 设置编码
        webview.getSettings().setDefaultTextEncodingName("utf-8");
        // 支持js
        webview.getSettings().setJavaScriptEnabled(true);
        // 设置背景颜色 透明
        webview.setBackgroundColor(Color.argb(0, 0, 0, 0));
        webview.getSettings().setDefaultFontSize(16);
        // webview闪烁问题
        webview.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        // 设置本地调用对象及其接口
        webview.addJavascriptInterface(new JavaScriptObject(mActivity),"jsListener");

        if (mActivity instanceof WapActivity) {
            // ((WapActivity)mActivity).showWaitDialog(null, true);
        }
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    if (mActivity instanceof WapActivity) {
                        // ((AbstractActivity)mActivity).dissmissWaitingDialog();
                    }
                    webview.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() && getActivity() != null) {
            Bundle bundle = getArguments();
            content = bundle.getString(IntentConstants.WAP_CONTENT);
            url = bundle.getString(IntentConstants.URL);
            if (!TextUtils.isEmpty(content)) {
                showHtml(content);
            } else {
                webview.loadUrl(url);
            }
        }
    }

    public class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public class JavaScriptObject {
        Context mContxt;

        public JavaScriptObject(Context mContxt) {
            this.mContxt = mContxt;
        }

        /**
         * 点击webview上的图片，传入该缩略图的大图Url
         * @param imageUrl
         */
        @JavascriptInterface   //4.2以上的版本需要此注解，来兼容
        public void onImageClick(String imageUrl) {
            Log.e("--Main--", "imageUrl:" + imageUrl);
            Intent intent = new Intent(mContxt, ImageBrowserActivity.class);
            intent.putExtra(IntentConstants.IMAGE_URL, imageUrl);
            intent.putExtra(IntentConstants.IMAGE_LIST, new ImageUrl(urlList));
            startActivity(intent);
        }
    }

    /**
     * 显示html
     * @param body
     */
    private void showHtml(String body) {
        if(TextUtils.isEmpty(body)){
            body="<h3>404</h3>";
        }
        String html = getAssetsHtml();
        html = html.replace("*****", body);
        // System.out.println("====>assetsHtml="+assetsHtml);
        if (!TextUtils.isEmpty(html)) {
            // 获取一共有多少图片URL  jpg
            Pattern p_img = Pattern.compile(
                    "<\\s*img[^<]*src\\s*=\\s*\"([^<]+(jpg|gif|bmp|bnp|png){1})\"[^>]*>",
                    Pattern.CASE_INSENSITIVE);
            Matcher mImg = p_img.matcher(html);
            urlList = new ArrayList<String>();
            for (; mImg.find(); urlList.add(mImg.group(1))) {

            }

            // 添加点击图片放大支持--》只设置宽，不设置高，图片就会等比例缩放
            content = html.replaceAll("(<img[^>]+src=\")(\\S+)\"",
                    "$1$2\" width='100%' onClick=\"javascript:jsListener.onImageClick('$2')\"");
            //解决：CnBeta的每个图片<img>标签被<a href>标签所包含，会导致点击图片时，打开浏览器显示图片。
            content=content.replaceAll("href", "");
            //只针对开源中国制定
            content=content.replaceAll(OSCHINA_START, OSCHINA_END);
            webview.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
        }
    }

    /**
     * 从Assets中获取html
     * @return
     */
    private String getAssetsHtml() {
        String data = "";
        try {
            // 读取assets目录下的文件需要用到AssetManager对象的Open方法打开文件
            InputStream is = getActivity().getAssets().open("html/newspage.html");
            // loadData()方法需要的是一个字符串数据所以我们需要把文件转成字符串
            ByteArrayBuffer baf = new ByteArrayBuffer(1024);
            int count = 0;
            while ((count = is.read()) != -1) {
                baf.append(count);
            }
            data = EncodingUtils.getString(baf.toByteArray(), "utf-8");
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}

