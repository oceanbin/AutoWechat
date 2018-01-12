package com.qunar.wechat.auto.action;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.jsonbean.Remark;
import com.qunar.wechat.auto.utils.AccNodeHelper;
import com.qunar.wechat.auto.utils.Function;
import com.qunar.wechat.auto.utils.MethodHelper;

import java.util.List;

/**
 * Created by lihaibin.li on 2017/12/14.
 */

public class FillAddFriendInfoAction implements Action {
    private String mobile;
    private Remark remark;
    private static final String requestLableText = "你需要发送验证申请，等对方通过";
    private static final String remarkLableText = "为朋友设置备注";

    public FillAddFriendInfoAction(String mobile){
        this.mobile = mobile;
    }

    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        List<AccessibilityNodeInfo> viewContactsLableNodes = AccNodeHelper.findNodesByExactText(rootInActiveWindow, "验证申请");
        if (viewContactsLableNodes == null || viewContactsLableNodes.isEmpty()) {
            // 不是到达验证申请页面触发的事件
            return false;
        }

        // 先清空验证申请内容
        clearEditText(context, rootInActiveWindow, requestLableText, true);

//        AccNodeHelper.setEditableNodeTextByLabelText(context, rootInActiveWindow,
//                requestLableText, autoOrder.getAddFriendRequestContent(), true);

        // 先清空朋友备注内容
        clearEditText(context, rootInActiveWindow, remarkLableText, false);

//        String prefix = SettingsProvider.getValue(SettingKeyEnum.WECHAT_REMARK_PREFIX);
//        AccNodeHelper.setEditableNodeTextByLabelText(context, rootInActiveWindow, remarkLableText,
//                (StringUtils.isBlank(prefix) ? "" : prefix) + autoOrder.getWechatRemark(), true);

        List<AccessibilityNodeInfo> sendButtonNodes = AccNodeHelper.findNodesByExactText(rootInActiveWindow, "发送");
        if (sendButtonNodes != null && !sendButtonNodes.isEmpty()) {
            // 调接口更新
//            if (VtmOpToolsServiceProxy.updateAutoAddFriendOrderProcessStatus(Collections.singletonList(autoOrder.getAutoOrderId()),
//                    ProcessStatusEnum.REQUEST_SENT)) {
//                // 更新DB
//                autoOrder.setProcessStatus(ProcessStatusEnum.REQUEST_SENT.getValue());
//                if (!autoOrder.save()) {
//                    ToastHelper.showShort("更新本地数据为[已发送]请求失败！");
//                }
//            } else {
//                ToastHelper.showShort("调接口更新数据为[已发送]失败！");
//                return false;
//            }

            AccNodeHelper.clickByParent(sendButtonNodes.get(0));
        }
        return true;
    }

    private void clearEditText(final Context context, final AccessibilityNodeInfo rootInActiveWindow,
                               final String labelText, final boolean isForward) {
        MethodHelper.retry(new Function<Integer, Object>() {
            @Override
            public Object apply(Integer integer) {
                AccNodeHelper.setEditableNodeTextByLabelText(context, rootInActiveWindow, labelText, "", isForward);
                return null;
            }
        }, new Function<Object, Boolean>() {
            @Override
            public Boolean apply(Object o) {
                return false;
            }
        }, 5);
    }
}
