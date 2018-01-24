package com.qunar.wechat.auto.action;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.utils.AccNodeHelper;

import java.util.List;

/**
 * Created by lihaibin.li on 2018/1/23.
 */

public class FillRemarkInfoAction3 implements Action{
    private String remark;
    public FillRemarkInfoAction3(String remark){
        this.remark = remark;
    }
    @TargetApi(18)
    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        List<AccessibilityNodeInfo> nodeInfos = rootInActiveWindow.findAccessibilityNodeInfosByViewId(Constants.WECHAT_PACKAGENAME + ":id/amk");
        if(nodeInfos == null || nodeInfos.isEmpty()){
            return false;
        }
        nodeInfos.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);

        List<AccessibilityNodeInfo> inputNode = rootInActiveWindow.findAccessibilityNodeInfosByViewId(Constants.WECHAT_PACKAGENAME + ":id/amj");
        if(inputNode == null || inputNode.isEmpty()){
            return false;
        }
        return AccNodeHelper.setText(context,inputNode.get(0),remark,false);

    }
}
