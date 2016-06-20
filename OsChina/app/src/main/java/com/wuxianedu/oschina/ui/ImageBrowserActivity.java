package com.wuxianedu.oschina.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.wuxianedu.oschina.R;
import com.wuxianedu.oschina.adapter.ImageBrowserAdapter;
import com.wuxianedu.oschina.base.BaseActivity;
import com.wuxianedu.oschina.bean.ImageUrl;
import com.wuxianedu.oschina.util.IntentConstants;
import com.wuxianedu.oschina.widget.photoview.PhotoTextView;

import java.util.List;

/**
 * 图片浏览页面
 * Created by lungank on 2016/5/12.
 */
public class ImageBrowserActivity extends BaseActivity {

    private ViewPager viewPager;
    private PhotoTextView pageTextView;
    private ImageBrowserAdapter mAdapter;
    private int mPosition;
    private int mTotal;
    private String imageURL; //当前图片
    private ImageUrl imageVO; //所有图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.isTemplate=false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browser);
    }

    @Override
    protected void init() {
        viewPager = (ViewPager) findViewById(R.id.imagebrowser_svp_pager);
        pageTextView = (PhotoTextView) findViewById(R.id.imagebrowser_ptv_page);
        Intent intent = getIntent();
        imageURL = intent.getStringExtra(IntentConstants.IMAGE_URL);
        imageVO = (ImageUrl) intent.getSerializableExtra(IntentConstants.IMAGE_LIST);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        List<String> list=imageVO.getUrlList();
        for (int i=0;i<list.size();i++) {
            String url=list.get(i);
            int index=url.indexOf("title");
            if(index!=-1){
                url=url.substring(0, index-2);
                list.remove(i);
                list.add(i, url);
            }
        }
        mPosition=list.indexOf(imageURL);
        mTotal=list.size();
        if (mPosition > mTotal) {
            mPosition = mTotal - 1;
        }
        if (mTotal >= 1) {
            pageTextView.setText((mPosition<=0?1:mPosition)+ "/" + mTotal);
            mAdapter = new ImageBrowserAdapter(this,imageVO.getUrlList());
            viewPager.setAdapter(mAdapter);
            viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    mPosition = position;
                    viewPager.setCurrentItem(position);
                    pageTextView.setText((position % mTotal) + 1 + "/" + mTotal);
                }
            });
            viewPager.setCurrentItem(mPosition);
        }
    }

}

