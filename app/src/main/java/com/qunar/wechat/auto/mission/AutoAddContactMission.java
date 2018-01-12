package com.qunar.wechat.auto.mission;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.google.common.base.Predicate;
import com.qunar.wechat.auto.task.Task;
import com.qunar.wechat.auto.task.impl.AddContactFriendTask;
import com.qunar.wechat.auto.task.impl.GoToContactFriendViewTask;
import com.qunar.wechat.auto.utils.AccNodeHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lihaibin.li on 2017/12/14.
 */

public class AutoAddContactMission implements Mission {
    private static Map<String,AccessibilityNodeInfo> completedAddMap = new HashMap<>();
    private boolean isAccomplished;
    private Task goToContactFriendViewTask = new GoToContactFriendViewTask();
    private AddContactFriendTask currentAddFriendTask;
    @Override
    public boolean isAccomplished() {
        return isAccomplished;
    }

    @Override
    public void trigger(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        // 打开查看手机通讯录页面
        if (!goToContactFriendViewTask.isAccomplished()) {
            goToContactFriendViewTask.trigger(context, event, rootInActiveWindow);
            return;
        }

        if (currentAddFriendTask != null && !currentAddFriendTask.isAccomplished()) {
            currentAddFriendTask.trigger(context, event, rootInActiveWindow);
            return;
        }

        List<AccessibilityNodeInfo> viewContactsLableNodes = AccNodeHelper.findNodesByExactText(rootInActiveWindow, "查看手机通讯录");
        if (viewContactsLableNodes == null || viewContactsLableNodes.isEmpty()) {
            // 不是到达查看通讯录页面触发的事件
            return;
        }

        currentAddFriendTask = getNextAddFriendTask(rootInActiveWindow);
        if (currentAddFriendTask == null) {
            AccessibilityNodeInfo scrollListNode = AccNodeHelper.firstChildNode(rootInActiveWindow, new Predicate<AccessibilityNodeInfo>() {
                @Override
                public boolean apply(AccessibilityNodeInfo nodeInfo) {
                    return nodeInfo != null && nodeInfo.isScrollable();
                }
            });
            boolean scroolResult = scrollListNode != null && scrollListNode.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            if (!scroolResult) {
                // 列表上一个操作时已滚动到最底
                this.isAccomplished = true;
                Toast.makeText(context,"本次自动加好友操作完成！！！",Toast.LENGTH_LONG).show();
            }

            // 直接返回，等待下次触发
            return;
        }
        currentAddFriendTask.trigger(context, event, rootInActiveWindow);
    }

    // region Private Methods
    private static AddContactFriendTask getNextAddFriendTask(AccessibilityNodeInfo rootInActiveWindow) {
        List<AccessibilityNodeInfo> wechatNameNodes = AccNodeHelper.findNodesByStartWithText(rootInActiveWindow, "1");
        if (wechatNameNodes == null || wechatNameNodes.isEmpty()) {
            return null;
        }

        for (AccessibilityNodeInfo wechatNameNode : wechatNameNodes) {
            String mobile = null;
            try {
                AccessibilityNodeInfo uidNode = AccNodeHelper.firstChildNode(wechatNameNode.getParent(), new Predicate<AccessibilityNodeInfo>() {
                    @Override
                    public boolean apply(AccessibilityNodeInfo nodeInfo) {
                        if (nodeInfo == null || nodeInfo.getText() == null) {
                            return false;
                        }
                        String text = nodeInfo.getText().toString();
                        return text.length() == 11;
                    }
                });
                if (uidNode == null) {
                    continue;
                }

                mobile = uidNode.getText().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            AccessibilityNodeInfo addButtonNode = AccNodeHelper.firstChildNode(wechatNameNode.getParent(), new Predicate<AccessibilityNodeInfo>() {
                @Override
                public boolean apply(AccessibilityNodeInfo nodeInfo) {
                    if (nodeInfo == null || nodeInfo.getText() == null) {
                        return false;
                    }
                    String text = nodeInfo.getText().toString();
                    return "添加".equalsIgnoreCase(text);
                }
            });
            if(addButtonNode == null)
                continue;

            if (completedAddMap.containsKey(mobile)) {
                // 已邀请过
                continue;
            }

            AccessibilityNodeInfo wechatNode = AccNodeHelper.firstChildNode(wechatNameNode.getParent(), new Predicate<AccessibilityNodeInfo>() {
                @Override
                public boolean apply(AccessibilityNodeInfo nodeInfo) {
                    CharSequence textCs = nodeInfo.getText();
                    if (textCs == null) {
                        return false;
                    }
                    return textCs.toString().contains("微信:");
//                    return "添加".equalsIgnoreCase(textCs.toString());
                }
            });

            if (wechatNode == null) {
                continue;
            }
            completedAddMap.put(mobile,wechatNode);
            return new AddContactFriendTask(wechatNode,mobile);
        }

        return null;
    }
    // endregion
}
