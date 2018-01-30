package com.qunar.wechat.auto.utils;

import android.text.TextUtils;

/**
 * Created by lihaibin.li on 2018/1/30.
 */

public class Utils {
    public static boolean isNum(String str){
        if(TextUtils.isEmpty(str))
            return false;
        String reg = "^\\d+$";
        return str.matches(reg);
    }
}
