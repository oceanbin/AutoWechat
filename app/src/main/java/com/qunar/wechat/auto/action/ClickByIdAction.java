package com.qunar.wechat.auto.action;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.exception.AutoWechatRuntimeException;
import com.qunar.wechat.auto.utils.AccNodeHelper;
import com.qunar.wechat.auto.utils.RandomUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by lihaibin.li on 2018/1/23.
 */

public class ClickByIdAction implements Action {

    private String controlId;

    public ClickByIdAction(String controlId){
        this.controlId = controlId;
        if (StringUtils.isBlank(controlId)) {
            throw new AutoWechatRuntimeException("控件id不能为空");
        }
    }
    @TargetApi(18)
    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        List<AccessibilityNodeInfo> nodeInfos = rootInActiveWindow.findAccessibilityNodeInfosByViewId(controlId);
        if (nodeInfos == null || nodeInfos.isEmpty()) {
             return false;
        }
        try {
            Thread.sleep(RandomUtil.getRandomSeconds());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return AccNodeHelper.clickByParent(nodeInfos.get(0));
    }
}
