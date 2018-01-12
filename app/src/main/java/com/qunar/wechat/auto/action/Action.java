package com.qunar.wechat.auto.action;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by lihaibin.li on 2017/11/9.
 */

public interface Action {
    /**
     * 执行一个动作，只有真正执行并成功才返回true
     *
     * @param event
     * @param rootInActiveWindow
     * @return
     */
    boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow);
}
