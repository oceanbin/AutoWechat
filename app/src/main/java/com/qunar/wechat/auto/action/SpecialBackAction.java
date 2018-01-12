package com.qunar.wechat.auto.action;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.utils.AccNodeHelper;

import java.util.List;

/**
 * Created by lihaibin.li on 2017/12/18.
 */

public class SpecialBackAction extends BackAction {
    private String flag;
    public SpecialBackAction(String flag){
        this.flag = flag;
    }
    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        List<AccessibilityNodeInfo> wechatNameNodes = AccNodeHelper.findNodesByExactText(rootInActiveWindow, flag);
        if(wechatNameNodes == null || wechatNameNodes.isEmpty()){
            return false;
        }
        return super.execute(context, event, rootInActiveWindow);
    }
}
