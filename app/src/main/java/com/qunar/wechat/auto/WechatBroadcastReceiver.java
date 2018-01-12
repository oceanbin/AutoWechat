package com.qunar.wechat.auto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.event.WechatDBEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lihaibin.li on 2018/1/12.
 */

public class WechatBroadcastReceiver extends BroadcastReceiver {
    WechatDBEvent wechatDBEvent = new WechatDBEvent();
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null){
            int count = intent.getIntExtra(Constants.SP_MAX_FRIENDS_COUNT,0);
            boolean hasMsg = intent.getBooleanExtra(Constants.SP_IS_HAS_MESSAGE_UPLOAD,true);
            wechatDBEvent.hasMsg = hasMsg;
            wechatDBEvent.count = count;
            EventBus.getDefault().post(wechatDBEvent);
        }
    }
}
