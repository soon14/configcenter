package com.asiainfo.configcenter.center.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JSONUtil {
    private static Gson gson = new Gson();
    public static String obj2JsonStr(Object obj){
        return gson.toJson(obj);
    }

    public static JsonObject jsonStr2JsonObject(String jsonStr){
        return gson.fromJson(jsonStr,JsonObject.class);
    }

    public static <T> T jsonStrToBean(String jsonStr, Class<T> clz){
        return gson.fromJson(jsonStr,clz);
    }

}
