package com.qunar.wechat.auto.action;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.utils.AccNodeHelper;

import java.util.List;

/**
 * Created by lihaibin.li on 2017/12/19.
 */

public class SetContentForEditTextByIdAction implements Action {
    private String id;
    private String content;
    public SetContentForEditTextByIdAction(String id,String content){
        this.id = id;
        this.content = content;
    }
    @TargetApi(18)
    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        List<AccessibilityNodeInfo> idNodeInfos =  rootInActiveWindow.findAccessibilityNodeInfosByViewId(id);
        if(idNodeInfos == null || idNodeInfos.isEmpty()){
            return false;
        }
        return AccNodeHelper.setEditableNodeTextByViewId(context,idNodeInfos.get(0),id,content,false);
    }
}
