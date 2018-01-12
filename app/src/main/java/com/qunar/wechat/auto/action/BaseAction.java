package com.qunar.wechat.auto.action;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.utils.RandomUtil;

/**
 * Created by lihaibin.li on 2017/12/19.
 */

public class BaseAction implements Action {
    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        return false;
    }

    protected void sleep(){
        try{
            Thread.sleep(RandomUtil.getRandomSeconds());
        }catch (Exception e){

        }
    }
}
