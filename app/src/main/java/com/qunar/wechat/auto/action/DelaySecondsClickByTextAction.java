package com.qunar.wechat.auto.action;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by lihaibin.li on 2017/12/18.
 */

public class DelaySecondsClickByTextAction extends ClickByTextCommonAction {
    boolean isDelay;
    public DelaySecondsClickByTextAction(String text){
        super(text);
    }
    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        if(!isDelay){
            try {
                Thread.sleep(3000);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                isDelay = true;
            }
        }
        return super.execute(context, event, rootInActiveWindow);
    }
}
