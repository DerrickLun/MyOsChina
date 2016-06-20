package com.wuxianedu.corelib.util.file;

import com.wuxianedu.corelib.CoreApplication;
import com.wuxianedu.corelib.util.MD5Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化工具类
 * Created by lungank on 2016/4/9.
 */
public class SerializableUtils {

    /**
     * 序列化数据
     * @param fileName 文件地址
     * @param t 要序列化的对象
     */
    public static <T> void setSerializable(String fileName,T t) {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = new FileOutputStream(new File(CoreApplication.FILE_DIR +
                MD5Utils.md5(fileName)));
            oos = new ObjectOutputStream(fos);
            oos.writeObject(t);
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            IOUtils.close(oos);
        }
    }

    /**
     * 反序列化数据
     * @param fileName 缓存文件名
     * @param <T> 对象类型
     * @return 对象
     */
    public static <T> T getSerializable(String fileName) {
        ObjectInputStream oos = null;
        try {
            File file = new File(CoreApplication.FILE_DIR + MD5Utils.md5(fileName));
            if (!file.exists()){
                file.createNewFile();
            }

            if (file.length() == 0){
                return null;
            }

            FileInputStream fis = new FileInputStream(file);
            oos = new ObjectInputStream(fis);
            return (T)oos.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(oos);
        }
        return null;
    }

    /**
     * 反序列化数据
     * @param fileName 缓存文件名
     * @param time 缓存文件有效时间
     * @param <T> 对象类型
     * @return 对象
     */
    public static <T> T getSerializable(String fileName,long time) {
        ObjectInputStream oos = null;
        try {
            File file = new File(CoreApplication.FILE_DIR + MD5Utils.md5(fileName));
            if (!file.exists()){
                file.createNewFile();
            }

            if (file.length() == 0){
                return null;
            }

            long lastTime = file.lastModified();
            long nowTime=System.currentTimeMillis();
            if(nowTime-lastTime>time){
                return null;
            }

            FileInputStream fis = new FileInputStream(file);
            oos = new ObjectInputStream(fis);
            return (T)oos.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(oos);
        }
        return null;
    }

    /**
     * 删除序列化数据
     * @param fileName 文件名
     */
    public static boolean deleteSerializable(String fileName) {
        File file = new File(CoreApplication.FILE_DIR + MD5Utils.md5(fileName));
        return file.exists() && file.delete();
    }

}
