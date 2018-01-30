package com.qunar.wechat.auto.utils;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AccNodeHelper {
    private AccNodeHelper() {
    }

    /**
     * 根据给定的node找到第一个可以点击的parent
     *
     * @param child
     * @return
     */
    public static AccessibilityNodeInfo firstClickableParent(AccessibilityNodeInfo child) {
        return firstParentNode(child, new Predicate<AccessibilityNodeInfo>() {
            @Override
            public boolean apply(AccessibilityNodeInfo node) {
                return node != null && node.isClickable();
            }
        });
    }

    public static AccessibilityNodeInfo firstCheckableParent(AccessibilityNodeInfo child) {
        return firstParentNode(child, new Predicate<AccessibilityNodeInfo>() {
            @Override
            public boolean apply(AccessibilityNodeInfo node) {
                return node != null && node.isCheckable();
            }
        });
    }

    /**
     * 查找第一个可以点击的父node
     *
     * @param child
     * @param predicate
     * @return
     */
    public static AccessibilityNodeInfo firstParentNode(AccessibilityNodeInfo child, Predicate<AccessibilityNodeInfo> predicate) {
        if (child == null || predicate.apply(child)) {
            return child;
        }
        AccessibilityNodeInfo parent = child.getParent();
        return firstParentNode(parent, predicate);
    }

    /**
     * 查找第一个满足要求的node
     *
     * @param unfiltered
     * @param predicate
     * @return
     */
    public static AccessibilityNodeInfo firstNode(Collection<AccessibilityNodeInfo> unfiltered, Predicate<AccessibilityNodeInfo> predicate) {
        if (unfiltered == null || unfiltered.isEmpty()) {
            return null;
        }

        Collection<AccessibilityNodeInfo> filtered = Collections2.filter(unfiltered, predicate);
        if (filtered == null || filtered.isEmpty()) {
            return null;
        }

        return filtered.iterator().next();
    }


    /**
     * 点击node，向parent逐级找，直到找到能点击的node
     *
     * @param nodeInfo
     * @return
     */
    public static boolean clickByParent(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return false;
        }

        AccessibilityNodeInfo clickableParent = AccNodeHelper.firstClickableParent(nodeInfo);
        if (clickableParent == null) {
            return false;
        }

        return clickableParent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }

    public static boolean clickByParent(AccessibilityNodeInfo nodeInfo,boolean focus) {
        if (nodeInfo == null) {
            return false;
        }

        AccessibilityNodeInfo clickableParent = AccNodeHelper.firstClickableParent(nodeInfo);
        if (clickableParent == null) {
            return false;
        }
        if(focus){
            clickableParent.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
        }
        return clickableParent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }

    public static boolean clickBySelf(AccessibilityNodeInfo nodeInfo){
        if (nodeInfo == null) {
            return false;
        }
        return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }

    /**
     * 滑动到最底部
     *
     * @param scrollableNodeInfo
     * @return
     */
    public static boolean scrollToEnd(AccessibilityNodeInfo scrollableNodeInfo) {
        if (scrollableNodeInfo == null || !scrollableNodeInfo.isScrollable()) {
            return false;
        }

        boolean scrollResult;
        do {
            scrollResult = scrollableNodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        } while (scrollResult);

        return true;
    }

    public static boolean scrollToNext(AccessibilityNodeInfo scrollableNodeInfo) {
        if (scrollableNodeInfo == null || !scrollableNodeInfo.isScrollable()) {
            return false;
        }
        boolean scrollResult = scrollableNodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);

        return scrollResult;
    }

    /**
     * 滑动到最顶部
     *
     * @param scrollableNodeInfo
     * @return
     */
    public static boolean scrollToTop(AccessibilityNodeInfo scrollableNodeInfo) {
        if (scrollableNodeInfo == null || !scrollableNodeInfo.isScrollable()) {
            return false;
        }

        boolean scrollResult;
        do {
            scrollResult = scrollableNodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
        } while (scrollResult);

        return true;
    }

    /**
     * 查找第一个符合条件的child
     *
     * @param parent
     * @param predicate
     * @return
     */
    public static AccessibilityNodeInfo firstChildNode(AccessibilityNodeInfo parent, Predicate<AccessibilityNodeInfo> predicate) {
        if (parent == null) {
            return null;
        }

        boolean parentResult = predicate.apply(parent);
        if (parentResult) {
            return parent;
        }

        int childCount = parent.getChildCount();
        if (childCount == 0) {
            return null;
        }

        for (int i = 0; i < childCount; i++) {
            AccessibilityNodeInfo currentNode = parent.getChild(i);
            boolean applyResult = predicate.apply(currentNode);
            if (applyResult) {
                return currentNode;
            }

            AccessibilityNodeInfo result = firstChildNode(currentNode, predicate);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    /**
     * 确切地根据Text找到对应的控件
     *
     * @return
     */
    public static List<AccessibilityNodeInfo> findNodesByExactText(AccessibilityNodeInfo rootInActiveWindow, final String text) {
        return findNodesByText(rootInActiveWindow, text, new Predicate<AccessibilityNodeInfo>() {
            @Override
            public boolean apply(AccessibilityNodeInfo nodeInfo) {
                CharSequence textCs = nodeInfo.getText();
                if (textCs == null) {
                    return false;
                }

                return text.equalsIgnoreCase(textCs.toString());
            }
        });
    }

    public static List<AccessibilityNodeInfo> findNodesByStartWithText(AccessibilityNodeInfo rootInActiveWindow, final String text) {
        return findNodesByText(rootInActiveWindow, text, new Predicate<AccessibilityNodeInfo>() {
            @Override
            public boolean apply(AccessibilityNodeInfo nodeInfo) {
                CharSequence textCs = nodeInfo.getText();
                if (textCs == null) {
                    return false;
                }

                return StringUtils.startsWith(textCs, text);
            }
        });
    }

    public static List<AccessibilityNodeInfo> findNodesByContainsWithText(AccessibilityNodeInfo rootInActiveWindow, final String text) {
        return findNodesByText(rootInActiveWindow, text, new Predicate<AccessibilityNodeInfo>() {
            @Override
            public boolean apply(AccessibilityNodeInfo nodeInfo) {
                CharSequence textCs = nodeInfo.getText();
                if (textCs == null) {
                    return false;
                }

                return StringUtils.contains(textCs, text);
            }
        });
    }

    public static List<AccessibilityNodeInfo> findNodesByText(AccessibilityNodeInfo rootInActiveWindow, String text, Predicate<AccessibilityNodeInfo> predicate) {
        if (StringUtils.isBlank(text) || rootInActiveWindow == null || predicate == null) {
            return null;
        }

        List<AccessibilityNodeInfo> nodeInfos = rootInActiveWindow.findAccessibilityNodeInfosByText(text);
        if (nodeInfos == null || nodeInfos.isEmpty()) {
            return null;
        }

        return new ArrayList<>(Collections2.filter(nodeInfos, predicate));
    }

    /**
     * 获取通一个父控件下，满足条件的第一个控件
     *
     * @param current
     * @param predicate
     * @return
     */
    public static AccessibilityNodeInfo firstNodesBySameParent(AccessibilityNodeInfo current, Predicate<AccessibilityNodeInfo> predicate) {
        if (current == null || predicate == null) {
            return null;
        }

        AccessibilityNodeInfo parent = current.getParent();
        if (parent == null || parent.getChildCount() <= 1) {
            return null;
        }

        return firstChildNode(parent, predicate);
    }

    /**
     * 设置文本框的值
     *
     * @param nodeInfo
     * @param text
     * @return
     */
    @TargetApi(18)
    public static boolean setText(Context context, AccessibilityNodeInfo nodeInfo, String text, boolean isforward) {
        if (nodeInfo == null) {
          return false;
        }
        //大于等于5.0 直接setText
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
            return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        }else {
            Bundle arguments = new Bundle();
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT,
                    AccessibilityNodeInfo.MOVEMENT_GRANULARITY_PAGE);
            arguments.putBoolean(AccessibilityNodeInfo.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN,
                    true);
            nodeInfo.performAction(isforward ? AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY
                    : AccessibilityNodeInfo.ACTION_NEXT_AT_MOVEMENT_GRANULARITY, arguments);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            ClipData clip = ClipData.newPlainText(nodeInfo.getClassName(), text);
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(clip);
            return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        }
    }
    /**
     * 根据EditText控件的Label文本内容找到对应的控件，并设置值
     *
     * @param context
     * @param rootInActiveWindow
     * @param labelText
     * @param newContentg
     * @return
     */
    @TargetApi(18)
    public static boolean setEditableNodeTextByLabelText(Context context, AccessibilityNodeInfo rootInActiveWindow,
                                                         String labelText, String newContentg, boolean isForward) {
        List<AccessibilityNodeInfo> labelNodes = AccNodeHelper.findNodesByExactText(rootInActiveWindow, labelText);
        if (labelNodes == null || labelNodes.isEmpty()) {
            return false;
        }

        AccessibilityNodeInfo editableNode = AccNodeHelper.firstNodesBySameParent(labelNodes.get(0), new Predicate<AccessibilityNodeInfo>() {
            @Override
            public boolean apply(AccessibilityNodeInfo input) {
                return input.isEditable();
            }
        });

        if (editableNode == null) {
            return false;
        }

        return AccNodeHelper.setText(context, editableNode, newContentg, isForward);
    }

    /**
     * 根据EditText控件的Label文本内容找到对应的控件，并设置值
     *
     * @param context
     * @param rootInActiveWindow
     * @param viewId
     * @param newContentg
     * @return
     */
    @TargetApi(18)
    public static boolean setEditableNodeTextByViewId(Context context, AccessibilityNodeInfo rootInActiveWindow,
                                                         String viewId, String newContentg, boolean isForward) {
        List<AccessibilityNodeInfo> labelNodes = rootInActiveWindow.findAccessibilityNodeInfosByViewId(viewId);
        if (labelNodes == null || labelNodes.isEmpty()) {
            return false;
        }

        AccessibilityNodeInfo editableNode = AccNodeHelper.firstNodesBySameParent(labelNodes.get(0), new Predicate<AccessibilityNodeInfo>() {
            @Override
            public boolean apply(AccessibilityNodeInfo input) {
                return input.isEditable();
            }
        });

        if (editableNode == null) {
            return false;
        }

        return AccNodeHelper.setText(context, editableNode, newContentg, isForward);
    }

    /**
     * 根据控件id获取edittext里面的文本
     * @param rootInActiveWindow
     * @param viewId
     * @return
     */
    @TargetApi(18)
    public static String getEditableNodeTextByViewId(AccessibilityNodeInfo rootInActiveWindow,String viewId){
        List<AccessibilityNodeInfo> labelNodes = rootInActiveWindow.findAccessibilityNodeInfosByViewId(viewId);
        if (labelNodes == null || labelNodes.isEmpty()) {
            return "";
        }

        AccessibilityNodeInfo editableNode = AccNodeHelper.firstNodesBySameParent(labelNodes.get(0), new Predicate<AccessibilityNodeInfo>() {
            @Override
            public boolean apply(AccessibilityNodeInfo input) {
                return input.isEditable();
            }
        });

        if (editableNode == null) {
            return "";
        }
        return editableNode.getText().toString();
    }
}
