package com.qunar.wechat.auto.action;

import android.content.Context;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.google.common.base.Predicate;
import com.qunar.wechat.auto.utils.AccNodeHelper;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by lihaibin.li on 2018/1/23.
 */

public class ClickByDescAction implements Action{
    private final String TAG = ClickByDescAction.class.getSimpleName();
    private String desc;

    public ClickByDescAction(String desc){
        this.desc = desc;
    }
    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        AccessibilityNodeInfo backNode = AccNodeHelper.firstChildNode(rootInActiveWindow, new Predicate<AccessibilityNodeInfo>() {
            @Override
            public boolean apply(AccessibilityNodeInfo input) {
                if (input == null) {
                    return false;
                }

                return !StringUtils.isBlank(input.getContentDescription()) && input.getContentDescription().equals(desc);
            }
        });

        if (backNode == null) {
            return false;
        }

        Log.d(TAG,"执行ClickByDescAction");
        return AccNodeHelper.clickByParent(backNode);
    }
}
