package com.qunar.wechat.auto.task.impl;

import com.qunar.wechat.auto.action.BackAction;
import com.qunar.wechat.auto.action.ClickByDescAction;
import com.qunar.wechat.auto.action.ClickByIdAction;
import com.qunar.wechat.auto.action.ClickByTextCommonAction;
import com.qunar.wechat.auto.action.ClickByIdToIndexAction;
import com.qunar.wechat.auto.action.FillMessageAction;
import com.qunar.wechat.auto.action.FillRemarkInfoAction3;
import com.qunar.wechat.auto.action.FillSearchContentAction;
import com.qunar.wechat.auto.action.SendMessageAction;
import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.jsonbean.Params;
import com.qunar.wechat.auto.jsonbean.TodoTask;
import com.qunar.wechat.auto.task.SequentialTaskBase;
import com.qunar.wechat.auto.utils.JsonUtils;

/**
 * Created by lihaibin.li on 2018/1/23.
 */

public class RemoteTodoTask extends SequentialTaskBase {
    public RemoteTodoTask(TodoTask todoTask){
        super(new ClickByDescAction("搜索"),
                new FillSearchContentAction(todoTask),
                new ClickByIdToIndexAction(Constants.WECHAT_PACKAGENAME + ":id/jv",1),
                new ClickByDescAction("聊天信息"),
                new ClickByIdAction(Constants.WECHAT_PACKAGENAME + ":id/cp_"),
                new ClickByTextCommonAction("设置备注和标签"),
                new FillRemarkInfoAction3(JsonUtils.getGson().fromJson(todoTask.params, Params.class).remark),
                new ClickByTextCommonAction("完成"),
                new ClickByTextCommonAction("发消息"),
                new FillMessageAction(Constants.WECHAT_PACKAGENAME + ":id/a9o"),
                new SendMessageAction("发送"),
                new BackAction());
        Constants.AUTO_SEND_WX_MESSAGE = JsonUtils.getGson().fromJson(todoTask.params, Params.class).message;
    }
}
