package com.qunar.wechat.auto.task;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by lihaibin.li on 2017/11/9.
 */

public interface Task {
    void trigger(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow);

    void pause();

    void resume();

    void stop();

    boolean isAccomplished();
}
