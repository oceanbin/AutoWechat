package com.qunar.wechat.auto.task.impl;

import com.qunar.wechat.auto.action.AcceptFriendRequestAction;
import com.qunar.wechat.auto.action.ChatBackAction;
import com.qunar.wechat.auto.action.ClickByTextCommonAction;
import com.qunar.wechat.auto.action.ClickByTextOrIdAction;
import com.qunar.wechat.auto.action.CountAction;
import com.qunar.wechat.auto.action.FillMessageAction;
import com.qunar.wechat.auto.action.FillRemarkInfoAction;
import com.qunar.wechat.auto.action.SendMessageAction;
import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.task.SequentialTaskBase;

/**
 * 自动接收好友请求 并修改备注 发送话术 以及统计添加成功个数
 * Created by lihaibin.li on 2017/11/9.
 */

public class AutoAcceptFriendRequestTask extends SequentialTaskBase {
    public AutoAcceptFriendRequestTask() {
        super(new ClickByTextCommonAction("通讯录"),
                new ClickByTextOrIdAction("新的朋友",Constants.WECHAT_PACKAGENAME + ":id/b4g"),
                new AcceptFriendRequestAction(),
                new FillRemarkInfoAction(Constants.WECHAT_PACKAGENAME + ":id/cpl"),
                new ClickByTextCommonAction("完成"),
                new CountAction(),
                new ClickByTextCommonAction("发消息"),
                new FillMessageAction(Constants.WECHAT_PACKAGENAME + ":id/a9o"),
                new SendMessageAction("发送"),
                new ChatBackAction());
    }
}
