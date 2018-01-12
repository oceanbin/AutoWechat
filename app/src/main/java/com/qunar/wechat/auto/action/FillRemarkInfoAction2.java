package com.qunar.wechat.auto.action;

import android.content.Context;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.api.WechatApi;
import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.common.TaskExecutor;
import com.qunar.wechat.auto.jsonbean.Remark;
import com.qunar.wechat.auto.utils.AccNodeHelper;
import com.qunar.wechat.auto.utils.RemarkUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 主动添加好友时  设置备注
 * Created by lihaibin.li on 2017/12/15.
 */

public class FillRemarkInfoAction2 implements Action {

    private static final String TAG = FillRemarkInfoAction2.class.getSimpleName();

    private String mobile;
    private String viewId;

    public FillRemarkInfoAction2(String mobile,String viewId){
        this.mobile = mobile;
        this.viewId = viewId;
    }
    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        Future<Remark> future = TaskExecutor.submit(new Callable<Remark>() {
            @Override
            public Remark call() throws Exception {
                return WechatApi.getUserRemark(mobile, Constants.LOCAL_WX_ID);
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
            return AccNodeHelper.setEditableNodeTextByViewId(context, rootInActiveWindow, viewId, TextUtils.isEmpty(remark.getRemark()) ? RemarkUtils.makeRemark() : remark.getRemark(), false);
        }
    }
}
