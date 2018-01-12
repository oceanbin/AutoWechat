package com.qunar.wechat.auto.action;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.api.WechatApi;
import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.common.TaskExecutor;
import com.qunar.wechat.auto.jsonbean.Remark;
import com.qunar.wechat.auto.utils.AccNodeHelper;
import com.qunar.wechat.auto.utils.RemarkUtils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 自动接受好友请求时  添加备注action
 * Created by lihaibin.li on 2017/11/9.
 */

public class FillRemarkInfoAction implements Action {
    private static final String TAG = FillRemarkInfoAction.class.getSimpleName();

    private String viewId;

    public FillRemarkInfoAction(String viewId) {
        this.viewId = viewId;
    }

    String body = "";
    @Override
    public boolean execute(final Context context, AccessibilityEvent event, final AccessibilityNodeInfo rootInActiveWindow) {
        List<AccessibilityNodeInfo> viewContactsLableNodes = AccNodeHelper.findNodesByExactText(rootInActiveWindow, "为朋友设置备注");
        if (viewContactsLableNodes == null || viewContactsLableNodes.isEmpty()) {
            // 不是到朋友验证修改备注页面触发的事件
            return false;
        }
//        final String body = AccNodeHelper.getEditableNodeTextByViewId(rootInActiveWindow, viewId);
        List<AccessibilityNodeInfo> verifyNodeInfos = AccNodeHelper.findNodesByStartWithText(rootInActiveWindow, "对方发来的验证消息为");
        if (verifyNodeInfos != null && !verifyNodeInfos.isEmpty()) {
            body = verifyNodeInfos.get(0).getText().toString();
        }
        Log.d(TAG,body);
//        if (TextUtils.isEmpty(body)) {
//            return false;
//        }
        Future<Remark> future = TaskExecutor.submit(new Callable<Remark>() {
            @Override
            public Remark call() throws Exception {
                return WechatApi.getUserRemark(body, Constants.LOCAL_WX_ID);
            }
        });
        Remark remark;
        try {
            remark = future.get();
        } catch (Exception e) {
            remark = new Remark();
        }
        if (remark == null) {
            return false;
        } else {
            if (!TextUtils.isEmpty(remark.message)) {
                Constants.AUTO_SEND_WX_MESSAGE = remark.getMessage();
            } else {
                Constants.AUTO_SEND_WX_MESSAGE = "";
            }
            return AccNodeHelper.setEditableNodeTextByViewId(context, rootInActiveWindow, viewId, TextUtils.isEmpty(remark.getRemark()) ? RemarkUtils.makeRemark() : remark.getRemark(), false);
        }
    }


}
