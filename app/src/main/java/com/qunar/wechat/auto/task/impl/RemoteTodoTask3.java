package com.qunar.wechat.auto.task.impl;

import com.qunar.wechat.auto.action.BackAction;
import com.qunar.wechat.auto.action.ClickByDescAction;
import com.qunar.wechat.auto.action.ClickByIdToIndexAction;
import com.qunar.wechat.auto.action.FillMessageAction;
import com.qunar.wechat.auto.action.FillSearchContentAction;
import com.qunar.wechat.auto.action.SendMessageAction;
import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.jsonbean.Params;
import com.qunar.wechat.auto.jsonbean.TodoTask;
import com.qunar.wechat.auto.task.SequentialTaskBase;
import com.qunar.wechat.auto.utils.JsonUtils;

/**
 * Created by lihaibin.li on 2018/1/29.
 */

public class RemoteTodoTask3 extends SequentialTaskBase {
    public RemoteTodoTask3(TodoTask todoTask){
        super(new ClickByDescAction("搜索"),
                new FillSearchContentAction(todoTask),
                new ClickByIdToIndexAction(Constants.WECHAT_PACKAGENAME + ":id/jv",1),
                new FillMessageAction(Constants.WECHAT_PACKAGENAME + ":id/a9o",todoTask),
                new SendMessageAction("发送",todoTask),
                new BackAction(),
                new BackAction());
        Constants.AUTO_SEND_WX_MESSAGE = JsonUtils.getGson().fromJson(todoTask.params, Params.class).message;
    }
}
