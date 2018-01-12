package com.qunar.wechat.auto.action;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.event.AcceptFriendEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lihaibin.li on 2017/11/21.
 */

public class CountAction implements Action {

    AcceptFriendEvent acceptFriendEvent = new AcceptFriendEvent();

    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        EventBus.getDefault().post(acceptFriendEvent);
        return true;
    }
}
