package com.qunar.wechat.auto.mission;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.jsonbean.TodoTask;
import com.qunar.wechat.auto.task.impl.RemoteTodoTask3;

/**
 * 自动执行远程任务 任务类型3 searchkey 为关键字搜索，若搜到唯一的人，将
    message字段的内容作为消息发送出去
 * Created by lihaibin.li on 2018/1/29.
 */

public class AutoTaskMission3 extends BaseTaskMission {
    private RemoteTodoTask3 remoteTodoTask3;
    private int index;
    private TodoTask todoTask;

    @Override
    public void trigger(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        if (remoteTodoTask3 != null && !remoteTodoTask3.isAccomplished()) {
            remoteTodoTask3.trigger(context, event, rootInActiveWindow);
            return;
        }
        if (index == Constants.todoTasks3.size()) {
            isAccomplished = true;
            return;
        }

        //第一个任务可能有不准的情况
        setTaskDone(todoTask == null ? Constants.todoTasks3.get(0).id : todoTask.id);

        todoTask = Constants.todoTasks3.get(index);
        remoteTodoTask3 = new RemoteTodoTask3(todoTask);
        index++;
        remoteTodoTask3.trigger(context, event, rootInActiveWindow);
    }
}
