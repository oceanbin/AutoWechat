package com.qunar.wechat.auto.action;

import android.content.Context;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.google.common.base.Predicate;
import com.qunar.wechat.auto.utils.AccNodeHelper;

import java.util.List;

/**
 * Created by lihaibin.li on 2017/11/17.
 */

public class ScrollNewFriendAction implements Action{
    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        List<AccessibilityNodeInfo> checkNodes = rootInActiveWindow.findAccessibilityNodeInfosByText("添加朋友");
        if (checkNodes == null || checkNodes.isEmpty()) {
            return false;
        }

        List<AccessibilityNodeInfo> nodeInfos = rootInActiveWindow.findAccessibilityNodeInfosByText("新的朋友");
        if (nodeInfos == null || nodeInfos.isEmpty()) {
            return false;
        }

        AccessibilityNodeInfo scrollableListView = null;
        for (AccessibilityNodeInfo nodeInfo : nodeInfos) {
            scrollableListView = AccNodeHelper.firstParentNode(nodeInfo, new Predicate<AccessibilityNodeInfo>() {
                @Override
                public boolean apply(AccessibilityNodeInfo input) {
                    return input.isScrollable();
                }
            });

            if (scrollableListView != null) {
                break;
            }
        }

        if (scrollableListView == null) {
            return false;
        }

        Log.d("ScrollNewFriendAction","ScrollNewFriendAction");
        AccNodeHelper.scrollToEnd(scrollableListView);
        return true;
    }
}
