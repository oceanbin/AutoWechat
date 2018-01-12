package com.qunar.wechat.auto.utils;

import com.google.gson.Gson;


public class JsonUtils {
    private static final Gson gson = new Gson();

    public static Gson getGson()
    {
        return gson;
    }
}
