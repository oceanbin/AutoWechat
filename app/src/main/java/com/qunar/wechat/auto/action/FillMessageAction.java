package com.qunar.wechat.auto.action;

import android.content.Context;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.utils.AccNodeHelper;

/**
 * 赋值聊天框EditText
 * Created by lihaibin.li on 2017/11/21.
 */

public class FillMessageAction implements Action {
    private String viewId;
    public FillMessageAction(String viewId){
        this.viewId = viewId;
    }
    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        if(TextUtils.isEmpty(Constants.AUTO_SEND_WX_MESSAGE))
            return true;
        return AccNodeHelper.setEditableNodeTextByViewId(context, rootInActiveWindow, viewId, Constants.AUTO_SEND_WX_MESSAGE, false);
    }

}
