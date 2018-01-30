package com.qunar.wechat.auto.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.qunar.wechat.auto.common.Constants;

/**
 * Created by lihaibin.li on 2017/11/30.
 */

public class SharedPrefsHelper {
    private final SharedPreferences weChatSp;
    private final SharedPreferences.Editor weChatSpEditor;

    public SharedPrefsHelper(Context context) {
        weChatSp = context.getSharedPreferences(Constants.SP_NAME_WECHAT,Context.MODE_APPEND);
        weChatSpEditor = weChatSp.edit();
    }

    /**
     * 获取weChatSp.xml的SharedPreferences对象
     *
     * @return
     */
    public SharedPreferences getWeChatSp() {
        return weChatSp;
    }
}
