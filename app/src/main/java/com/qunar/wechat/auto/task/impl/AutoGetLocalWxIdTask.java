package com.qunar.wechat.auto.task.impl;

import com.qunar.wechat.auto.action.BackAction;
import com.qunar.wechat.auto.action.ClickByTextCommonAction;
import com.qunar.wechat.auto.action.GetWxIdAction;
import com.qunar.wechat.auto.task.SequentialTaskBase;

/**
 * Created by lihaibin.li on 2017/11/21.
 */

public class AutoGetLocalWxIdTask extends SequentialTaskBase {
    public AutoGetLocalWxIdTask(){
        super(new ClickByTextCommonAction("我"),
                new GetWxIdAction(),
                new BackAction());
    }

}
