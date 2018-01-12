package com.qunar.wechat.auto.action;

import android.content.Context;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.common.Constants;

/**
 * 发送消息
 * Created by lihaibin.li on 2017/11/21.
 */

public class SendMessageAction extends ClickByTextCommonAction implements Action{
    public SendMessageAction(String controlText){
        super(controlText);
    }
    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        if(TextUtils.isEmpty(Constants.AUTO_SEND_WX_MESSAGE))
            return true;
        return super.execute(context,event,rootInActiveWindow);
    }
}
