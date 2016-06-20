package com.wuxianedu.corelib.image;

import android.content.Context;
import android.widget.ImageView;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wuxianedu.corelib.CoreApplication;
import com.wuxianedu.corelib.R;
import java.io.File;

/**
 * 图片加载管理类
 * Created by lungank on 2016/4/11.
 */
public class ImageLoaderManager {

    private static ImageLoaderManager imageLoaderManager = null;

    private ImageLoaderManager(){}

    public static ImageLoaderManager getInstance(){
        if (imageLoaderManager == null) {
            synchronized (ImageLoaderManager.class) {
                if (imageLoaderManager == null) {
                    imageLoaderManager = new ImageLoaderManager();
                }
            }
        }
        return imageLoaderManager;
    }

    /**
     * 初始化ImageLoader
     */
    public void initImageLoader(Context context){
        // 获取我们应用的最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory/8;

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.img_pictures_no) //下载时显示的图片
                .showImageOnFail(R.mipmap.img_def_error) //下载失败时显示的图片
                .displayer(new FadeInBitmapDisplayer(500)) //图片加载成功后渐入动画
                .cacheInMemory(true) //设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) //设置下载的图片是否缓存在SD卡
                .build();

        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(5) //指定线程池大小
                .tasksProcessingOrder(QueueProcessingType.LIFO) //指定图片加载策略，后进先出
                .memoryCache(new LruMemoryCache(cacheMemory / 8)) //内存缓存
                .diskCache(new UnlimitedDiscCache(new File(CoreApplication.IMAGE_DIR))) //设置硬盘缓存
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .imageDownloader(new BaseImageDownloader(context, 5000, 30000)) //设置超时时间，第一个参数连接超时，第二个是读取超时
                .defaultDisplayImageOptions(options)
                .writeDebugLogs() //调试日志
                .build();
        ImageLoader.getInstance().init(configuration);
    }

    /**
     * 加载图片
     * @param url 图片url
     * @param imageView View
     * @param defaultImage 下载时默认图片
     * @param errorImage 加载错误图片
     */
    public void loadImage(String url, ImageView imageView, int defaultImage, int errorImage){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(defaultImage) //下载时显示的图片
            .showImageOnFail(errorImage) //下载失败时显示的图片
            .displayer(new FadeInBitmapDisplayer(500)) //图片加载成功后渐入动画
            .cacheInMemory(true) //设置下载的图片是否缓存在内存中
            .cacheOnDisk(true) //设置下载的图片是否缓存在SD卡
            .build();
        ImageLoader.getInstance().displayImage(url, imageView, options);
    }

    /**
     * 加载图片
     * @param imageView 图片显示框
     * @param url 图片地址
     */
    public void loadImage(String url, ImageView imageView){
        ImageLoader.getInstance().displayImage(url, imageView);
    }

    /**
     * 加载图片
     * @param url 图片地址
     * @param listener 加载完成监听器
     */
    private void loadImage(final String url,final SimpleImageLoadingListener listener){
        ImageLoader.getInstance().loadImage(url, listener);
    }

    /**
     * 加载图片,指定图片最大宽和高
     * @param url 图片地址
     * @param maxWidth 最大宽
     * @param maxHeight 最大高
     * @param listener 加载完成监听器
     */
    private void loadImage(final String url, int maxWidth, int maxHeight,
               final SimpleImageLoadingListener listener){
        //指定图片大小
        ImageSize imageSize = new ImageSize(maxWidth,maxHeight);
        ImageLoader.getInstance().loadImage(url, imageSize, listener);
    }

}
