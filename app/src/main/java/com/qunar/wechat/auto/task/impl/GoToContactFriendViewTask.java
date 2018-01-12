package com.qunar.wechat.auto.task.impl;

import com.qunar.wechat.auto.action.ClickByTextCommonAction;
import com.qunar.wechat.auto.action.ClickByTextOrIdAction;
import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.task.SequentialTaskBase;

/**
 * 自动添加通讯录好友Task
 * Created by lihaibin.li on 2017/11/9.
 */

public class GoToContactFriendViewTask extends SequentialTaskBase {
    public GoToContactFriendViewTask(){
        super(new ClickByTextCommonAction("通讯录"),
                new ClickByTextOrIdAction("新的朋友",Constants.WECHAT_PACKAGENAME + ":id/b4g"),
                new ClickByTextCommonAction("添加朋友"),
                new ClickByTextCommonAction("手机联系人"));
    }
}
