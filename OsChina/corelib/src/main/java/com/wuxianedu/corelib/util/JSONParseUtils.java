package com.wuxianedu.corelib.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON解析工具类
 * Created by lungank on 2016/4/9.
 */
public class JSONParseUtils {

    /**
     * 解析对象
     * @param json json字符串
     * @param clazz 对象class
     * @return 对象
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        if(TextUtils.isEmpty(json)){
            return null;
        }
        return JSONObject.parseObject(json, clazz);
    }

    /**
     * 解析数组
     * @param json json字符串
     * @param clazz 数组类型
     * @return 数组
     */
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        if(TextUtils.isEmpty(json)){
            return null;
        }
        return new ArrayList<T>(JSONArray.parseArray(json, clazz));
    }

}
