package com.qunar.wechat.auto.action;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.google.common.base.Predicate;
import com.qunar.wechat.auto.utils.AccNodeHelper;

import java.util.List;

/**
 * Created by lihaibin.li on 2017/11/17.
 */
@TargetApi(18)
public class AcceptFriendRequestAction extends ClickByTextCommonAction {
    private final String TAG = AcceptFriendRequestAction.class.getSimpleName();
    @Override
    public boolean execute(Context context, AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {
        List<AccessibilityNodeInfo> checkNodes = rootInActiveWindow.findAccessibilityNodeInfosByText("添加朋友");
        if (checkNodes == null || checkNodes.isEmpty()) {
            return false;
        }

        List<AccessibilityNodeInfo> nfs = rootInActiveWindow.findAccessibilityNodeInfosByText("新的朋友");
        if (nfs == null || nfs.isEmpty()) {
            return false;
        }

        List<AccessibilityNodeInfo> srollInfos = rootInActiveWindow.findAccessibilityNodeInfosByText("已添加");


        List<AccessibilityNodeInfo> nodeInfos = AccNodeHelper.findNodesByExactText(rootInActiveWindow, "接受");
        if (nodeInfos == null || nodeInfos.isEmpty()) {//没有接受之后 滚动
            scrollView(srollInfos);
            return false;
        }

        Log.d(TAG,String.format("点击[%s]", "接受"));
        return AccNodeHelper.clickByParent(nodeInfos.get(0));
    }

    private void scrollView(List<AccessibilityNodeInfo> nodeInfos){
        AccessibilityNodeInfo scrollableListView = null;
        for (AccessibilityNodeInfo nodeInfo : nodeInfos) {
            scrollableListView = AccNodeHelper.firstParentNode(nodeInfo, new Predicate<AccessibilityNodeInfo>() {
                @Override
                public boolean apply(AccessibilityNodeInfo input) {
                    return input.isScrollable();
                }
            });

            if (scrollableListView != null) {
                break;
            }
        }

        if (scrollableListView == null) {
            return;
        }

        Log.d(TAG,"执行ScrollNewFriendViewAction");
//        try{
//            Thread.sleep(RandomUtil.getRandomSeconds());
            AccNodeHelper.scrollToNext(scrollableListView);
//        }catch (InterruptedException e){
//
//        }
    }
}
