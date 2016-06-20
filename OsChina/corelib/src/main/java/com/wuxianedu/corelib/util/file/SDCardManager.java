package com.wuxianedu.corelib.util.file;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import java.io.File;

/**
 * SD卡管理类
 * @author lungank
 */
public class SDCardManager {

    /**
     * 判断SD卡是否可用
     * @return 是否可用
     */
    public static boolean isExistSD(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取SD卡根路径
     * @return 根路径
     */
    public static String getSDCardPath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator;
    }

    /**
     * 获取手机内部空间大小
     * @return 手机内部空间大小
     */
    public static long getTotalInternalMemorySize() {
        //Gets the Android data directory
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        //每个block 占字节数
        long blockSize = stat.getBlockSize();
        //block总数
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 获取手机内部可用空间大小
     * @return 手机内部可用空间大小
     */
    public static long getAvailableInternalSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 获取SD卡缓存目录
     * @param context 上下文对象
     * @return SD卡缓存目录
     */
    public static String getSDCacheDir(Context context){
        if(isExistSD()){
            return context.getExternalCacheDir().getAbsolutePath();
        }
        return null;
    }

    /**
     * 获取SD卡大小
     * @return
     */
    public static long getSDCardSize() {
        if (isExistSD()) {
            StatFs stat = new StatFs(getSDCardPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        }
        return 0;
    }

    /**
     * 获取SD卡可用空间大小,单位byte
     * @return SD卡可用空间大小
     */
    public static long getAvailableSDCardSize() {
        if (isExistSD()) {
            StatFs stat = new StatFs(getSDCardPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        }
        return 0;
    }

}
