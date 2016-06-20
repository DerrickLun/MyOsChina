package com.wuxianedu.corelib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.wuxianedu.corelib.R;

/**
 * 提示信息控件
 * Created by lungank on 2016/4/11.
 */
public class TipInfoLayout extends FrameLayout {

    private Context context;
    private ProgressWheel progressWheel;
    private ImageView mTvTipState;
    private TextView mTvTipMsg;

    public TipInfoLayout(Context context) {
        super(context);
        this.context    = context;
        initView(context);
    }

    public TipInfoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);
    }

    public TipInfoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);
    }

    /**
     * 初始化视图
     * @param context 上下文对象
     */
    private void initView(Context context){
        View view  = LayoutInflater.from(context).inflate(R.layout.core_tip_info_layout,null);
        progressWheel = (ProgressWheel)view.findViewById(R.id.tv_tip_loading);
        mTvTipState = (ImageView) view.findViewById(R.id.tv_tip_state);
        mTvTipMsg = (TextView) view.findViewById(R.id.tv_tip_msg);
        addView(view);
    }

    /**
     * 显示加载
     */
    public void showLoading() {
        this.progressWheel.setVisibility(View.VISIBLE);
        this.mTvTipState.setVisibility(View.GONE);
        this.mTvTipMsg.setVisibility(View.VISIBLE);
        this.mTvTipMsg.setText(context.getString(R.string.tip_loading));
    }

    /**
     * 隐藏加载
     */
    public void hideLoading(){
        this.progressWheel.setVisibility(View.GONE);
        this.mTvTipState.setVisibility(View.GONE);
        this.mTvTipMsg.setVisibility(View.GONE);
    }

    /**
     * 设置网络错误
     */
    public void showNetworkError() {
        this.progressWheel.setVisibility(View.GONE);
        this.mTvTipState.setVisibility(View.VISIBLE);
        this.mTvTipState.setImageResource(R.mipmap.core_page_icon_network);
        this.mTvTipMsg.setVisibility(View.VISIBLE);
        this.mTvTipMsg.setText(context.getString(R.string.tip_load_network_error));
    }

    /**
     * 设置加载失败信息
     * @param message 失败信息
     */
    public void showFailureInfo(String message) {
        this.progressWheel.setVisibility(View.GONE);
        this.mTvTipState.setVisibility(View.VISIBLE);
        this.mTvTipState.setImageResource(R.mipmap.core_page_icon_loaderror);
        this.mTvTipMsg.setVisibility(View.VISIBLE);
        this.mTvTipMsg.setText(message);
    }

    /**
     * 设置空数据信息
     */
    public void showEmptyData() {
        this.setVisibility(VISIBLE);
        this.progressWheel.setVisibility(View.GONE);
        this.mTvTipState.setVisibility(View.VISIBLE);
        this.mTvTipState.setImageResource(R.mipmap.core_page_icon_empty);
        this.mTvTipMsg.setVisibility(View.VISIBLE);
        this.mTvTipMsg.setText(context.getString(R.string.tip_load_empty));
    }

    /**
     * 设置空数据信息
     * @param message 空数据信息
     */
    public void showEmptyData(String message){
        showEmptyData();
        this.mTvTipMsg.setText(message);
    }

    /**
     * 不显示任何提示
     */
    public void showNoMessage(){
        this.progressWheel.setVisibility(View.GONE);
        this.mTvTipState.setVisibility(View.GONE);
        this.mTvTipMsg.setVisibility(View.GONE);
    }

}
