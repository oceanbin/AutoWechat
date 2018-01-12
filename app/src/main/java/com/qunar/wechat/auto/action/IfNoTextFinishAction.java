package com.qunar.wechat.auto.action;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.utils.AccNodeHelper;

import java.util.List;

/**
 * 未找到 自动结束掉当前action
 * Created by lihaibin.li on 2017/12/15.
 */

public class IfNoTextFinishAction extends ClickByTextCommonAction {
    private String TAG = IfNoTextFinishAction.class.getSimpleName();

    public IfNoTextFinishAction(String controlText) {
        this.controlText = controlText;
    }

    @TargetApi(18)
    @Override
    public boolean execute(Context context, AccessibilityEvent event, final AccessibilityNodeInfo rootInActiveWindow) {
        List<AccessibilityNodeInfo> sendMsgInfo = AccNodeHelper.findNodesByExactText(rootInActiveWindow, "发消息");
        if (sendMsgInfo != null && !sendMsgInfo.isEmpty())
            return true;

        List<AccessibilityNodeInfo> nodeInfos = AccNodeHelper.findNodesByExactText(rootInActiveWindow, controlText);
        if (nodeInfos == null || nodeInfos.isEmpty()) {
            return false;
        }
        return AccNodeHelper.clickByParent(nodeInfos.get(0));
    }

}
