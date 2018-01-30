package com.qunar.wechat.auto.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.mission.AutoAddContactMission;
import com.qunar.wechat.auto.mission.AutoTaskMission2;
import com.qunar.wechat.auto.mission.AutoTaskMission3;
import com.qunar.wechat.auto.mission.AutoTaskMission4;
import com.qunar.wechat.auto.task.impl.AutoAcceptFriendRequestTask;
import com.qunar.wechat.auto.task.impl.AutoGetLocalWxIdTask;
import com.qunar.wechat.auto.task.impl.AutoPublishCommentsTask;

/**
 * Created by lihaibin.li on 2017/11/9.
 */

public class AutoWechatService extends AccessibilityService {
    private static final String TAG = "AutoWechatService";

    AutoAcceptFriendRequestTask autoAcceptFriendRequestTask = new AutoAcceptFriendRequestTask();
    AutoGetLocalWxIdTask autoGetLocalWxIdTask = new AutoGetLocalWxIdTask();
    AutoAddContactMission autoAddContactMission = new AutoAddContactMission();
    AutoPublishCommentsTask autoPublishCommentsTask;
    AutoTaskMission2 autoTaskMission2 = new AutoTaskMission2();
    AutoTaskMission3 autoTaskMission3 = new AutoTaskMission3();
    AutoTaskMission4 autoTaskMission4 = new AutoTaskMission4();

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        String pkn = String.valueOf(accessibilityEvent.getPackageName());
        Log.d(TAG, pkn);
        if (!Constants.WECHAT_PACKAGENAME.equals(pkn)) {
            return;
        }

        onEvent(accessibilityEvent);
    }

    @TargetApi(16)
    private void onEvent(AccessibilityEvent event) {
        if (!autoGetLocalWxIdTask.isAccomplished()) {
            autoGetLocalWxIdTask.trigger(this, event, getRootInActiveWindow());
        }else {
            switch (Constants.autoType) {
                case AUTO_ACCEPT_FRIEND_REQUEST:
                    //开启自动接受好友请求
                    if (!autoAcceptFriendRequestTask.isAccomplished()) {
                        autoAcceptFriendRequestTask.trigger(this, event, getRootInActiveWindow());
                    } else {
                        autoAcceptFriendRequestTask = new AutoAcceptFriendRequestTask();
                        autoAcceptFriendRequestTask.trigger(this, event, getRootInActiveWindow());
                    }
                    break;
                case AUTO_ADD_CONTACT_FRIEND:
                    if (!autoAddContactMission.isAccomplished()) {
                        autoAddContactMission.trigger(this, event, getRootInActiveWindow());
                    }
                    break;
                case AUTO_PUBLISH_WECHAT_COMMENTS:
                    if (autoPublishCommentsTask == null)
                        autoPublishCommentsTask = new AutoPublishCommentsTask(Constants.WECHAT_COMMENTS_CONTENT);
                    if (!autoPublishCommentsTask.isAccomplished()) {
                        autoPublishCommentsTask.trigger(this, event, getRootInActiveWindow());
                    }
                    break;
                case AUTO_EXCUTE_TODO_TASK:
                    if (Constants.todoTasks2 != null && !Constants.todoTasks2.isEmpty()) {
                        if (!autoTaskMission2.isAccomplished()) {
                            autoTaskMission2.trigger(this, event, getRootInActiveWindow());
                            return;
                        }
                    }
                    if (Constants.todoTasks3 != null && !Constants.todoTasks3.isEmpty()) {
                        if (!autoTaskMission3.isAccomplished()) {
                            autoTaskMission3.trigger(this, event, getRootInActiveWindow());
                            return;
                        }
                    }
                    if (Constants.todoTasks4 != null && !Constants.todoTasks4.isEmpty()) {
                        if (!autoTaskMission4.isAccomplished()) {
                            autoTaskMission4.trigger(this, event, getRootInActiveWindow());
                            return;
                        }
                    }
                    break;
            }
        }



    }

    @Override
    public void onInterrupt() {

    }


}
