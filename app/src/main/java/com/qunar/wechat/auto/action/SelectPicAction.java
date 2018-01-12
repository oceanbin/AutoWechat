package com.qunar.wechat.auto.action;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.utils.AccNodeHelper;

import java.util.List;

/**
 * Created by lihaibin.li on 2017/12/19.
 */

public class SelectPicAction extends BaseAction{
    int count;

    @TargetApi(18)
    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        List<AccessibilityNodeInfo> bgcNodeInfos = rootInActiveWindow.findAccessibilityNodeInfosByViewId(Constants.WECHAT_PACKAGENAME + ":id/bgc");
        if (bgcNodeInfos == null || bgcNodeInfos.isEmpty()) {
            return false;
        }
        for (AccessibilityNodeInfo nodeInfo : bgcNodeInfos) {

            if (count == 9) {//最多前九张
                break;
            }

            if (nodeInfo.isChecked())
                continue;

            AccessibilityNodeInfo accessibilityNodeInfo = AccNodeHelper.firstClickableParent(nodeInfo);
            accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);

            sleep();
            List<AccessibilityNodeInfo> selectNodeInfos = AccNodeHelper.findNodesByExactText(rootInActiveWindow, "选择");
            if (selectNodeInfos != null && !selectNodeInfos.isEmpty()) {
                selectNodeInfos.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                new BackAction().execute(context, event, rootInActiveWindow);
                count++;
                return false;
            }

        }

        return true;
    }
}
