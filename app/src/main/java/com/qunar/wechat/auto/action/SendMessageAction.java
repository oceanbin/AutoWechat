package com.qunar.wechat.auto.action;

import android.content.Context;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.jsonbean.Params;
import com.qunar.wechat.auto.jsonbean.TodoTask;
import com.qunar.wechat.auto.utils.JsonUtils;

/**
 * 发送消息
 * Created by lihaibin.li on 2017/11/21.
 */

public class SendMessageAction extends ClickByTextCommonAction implements Action{
    private TodoTask todoTask;
    public SendMessageAction(String controlText){
        super(controlText);
    }
    public SendMessageAction(String controlText, TodoTask todoTask){
        super(controlText);
        this.todoTask = todoTask;
    }
    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        if(TextUtils.isEmpty(Constants.AUTO_SEND_WX_MESSAGE))
            return true;

        if (todoTask != null) {//如果任务不为空 并且时间大于当前时间 失效 不发送
            String deadline = JsonUtils.getGson().fromJson(todoTask.params, Params.class).deadline;
            if (!TextUtils.isEmpty(deadline)) {
                if (System.currentTimeMillis() > Long.parseLong(deadline))
                    return true;
            }

        }
        return super.execute(context,event,rootInActiveWindow);
    }
}
