package com.wuxianedu.corelib.util.file;

import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件工具类
 * @author lungank
 */
public class FileUtils {

    /**
     * 检查目录是否存在，不存在则创建
     * @param path 目录
     * @return 目录最后是否存在
     */
    public static boolean checkDir(String path) {
        File f = new File(path);
        return f.exists() || f.mkdirs();
    }

    /**
     * 获取文件扩展名
     * @param filename 文件名
     * @return 拓展名
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 复制文件
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     */
    public static void copyFile(String sourceFile, String targetFile) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFile));
            bos = new BufferedOutputStream(new FileOutputStream(targetFile));
            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len ;
            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }

                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除指定目录下文件及目录
     * @param filePath 目录
     * @param deleteThisPath 是否删除当前目录
     * @return 是否完全删除
     */
    public static boolean deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            return true;
        }
        try {
            File file = new File(filePath);
            if (!file.isDirectory()) {// 如果是文件，删除
                return file.delete();
            }else { //如果是目录
                File files[] = file.listFiles();
                for (File file1 : files) { //遍历子目录
                    if(!deleteFolderFile(file1.getAbsolutePath(), true)){
                        //如果删除不成功，返回false
                        return false;
                    }
                }
            }
            if (deleteThisPath && file.delete()){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除指定目录下文件及目录
     * @param filePath 文件或目录名称
     * @return 文件是否完全删除成功
     */
    public static boolean deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return true;
        }
        try {
            File file = new File(filePath);
            if (file.isDirectory()) {// 要删除的是目录
                File files[] = file.listFiles();
                for (File file1 : files) {
                    if (!deleteFile(file1.getAbsolutePath())){ //如果某个文件删除不成功
                        return false;
                    }
                }
                return true;
            }else { //要删除的是文件
                return file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //报异常才会执行下一条语句
        return false;
    }

}
