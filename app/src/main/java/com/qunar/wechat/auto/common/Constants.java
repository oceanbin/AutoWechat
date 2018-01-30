package com.qunar.wechat.auto.common;
import com.qunar.wechat.auto.jsonbean.TodoTask;

import java.util.List;

/**
 * Created by lihaibin.li on 2017/11/9.
 */

public class Constants {
    public static AutoType autoType = AutoType.AUTO_ACCEPT_FRIEND_REQUEST;

    public static String LOCAL_WX_ID;
    public static String AUTO_SEND_WX_MESSAGE;

    public static String WECHAT_COMMENTS_CONTENT;

    public static List<TodoTask> todoTasks2;

    public static List<TodoTask> todoTasks3;

    public static List<TodoTask> todoTasks4;
    /**
     * 微信包名
     */
    public static final String WECHAT_PACKAGENAME = "com.tencent.mm";

    /**
     * shared_prefs 文件名
     */
    public static final String SP_NAME_WECHAT = "weChatSp";
    // shared_prefs keys
    public static final String SP_WECHAT_KEY_LASTMSGTIME = "lastMsgTime";
    public static final String SP_WECHAT_KEY_ENMICRODBPWD = "enMicroDbPwd";
    public static final String SP_WECHAT_KEY_ENMICRODBPATH = "enMicroDbPath";
    public static final String SP_WECHAT_KEY_SERVICE_ISENABLED = "isServiceEnabled";
    public static final String SP_WECHAT_KEY_SERVICE_LASTRUNTIME = "serviceLastRunEndTime";
    public static final String SP_WECHAT_KEY_SERVICE_INTERVALMINUTE = "serviceIntervalMinute";
    public static final String SP_WECHAT_KEY_IS_DELETE_MSG_AFTER_UPLOAD = "isDeleteMsgAfterUpload";
    public static final String SP_MAX_FRIENDS_COUNT = "maxFriendsCount";
    public static final String SP_CURRENT_FRIENDS_COUNT = "currentFriendsCount";
    public static final String SP_CURRENT_GROUPS_COUNT = "currentGroupsCount";
    public static final String SP_IS_HAS_MESSAGE_UPLOAD = "isHasMessageUpload";

    /**
     * 默认服务定时运行时间（分钟）
     */
    public static final int DEFALT_SERVICE_INTERVAL_MINUTES = 60;

    /**
     * 查询单人消息最大数
     */
    public static final int CHAT_MESSAGE_LIMIT = 100;

    //beta l-wxapp1.vc.beta.cn0.qunar.com
    public static final String HTTP_HOST = "http://wx.corp.qunar.com/laravel/public/api/";

//    public static final String HTTP_HOST = "http://l-wxapp1.vc.beta.cn0.qunar.com/laravel/public/api/";

    public static final String GET_USER_REMARK = HTTP_HOST + "%s?body=%s&wxname=%s";

    public static final String UPLOAD_MESSAGE = HTTP_HOST + "wechat_wdy/uploadMessage.do";

    public static final String UPLOAD_GROUP_MEMBER = HTTP_HOST + "wechat_wdy/uploadGroupMemberList.do";

    public static final String UPLOAD_FRIEND = HTTP_HOST + "wechat_wdy/uploadFriendsList.do";

    public static final String GET_TODO_TASK = HTTP_HOST + "task/getTodoTasks.json?wxname=%s";

    public static final String SET_TASK_DONE = HTTP_HOST + "task/setTasksDone.json";

    public static class Preferences{
        public static final String WX_ID = "wx_id";
    }

    /**
     * 自动化任务类型
     */
    public enum AutoType{
        AUTO_ACCEPT_FRIEND_REQUEST,//自动接收好友请求
        AUTO_ADD_CONTACT_FRIEND,//自动添加通讯录好友
        AUTO_PUBLISH_WECHAT_COMMENTS,//自动发朋友圈
        AUTO_EXCUTE_TODO_TASK//自动执行远程任务
    }

}
