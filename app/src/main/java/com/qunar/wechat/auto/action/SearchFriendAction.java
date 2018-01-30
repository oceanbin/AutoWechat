package com.qunar.wechat.auto.action;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.jsonbean.Params;
import com.qunar.wechat.auto.jsonbean.TodoTask;
import com.qunar.wechat.auto.utils.AccNodeHelper;
import com.qunar.wechat.auto.utils.JsonUtils;

import java.util.List;

/**
 * Created by lihaibin.li on 2018/1/30.
 */

public class SearchFriendAction extends BaseAction implements Action {
    private TodoTask todoTask;
    private boolean isFinish;
    private boolean isClickAddRemark;
    public SearchFriendAction(TodoTask todoTask){
        this.todoTask = todoTask;
    }
    @TargetApi(18)
    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        //用户不存在
        List<AccessibilityNodeInfo> noUsrInfo = rootInActiveWindow.findAccessibilityNodeInfosByText("用户不存在");
        if(noUsrInfo != null && !noUsrInfo.isEmpty()){
            List<AccessibilityNodeInfo> okNodeInfos = rootInActiveWindow.findAccessibilityNodeInfosByViewId(Constants.WECHAT_PACKAGENAME + ":id/ajg");
            if(okNodeInfos != null && !okNodeInfos.isEmpty()){
                AccNodeHelper.clickByParent(okNodeInfos.get(0));
            }
            return true;
        }
        //已经是好友
        List<AccessibilityNodeInfo> alreadyUserInfo = rootInActiveWindow.findAccessibilityNodeInfosByText("发消息");
        if(alreadyUserInfo != null && !alreadyUserInfo.isEmpty()){
            back(rootInActiveWindow);
            return true;
        }
        //设置备注标签
        List<AccessibilityNodeInfo> addRemarkNodeInfo = rootInActiveWindow.findAccessibilityNodeInfosByText("设置备注和标签");
        if(addRemarkNodeInfo !=null && !addRemarkNodeInfo.isEmpty() && !isClickAddRemark){
            AccNodeHelper.clickByParent(addRemarkNodeInfo.get(0));
            isClickAddRemark = true;
            sleep(2000);
        }

        //添加备注
        List<AccessibilityNodeInfo> fillRemarkInfo = rootInActiveWindow.findAccessibilityNodeInfosByViewId(Constants.WECHAT_PACKAGENAME + ":id/amj");
        if(fillRemarkInfo !=null && !fillRemarkInfo.isEmpty()){
            AccNodeHelper.setText(context,fillRemarkInfo.get(0), JsonUtils.getGson().fromJson(todoTask.params, Params.class).remark,false);
        }

        //保存备注
        List<AccessibilityNodeInfo> saveRemarkNodeInfo = rootInActiveWindow.findAccessibilityNodeInfosByText("保存");
        if(saveRemarkNodeInfo !=null && !saveRemarkNodeInfo.isEmpty()){
            AccNodeHelper.clickByParent(saveRemarkNodeInfo.get(0));
            sleep(2500);
        }

        //添加到通讯录
        List<AccessibilityNodeInfo> addToContactNodeInfo = rootInActiveWindow.findAccessibilityNodeInfosByText("添加到通讯录");
        if(addToContactNodeInfo!=null && !addToContactNodeInfo.isEmpty() && !isFinish){
            AccNodeHelper.clickByParent(addToContactNodeInfo.get(0));
            sleep(2000);
        }

        //发送
        List<AccessibilityNodeInfo> sendRemarkNodeInfo = rootInActiveWindow.findAccessibilityNodeInfosByViewId(Constants.WECHAT_PACKAGENAME + ":id/gy");
        if(sendRemarkNodeInfo !=null && !sendRemarkNodeInfo.isEmpty()){
            AccNodeHelper.clickByParent(sendRemarkNodeInfo.get(0));
            sleep(3000);
            isFinish = true;
            return false;
        }

        if(isFinish){
            back(rootInActiveWindow);
            sleep(1000);
            return true;
        }

        return false;
    }

    @TargetApi(18)
    private void back(AccessibilityNodeInfo rootInActiveWindow){
        List<AccessibilityNodeInfo> backNodeInfos = rootInActiveWindow.findAccessibilityNodeInfosByViewId(Constants.WECHAT_PACKAGENAME + ":id/hg");
        if(backNodeInfos != null && !backNodeInfos.isEmpty()){
            AccNodeHelper.clickByParent(backNodeInfos.get(0));
        }
    }
}
