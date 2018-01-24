package com.qunar.wechat.auto.mission;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.api.WechatApi;
import com.qunar.wechat.auto.common.BackgroundExecutor;
import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.jsonbean.TodoTask;
import com.qunar.wechat.auto.task.impl.RemoteTodoTask;

/**
 * 自动执行远程任务
 * Created by lihaibin.li on 2018/1/23.
 */

public class AutoTaskMission implements Mission {
    private boolean isAccomplished;
    private RemoteTodoTask remoteTodoTask;
    private int index;
    private TodoTask todoTask;

    @Override
    public void trigger(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        if (remoteTodoTask != null && !remoteTodoTask.isAccomplished()) {
            remoteTodoTask.trigger(context, event, rootInActiveWindow);
            return;
        }
        if (index == Constants.todoTasks.size()) {
            isAccomplished = true;
            return;
        }

        //第一个任务可能有不准的情况
        setTaskDone(todoTask == null ? Constants.todoTasks.get(0).id : todoTask.id);

        todoTask = Constants.todoTasks.get(index);
        remoteTodoTask = new RemoteTodoTask(todoTask);
        index++;
        remoteTodoTask.trigger(context, event, rootInActiveWindow);
    }

    private void setTaskDone(final int id) {
        BackgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                WechatApi.setTaskDone(id);
            }
        });
    }

    @Override
    public boolean isAccomplished() {
        return isAccomplished;
    }
}
