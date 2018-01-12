package com.qunar.wechat.auto.action;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.utils.AccNodeHelper;

import java.util.List;

/**
 * Created by lihaibin.li on 2017/11/22.
 */

public class ChatBackAction implements Action {
    @TargetApi(18)
    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        List<AccessibilityNodeInfo> nodeInfos = rootInActiveWindow.findAccessibilityNodeInfosByViewId(Constants.WECHAT_PACKAGENAME + ":id/h1");
        if (nodeInfos != null && nodeInfos.size() > 0){
             return AccNodeHelper.clickByParent(nodeInfos.get(0));
        }
        return false;
    }
}
