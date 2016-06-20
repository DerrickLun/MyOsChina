package com.wuxianedu.corelib.util.file;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * IO工具类
 * Created by lungank on 2016/4/9.
 */
public class IOUtils {

    /**
     * 将输入流解析为String
     * @param is 输入流
     * @return 输入流文本
     */
    public static String inputStreamToString(InputStream is){
        StringBuilder sb = new StringBuilder();
        String line = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(reader);
        }
        return sb.toString();
    }

    /**
     * 将String转为输入流
     * @param str 文本
     * @return 输入流 //TODO 为啥不是输入流
     */
    public static InputStream stringToInputStream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    /**
     * 获取文件字节数组
     * @param fileName 文件名
     * @return 字节数组
     */
    public static byte[] getFileBytes(String fileName) {
        File file = new File(fileName);
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        if( file.exists() ){
            try {
                //输入流转为byte数组
                is = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int len = -1;
                bos = new ByteArrayOutputStream();
                while ((len = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                return bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                close(bos);
                close(is);
            }
            return null;
        }
        return null;
    }

    /**
     * 读取文本文件
     * @param file 文件
     * @return 文本
     */
    public static String readTextFile(File file) {
        try {
            InputStream is = new FileInputStream(file);
            return inputStreamToString(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将文本内容写入文件
     * @param file 文件
     * @param str 文本
     */
    public static void writeTextFile(File file, String str) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(writer);
        }
    }

    /**
     * 关闭IO输入流
     * @param closeable IO流
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new RuntimeException("IOException occurred", e);
            }
        }
    }

}
