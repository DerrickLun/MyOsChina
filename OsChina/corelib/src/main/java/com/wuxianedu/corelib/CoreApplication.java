package com.wuxianedu.corelib;

import android.app.Application;

import com.wuxianedu.corelib.image.ImageLoaderManager;
import com.wuxianedu.corelib.network.RequestManager;
import com.wuxianedu.corelib.util.L;
import com.wuxianedu.corelib.util.file.FileUtils;
import com.wuxianedu.corelib.util.file.SDCardManager;
import java.io.File;

/**
 * 核心Application
 * @author ymx
 */
public class CoreApplication extends Application{

    public static CoreApplication instance;
    //缓存目录
    public static String CACHE_DIR;
    //图片缓存目录
    public static String IMAGE_DIR;
    //上传图片临时目录
    public static String IMAGE_UPLOAD_TEMP;
    //文件目录
    public static String FILE_DIR;
    //日志目录
    public static String LOG_DIR;
    //日志文件
    public static String LOG_FILE;
    //调试状态(控制日志是否打印，发布版本时需关闭)
    public static final boolean IS_DEBUG = true;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        if(SDCardManager.isExistSD()){
            //SD卡存在
            CACHE_DIR = SDCardManager.getSDCacheDir(this);
        }else{
            //内部存储
            CACHE_DIR = this.getCacheDir().getAbsolutePath()+ File.separator;
        }
        L.d("缓存目录："+CACHE_DIR);

        IMAGE_DIR = CACHE_DIR + "image" + File.separator;
        IMAGE_UPLOAD_TEMP = CACHE_DIR + "imageUploadTemp" + File.separator;
        FILE_DIR = CACHE_DIR + "file" + File.separator;
        LOG_DIR = CACHE_DIR + "log" + File.separator;
        LOG_FILE = LOG_DIR + "cache.log";

        FileUtils.checkDir(CACHE_DIR);
        FileUtils.checkDir(IMAGE_DIR);
        FileUtils.checkDir(IMAGE_UPLOAD_TEMP);
        FileUtils.checkDir(FILE_DIR);
        FileUtils.checkDir(LOG_DIR);

        //初始化ImageLoaderManager
        ImageLoaderManager.getInstance().initImageLoader(this);
        //初始化Volley
        RequestManager.getInstance().init(this);
    }

}
