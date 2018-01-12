package com.qunar.wechat.auto.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.mission.AutoAddContactMission;
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
        //当前微信id为空 获取wxid
//        if (TextUtils.isEmpty(Constants.LOCAL_WX_ID)) {
            if (!autoGetLocalWxIdTask.isAccomplished())
                autoGetLocalWxIdTask.trigger(this, event, getRootInActiveWindow());
//        }
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
        }


    }

    @Override
    public void onInterrupt() {

    }


}
