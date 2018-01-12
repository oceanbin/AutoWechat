package com.qunar.wechat.auto.mission;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by lihaibin.li on 2017/12/14.
 */

public interface Mission {
    void trigger(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow);

    boolean isAccomplished();
}
