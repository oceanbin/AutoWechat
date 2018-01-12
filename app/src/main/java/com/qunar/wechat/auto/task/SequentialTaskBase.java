package com.qunar.wechat.auto.task;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.action.Action;

import java.util.LinkedList;

/**
 * Created by lihaibin.li on 2017/11/9.
 */

public class SequentialTaskBase extends TaskBase{
    protected LinkedList<Action> actionList = new LinkedList<>();

    public SequentialTaskBase(Action... actions) {
        if (actions == null || actions.length == 0) {
            return;
        }

        for (Action action : actions) {
            actionList.add(action);
        }
    }

    @Override
    public void trigger(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        doNextAction(context, event, rootInActiveWindow);
    }

    protected boolean doNextAction(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        if (rootInActiveWindow == null) {
            return false;
        }

        if (!canProceed()) {
            return true;
        }

        if (this.actionList == null || this.actionList.isEmpty()) {
            setTaskAccomplished();
            return true;
        }

        Action action = this.actionList.getFirst();
        boolean executeResult = action.execute(context, event, rootInActiveWindow);
        if (executeResult) {
            this.actionList.removeFirst();
        }

        if (this.actionList == null || this.actionList.isEmpty()) {
            setTaskAccomplished();
            return true;
        }

        return executeResult;
    }
}
