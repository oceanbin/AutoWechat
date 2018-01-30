package com.qunar.wechat.auto.action;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.utils.AccNodeHelper;

import java.util.List;

/**
 * Created by lihaibin.li on 2018/1/29.
 */

public class FillRemarkInfoAction4 implements Action {
    private String remark;
    public FillRemarkInfoAction4(String remark){
        this.remark = remark;
    }
    @TargetApi(18)
    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {

        List<AccessibilityNodeInfo> inputNode = rootInActiveWindow.findAccessibilityNodeInfosByViewId(Constants.WECHAT_PACKAGENAME + ":id/cpl");
        if(inputNode == null || inputNode.isEmpty()){
            return false;
        }
        return AccNodeHelper.setText(context,inputNode.get(0),remark,false);
    }
}
