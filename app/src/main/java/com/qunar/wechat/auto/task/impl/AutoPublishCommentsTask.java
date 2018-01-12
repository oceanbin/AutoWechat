package com.qunar.wechat.auto.task.impl;

import com.qunar.wechat.auto.action.BackAction;
import com.qunar.wechat.auto.action.ClickByContainTextAction;
import com.qunar.wechat.auto.action.ClickByTextCommonAction;
import com.qunar.wechat.auto.action.ClickByTextOrIdAction;
import com.qunar.wechat.auto.action.SelectPicAction;
import com.qunar.wechat.auto.action.SetContentForEditTextByIdAction;
import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.task.SequentialTaskBase;

/**
 * 自动发布朋友圈
 * Created by lihaibin.li on 2017/12/19.
 */

public class AutoPublishCommentsTask extends SequentialTaskBase {
    public AutoPublishCommentsTask(String content){
        super(new ClickByTextCommonAction("发现"),
                new ClickByTextCommonAction("朋友圈"),
                new ClickByTextOrIdAction("test", Constants.WECHAT_PACKAGENAME + ":id/fu"),
                new ClickByTextCommonAction("从相册选择"),
                new SelectPicAction(),
                new ClickByContainTextAction("完成"),
                new SetContentForEditTextByIdAction(Constants.WECHAT_PACKAGENAME + ":id/d40",content),
                new ClickByTextCommonAction("发送"),
                new BackAction());
    }
}
