package com.wuxianedu.oschina.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.wuxianedu.corelib.image.ImageLoaderManager;
import com.wuxianedu.oschina.R;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * 图片浏览适配器
 * Created by lungank on 2016/5/12.
 */
public class ImageBrowserAdapter extends PagerAdapter {

    private List<String> mPhotos;
    private List<PhotoView> imageViews;
    private Context context;

    /**
     * 初始化
     */
    public void init() {
        imageViews = new ArrayList<>();
        for (int i = 0; i < mPhotos.size(); i++) {
            PhotoView photoView = new PhotoView(context);
            imageViews.add(photoView);
        }
    }

    public ImageBrowserAdapter(Context context, List<String> photos) {
        this.context = context;
        this.mPhotos = photos;
        init();
    }

    @Override
    public int getCount() {
        return imageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public View instantiateItem(final ViewGroup container, final int position) {
        final PhotoView imageView = imageViews.get(position);
        imageView.setImageResource(R.mipmap.img_pictures_no);
        ImageLoaderManager.getInstance().loadImage(mPhotos.get(position),imageView);
        container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViews.get(position));
    }

}

