package com.qunar.wechat.auto.mission;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.api.WechatApi;
import com.qunar.wechat.auto.common.BackgroundExecutor;

/**
 * Created by lihaibin.li on 2018/1/29.
 */

public class BaseTaskMission implements Mission {
    protected boolean isAccomplished;
    @Override
    public void trigger(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {

    }

    protected void setTaskDone(final int id) {
        BackgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                WechatApi.setTaskDone(id);
            }
        });
    }

    @Override
    public boolean isAccomplished() {
        return isAccomplished;
    }
}
