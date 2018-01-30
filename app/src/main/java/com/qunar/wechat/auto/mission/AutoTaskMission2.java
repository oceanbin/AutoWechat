package com.qunar.wechat.auto.mission;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.jsonbean.TodoTask;
import com.qunar.wechat.auto.task.impl.RemoteTodoTask2;

/**
 * 自动执行远程任务 任务类型2 搜人，给这个人发送消息，然后修改备注
 * Created by lihaibin.li on 2018/1/23.
 */

public class AutoTaskMission2 extends BaseTaskMission {

    private RemoteTodoTask2 remoteTodoTask2;
    private int index;
    private TodoTask todoTask;

    @Override
    public void trigger(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        if (remoteTodoTask2 != null && !remoteTodoTask2.isAccomplished()) {
            remoteTodoTask2.trigger(context, event, rootInActiveWindow);
            return;
        }
        if (index == Constants.todoTasks2.size()) {
            isAccomplished = true;
            return;
        }

        //第一个任务可能有不准的情况
        setTaskDone(todoTask == null ? Constants.todoTasks2.get(0).id : todoTask.id);

        todoTask = Constants.todoTasks2.get(index);
        remoteTodoTask2 = new RemoteTodoTask2(todoTask);
        index++;
        remoteTodoTask2.trigger(context, event, rootInActiveWindow);
    }
}
