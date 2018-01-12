package com.qunar.wechat.auto.api;

import android.text.TextUtils;
import android.util.Log;

import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.common.TaskExecutor;
import com.qunar.wechat.auto.jsonbean.GeneralJson;
import com.qunar.wechat.auto.jsonbean.Remark;
import com.qunar.wechat.auto.jsonbean.WechatFriend;
import com.qunar.wechat.auto.jsonbean.WechatGroup;
import com.qunar.wechat.auto.jsonbean.WechatGroupMember;
import com.qunar.wechat.auto.jsonbean.WechatMessage;
import com.qunar.wechat.auto.utils.HttpUtils;
import com.qunar.wechat.auto.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import de.robv.android.xposed.XposedBridge;
import okhttp3.Callback;

/**
 * Created by lihaibin.li on 2017/11/21.
 */

public class WechatApi {
    private static final String TAG = WechatApi.class.getSimpleName();

    /**
     * 通过手机号 获取到备注信息
     *
     * @param body
     * @param wxName
     * @return
     */
    public static Remark getUserRemark(String body, String wxName) {
        String GET_REMARK_URL = String.format(Constants.GET_USER_REMARK, "getuserremark", body, wxName);
        Remark remark = new Remark();
        try {
            String result = HttpUtils.get(GET_REMARK_URL);
            Log.d(TAG, result);
            GeneralJson generalJson = JsonUtils.getGson().fromJson(result, GeneralJson.class);
            if (generalJson != null && generalJson.data != null) {
                remark = new Remark();
                remark.setRemark(generalJson.data.get("remark"));
                remark.setMessage(generalJson.data.get("message"));
            }
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
        return remark;
    }

    /**
     * 上传消息
     *
     * @param message
     * @return {"ret":true,"errcode":0}
     */
    public static void uploadMessage(final WechatMessage message, Callback back) {
        HttpUtils.post(Constants.UPLOAD_MESSAGE, JsonUtils.getGson().toJson(message), back);
    }

    /**
     * 上传群成员
     *
     * @param groups
     * @return
     */
    public static synchronized void uploadGroupMemberList(List<WechatGroup> groups, String wxname) {
        if (groups == null || groups.isEmpty() || TextUtils.isEmpty(wxname)) return;
        XposedBridge.log("uploadGroupMemberList->>wxname" + wxname);
        final WechatGroupMember member = new WechatGroupMember();
        member.wxname = wxname;
        for (WechatGroup group : groups) {
            List<WechatGroup.Person> persons = group.persons;
            int count = persons == null ? 0 : persons.size();
            List<WechatGroupMember.Member> groupMembers = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                WechatGroupMember.Member m = new WechatGroupMember.Member();
                WechatGroup.Person person = persons.get(i);
                m.wxid = group.roomname;
                String wxid = person.username;
                m.memberwxid = wxid;
                m.displaynameingroup = group.roomowner.equals(wxid)?group.selfDisplayName:person.nickName;
                m.ower = group.roomowner.equals(wxid) ? "1" : "0";
                groupMembers.add(m);
            }
            member.groupmemberlist = groupMembers;
            final String requestBody = JsonUtils.getGson().toJson(member);
            XposedBridge.log("uploadGroupMemberList->>requestBody" + requestBody);
            TaskExecutor.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    String result = HttpUtils.post(Constants.UPLOAD_GROUP_MEMBER, requestBody);
                    XposedBridge.log("uploadGroupMemberList->>result" + result);
                    return result;
                }
            });

        }
    }


    /**
     * 上传联系人 每次最多上传100条数据
     *
     * @param friend
     * @return
     */
    public static synchronized void uploadFriendsList(final WechatFriend friend) {
        if (friend.friendlist.size() > 100) {
            WechatFriend wf = new WechatFriend();
            wf.wxname = friend.wxname;
            wf.friendlist = friend.friendlist.subList(0, 100);
            commitUploadFriends(wf);
            friend.friendlist = friend.friendlist.subList(100,friend.friendlist.size()-1);
            uploadFriendsList(friend);
        } else {
            commitUploadFriends(friend);
        }
    }

    private static synchronized void commitUploadFriends(final WechatFriend friend) {
        TaskExecutor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String requestBody = JsonUtils.getGson().toJson(friend);
                XposedBridge.log("uploadFriendsList->>" + requestBody);
                String result = HttpUtils.post(Constants.UPLOAD_FRIEND, requestBody);
                XposedBridge.log("uploadFriendsList->>" + result);
                return result;
            }
        });
    }
}
