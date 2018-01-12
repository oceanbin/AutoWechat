package com.qunar.wechat.auto.task.impl;

import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.action.Action;
import com.qunar.wechat.auto.action.ClickAddFriendAction;
import com.qunar.wechat.auto.action.ClickByTextCommonAction;
import com.qunar.wechat.auto.action.FillRemarkInfoAction2;
import com.qunar.wechat.auto.action.IfNoTextFinishAction;
import com.qunar.wechat.auto.action.SpecialBackAction;
import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.task.SequentialTaskBase;

/**
 * Created by lihaibin.li on 2017/12/14.
 */

public class AddContactFriendTask extends SequentialTaskBase {
    public AddContactFriendTask(AccessibilityNodeInfo needAddFriendBtnNode,String mobile){
        super(new ClickAddFriendAction(needAddFriendBtnNode),
                new ClickByTextCommonAction("设置备注和标签"),
                new FillRemarkInfoAction2(mobile, Constants.WECHAT_PACKAGENAME + ":id/amj"),
                new ClickByTextCommonAction("保存"),
                new ClickByTextCommonAction("添加到通讯录"),
                new IfNoTextFinishAction("发送"),
                new SpecialBackAction("详细资料"));
    }

    public Action getFirstAction(){
        if(!actionList.isEmpty())
            return actionList.get(0);
        else return null;
    }

    public void removeAction(){
        if(!actionList.isEmpty()){
            actionList.removeFirst();
        }
    }
}
