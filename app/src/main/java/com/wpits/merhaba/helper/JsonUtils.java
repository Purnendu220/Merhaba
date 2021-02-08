package com.wpits.merhaba.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {

    public static String toJson(Object object) {

        Gson gson = new GsonBuilder().create();
        return gson.toJson(object);
    }

    public static <T> T fromJson(String jsonString, Class<T> classType) {
        if(jsonString==null){
            return null;

        }

        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, classType);
    }


}
