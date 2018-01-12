package com.qunar.wechat.auto.action;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.utils.AccNodeHelper;
import com.qunar.wechat.auto.utils.DataUtils;

import java.util.List;

/**
 * 获取本地微信id
 * Created by lihaibin.li on 2017/11/21.
 */

public class GetWxIdAction implements Action {
    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        List<AccessibilityNodeInfo> nodeInfos = AccNodeHelper.findNodesByStartWithText(rootInActiveWindow, "微信号：");
        if (nodeInfos == null || nodeInfos.isEmpty()) {
            return false;
        }
        String s = nodeInfos.get(0).getText().toString();
        String[] values = s.split("：");
        if (values != null && values.length > 1) {
            String wx_id = values[1];
            Constants.LOCAL_WX_ID = wx_id;
            DataUtils.getInstance(context).putPreferences(Constants.Preferences.WX_ID, wx_id);
            return true;
        } else {
            return false;
        }
    }
}
