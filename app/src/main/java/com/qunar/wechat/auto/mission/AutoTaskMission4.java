package com.qunar.wechat.auto.mission;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.jsonbean.TodoTask;
import com.qunar.wechat.auto.task.impl.RemoteTodoTask4;

/**
 * 自动执行远程任务 任务类型4 主动添加好友并修改备注
 * Created by lihaibin.li on 2018/1/29.
 */

public class AutoTaskMission4 extends BaseTaskMission {
    private RemoteTodoTask4 remoteTodoTask4;
    private int index;
    private TodoTask todoTask;

    @Override
    public void trigger(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        if (remoteTodoTask4 != null && !remoteTodoTask4.isAccomplished()) {
            remoteTodoTask4.trigger(context, event, rootInActiveWindow);
            return;
        }
        if (index == Constants.todoTasks4.size()) {
            isAccomplished = true;
            return;
        }

        //第一个任务可能有不准的情况
        setTaskDone(todoTask == null ? Constants.todoTasks4.get(0).id : todoTask.id);

        todoTask = Constants.todoTasks4.get(index);
        remoteTodoTask4 = new RemoteTodoTask4(todoTask);
        index++;
        remoteTodoTask4.trigger(context, event, rootInActiveWindow);
    }
}
