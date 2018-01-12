package com.qunar.wechat.auto.action;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.utils.AccNodeHelper;
import com.qunar.wechat.auto.utils.RandomUtil;

/**
 * Created by lihaibin.li on 2017/12/14.
 */

public class ClickAddFriendAction implements Action{
    private AccessibilityNodeInfo needAddFriendBtnNode;

    public ClickAddFriendAction(AccessibilityNodeInfo needAddFriendBtnNode) {
        this.needAddFriendBtnNode = needAddFriendBtnNode;
    }

    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        try{
            Thread.sleep(RandomUtil.getRandomSeconds());
        }catch (Exception e){
            e.printStackTrace();
        }

        return AccNodeHelper.clickByParent(this.needAddFriendBtnNode);
    }
}
