package com.qunar.wechat.auto.action;

import android.content.Context;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.exception.AutoWechatRuntimeException;
import com.qunar.wechat.auto.utils.AccNodeHelper;
import com.qunar.wechat.auto.utils.RandomUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by lihaibin.li on 2017/11/22.
 */

public class ClickByContainTextAction implements Action {
    private final String controlText;
    private String TAG = ClickByContainTextAction.class.getSimpleName();

    public ClickByContainTextAction() {
        this.controlText = "";
    }

    public ClickByContainTextAction(String controlText) {
        if (StringUtils.isBlank(controlText)) {
            throw new AutoWechatRuntimeException("控件Text不能为空");
        }

        this.controlText = controlText;
    }

    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        List<AccessibilityNodeInfo> nodeInfos = AccNodeHelper.findNodesByContainsWithText(rootInActiveWindow, controlText);
        if (nodeInfos == null || nodeInfos.isEmpty()) {
            return false;
        }

        Log.d(TAG, String.format("点击[%s]", controlText));
        try {
            Thread.sleep(RandomUtil.getRandomSeconds());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return AccNodeHelper.clickByParent(nodeInfos.get(0));
    }
}
