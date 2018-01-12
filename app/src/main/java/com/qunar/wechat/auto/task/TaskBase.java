package com.qunar.wechat.auto.task;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by lihaibin.li on 2017/11/9.
 */

public abstract class TaskBase implements Task{
    private boolean isPaused = false;
    private boolean isStopped = false;

    private boolean isAccomplished = false;

    protected void setTaskAccomplished() {
        stop();
    }

    protected boolean isStopped() {
        return this.isStopped;
    }

    protected boolean isPaused() {
        return isPaused;
    }

    protected boolean canProceed() {
        return !this.isPaused && !this.isStopped && !this.isAccomplished;
    }

    @Override
    public abstract void trigger(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow);

    @Override
    public void pause() {
        this.isPaused = true;
    }

    @Override
    public void resume() {
        this.isPaused = false;
    }

    @Override
    public void stop() {
        this.isPaused = true;
        this.isStopped = true;
        this.isAccomplished = true;
    }

    @Override
    public boolean isAccomplished() {
        return this.isAccomplished;
    }
}
