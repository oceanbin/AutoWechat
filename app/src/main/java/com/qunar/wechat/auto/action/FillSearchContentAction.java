package com.qunar.wechat.auto.action;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.jsonbean.Params;
import com.qunar.wechat.auto.jsonbean.TodoTask;
import com.qunar.wechat.auto.utils.AccNodeHelper;
import com.qunar.wechat.auto.utils.JsonUtils;

/**
 * Created by lihaibin.li on 2018/1/23.
 */

public class FillSearchContentAction implements Action {
    private TodoTask todoTask;
    public FillSearchContentAction(TodoTask todoTask){
        this.todoTask = todoTask;
    }
    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        if(todoTask == null) return true;

        return AccNodeHelper.setEditableNodeTextByViewId(context, rootInActiveWindow, Constants.WECHAT_PACKAGENAME + ":id/hb", JsonUtils.getGson().fromJson(todoTask.params, Params.class).searchkey, false);
    }
}
