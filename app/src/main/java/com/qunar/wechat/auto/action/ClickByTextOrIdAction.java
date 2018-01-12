package com.qunar.wechat.auto.action;

import android.annotation.TargetApi;
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
 * 先根据text找没有再找id
 * Created by lihaibin.li on 2017/12/1.
 */

public class ClickByTextOrIdAction implements Action {
    private final String TAG = ClickByTextOrIdAction.class.getSimpleName();

    private String controlText;
    private String controlId;

    public ClickByTextOrIdAction(String controlText,String controlId){
        this.controlText = controlText;
        this.controlId = controlId;
        if (StringUtils.isBlank(controlText)) {
            throw new AutoWechatRuntimeException("控件Text不能为空");
        }
    }
    @TargetApi(18)
    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        List<AccessibilityNodeInfo> nodeInfos = AccNodeHelper.findNodesByExactText(rootInActiveWindow, controlText);
        if (nodeInfos == null || nodeInfos.isEmpty()) {
            nodeInfos =  rootInActiveWindow.findAccessibilityNodeInfosByViewId(controlId);
            if (nodeInfos == null || nodeInfos.isEmpty()) {
                return false;
            }
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
