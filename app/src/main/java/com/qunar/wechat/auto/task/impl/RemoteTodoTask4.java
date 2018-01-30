package com.qunar.wechat.auto.task.impl;

import com.qunar.wechat.auto.action.BackAction;
import com.qunar.wechat.auto.action.ClickByDescAction;
import com.qunar.wechat.auto.action.ClickByTextCommonAction;
import com.qunar.wechat.auto.action.FillSearchContentAction;
import com.qunar.wechat.auto.action.SearchFriendAction;
import com.qunar.wechat.auto.jsonbean.Params;
import com.qunar.wechat.auto.jsonbean.TodoTask;
import com.qunar.wechat.auto.task.SequentialTaskBase;
import com.qunar.wechat.auto.utils.JsonUtils;
import com.qunar.wechat.auto.utils.Utils;

/**
 * Created by lihaibin.li on 2018/1/29.
 */

public class RemoteTodoTask4 extends SequentialTaskBase {
    public RemoteTodoTask4(TodoTask todoTask){
        super(new ClickByDescAction("搜索"),
                new FillSearchContentAction(todoTask),
                new ClickByTextCommonAction((Utils.isNum(JsonUtils.getGson().fromJson(todoTask.params, Params.class).searchkey)
                        ? "查找手机/QQ号:" : "查找微信号:")
                        + JsonUtils.getGson().fromJson(todoTask.params, Params.class).searchkey),
                new SearchFriendAction(todoTask),
                new BackAction()
           );
    }

}
