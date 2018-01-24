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
 * 根据指定text后指定index点击
 * Created by lihaibin.li on 2018/1/23.
 */

public class ClickByIdToIndexAction implements Action{
    private String id;
    private int index;
    private String TAG = ClickByTextCommonAction.class.getSimpleName();

    public ClickByIdToIndexAction(String id, int index) {
        if (StringUtils.isBlank(id)) {
            throw new AutoWechatRuntimeException("控件Text不能为空");
        }

        this.index = index;
        this.id = id;
    }

    @TargetApi(18)
    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        List<AccessibilityNodeInfo> nodeInfos = rootInActiveWindow.findAccessibilityNodeInfosByViewId(id);
        if (nodeInfos == null || nodeInfos.isEmpty()) {
            return false;
        }
        try {
            Thread.sleep(RandomUtil.getRandomSeconds());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return AccNodeHelper.clickByParent(nodeInfos.get(index));
    }
}
