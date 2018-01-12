package com.qunar.wechat.auto.bll;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.qunar.wechat.auto.bean.WeChatUserDo;
import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.jsonbean.WechatFriend;
import com.qunar.wechat.auto.jsonbean.WechatGroup;
import com.qunar.wechat.auto.jsonbean.WechatMessage;
import com.qunar.wechat.auto.repository.WeChatDbRepository;
import com.qunar.wechat.auto.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihaibin.li on 2017/11/30.
 */

public class MessageSubmitBll {
    private final String TAG = MessageSubmitBll.class.getSimpleName();
    private final SharedPrefsHelper sharedPrefsHelper;
    private final WeChatDbRepository weChatDbRepository;

    public MessageSubmitBll(WeChatDbRepository weChatDbRepository, Context context) {
        this.weChatDbRepository = weChatDbRepository;
        this.sharedPrefsHelper = new SharedPrefsHelper(context);
    }

    /**
     * 获取微信用户信息
     *
     * @param weChatUid
     * @return
     */
    public WeChatUserDo queryWeChatUserInfo(String weChatUid) {
        String sql = "select username as weChatUid\n" +
                "      ,alias as weChatNo\n" +
                "\t  ,nickname as weChatNickname\n" +
                "    ,conRemark as weChatRemarkName\n" +
                "from rcontact\n" +
                "where username = '" + weChatUid + "'";
        Cursor cursor = null;
        WeChatUserDo result = null;
        try {

            cursor = weChatDbRepository.rawQuery(sql, null);
            result = new WeChatUserDo();
            while (cursor.moveToNext()) {
                result.WechatUid = cursor.getString(cursor.getColumnIndex("weChatUid"));
                result.WechatNickname = cursor.getString(cursor.getColumnIndex("weChatNickname"));
                result.WechatNo = cursor.getString(cursor.getColumnIndex("weChatNo"));
                result.WeChatRemarkName = cursor.getString(cursor.getColumnIndex("weChatRemarkName"));
            }
        } catch (Exception e) {
            Log.d(TAG, "获取微信用户信息", e);
            throw e;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return result;
    }

    /**
     * 获取当前登录微信用户信息
     *
     * @return
     */

    public WeChatUserDo queryCurrentWeChatUserInfo() {
        String sql = "select u1.value as weChatUid\n" +
                "      ,u2.value as weChatNickname\n" +
                "      ,u3.value as weChatNo\n" +
                "from userinfo u1\n" +
                "left join userinfo u2 on u2.id = 4\n" +
                "left join userinfo u3 on u3.id = 42\n" +
                "where u1.id = 2\n";
        Cursor cursor = null;
        WeChatUserDo result = null;
        try {

            cursor = weChatDbRepository.rawQuery(sql, null);
            result = new WeChatUserDo();
            while (cursor.moveToNext()) {
                result.WechatUid = cursor.getString(cursor.getColumnIndex("weChatUid"));
                result.WechatNickname = cursor.getString(cursor.getColumnIndex("weChatNickname"));
                result.WechatNo = cursor.getString(cursor.getColumnIndex("weChatNo"));
            }
        } catch (Exception e) {
            Log.d(TAG, "获取当前微信用户", e);
            throw e;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return result;
    }

    /**
     * 根据上次消息最大时间查询单人消息
     * @param lastMessageTime
     * @return
     */
    public List<WechatMessage> queryChatMessageByLastMessageTime(long lastMessageTime){
        String sql = "select m.msgSvrId as messageId\n" +
                "      ,r.conRemark as remark\n" +
                "\t  ,m.content\n" +
                "\t  ,m.type\n" +
                "\t  ,m.isSend\n" +
                "\t  ,m.createTime\n" +
                "from message m\n" +
                "join rcontact r on r.username = m.talker\n" +
                "where m.talker not like '%@chatroom'\n" +
                "and m.msgSvrId > 0\n" +
                "and m.isSend = 0\n" +
                "and m.createTime > " + lastMessageTime + "\n" +
                "order by m.createTime asc " +
                "limit " + Constants.CHAT_MESSAGE_LIMIT;

        List<WechatMessage> result = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = weChatDbRepository.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                WechatMessage wechatMessage = new WechatMessage();
                result.add(wechatMessage);
            }
        } catch (Exception e) {
            Log.d(TAG, "获取群消息信息", e);
            throw e;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return result;
    }

    /**
     * 查询消息
     * @param lastMessageTime
     * @return
     */
    public WechatMessage queryMessageByLastMessageTime(long lastMessageTime){
        String sql = "select m.msgSvrId as messageId\n" +
                "      ,r.conRemark as remark\n" +
                "      ,u.value as wxno\n" +
                "\t  ,m.content\n" +
                "\t  ,m.type\n" +
                "\t  ,m.talker\n" +
                "\t  ,m.isSend\n" +
                "\t  ,m.createTime\n" +
                "from message m\n" +
                "join rcontact r on r.username = m.talker\n" +
                "join userinfo u on u.id = 42\n" +
                "and m.msgSvrId > 0\n" +
                "and m.createTime > " + lastMessageTime + "\n" +
                "order by m.createTime asc " +
                "limit " + Constants.CHAT_MESSAGE_LIMIT;
        Cursor cursor = null;
        WechatMessage wechatMessage = new WechatMessage();
        List<WechatMessage.ML> messages = new ArrayList<>();
        try {
            cursor = weChatDbRepository.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                WechatMessage.ML ml = new WechatMessage.ML();
                ml.MsgType = cursor.getInt(cursor.getColumnIndex("type"));
                ml.Content = cursor.getString(cursor.getColumnIndex("content"));
                ml.FromUserName = cursor.getString(cursor.getColumnIndex("talker"));
                ml.ToUserName = cursor.getString(cursor.getColumnIndex("wxno"));
                ml.issend = cursor.getInt(cursor.getColumnIndex("isSend"));
                ml.MsgId = cursor.getString(cursor.getColumnIndex("messageId"));
                wechatMessage.lastMessageTime = cursor.getLong(cursor.getColumnIndex("createTime"));
                wechatMessage.wxname = cursor.getString(cursor.getColumnIndex("wxno"));
                messages.add(ml);
            }
            wechatMessage.ml = messages;
        } catch (Exception e) {
            Log.d(TAG, "获取消息信息", e);
            throw e;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return wechatMessage;
    }

    /**
     * 查询群信息
     * @return
     */
    public List<WechatGroup> queryGroups(){
        List<WechatGroup> wechatGroups = new ArrayList<>();
        String sql = "select c.chatroomname\n"+
                "\t  ,c.memberlist\n"+
                "\t  ,c.displayname\n"+
                "\t  ,c.roomowner\n"+
                "\t  ,c.selfDisplayName\n"+
                "from chatroom c";
        Cursor cursor = null;
        try {
            cursor = weChatDbRepository.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                WechatGroup group = new WechatGroup();
                group.roomname = cursor.getString(cursor.getColumnIndex("chatroomname"));
                String memberlist = cursor.getString(cursor.getColumnIndex("memberlist"));
                String[] members = memberlist.split(";");
                int count = members == null?0:members.length;
                for(int i = 0;i<count;i++){
                    WechatGroup.Person person = new WechatGroup.Person();
                    person.nickName = queryNickByWxId(members[i]);
                    person.username = members[i];
                    group.persons.add(person);
                }
//                group.memberlist = cursor.getString(cursor.getColumnIndex("memberlist"));
//                group.displayname = cursor.getString(cursor.getColumnIndex("displayname"));
                group.roomowner = cursor.getString(cursor.getColumnIndex("roomowner"));
                group.selfDisplayName = cursor.getString(cursor.getColumnIndex("selfDisplayName"));
                wechatGroups.add(group);
            }
        } catch (Exception e) {
            Log.d(TAG, "获取群成员信息", e);
            throw e;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return wechatGroups;
    }

    /**
     * 根据wxid查找昵称
     * @param wxid
     * @return
     */
    public String queryNickByWxId(String wxid){
        String sql = "select rc.nickname\n"+
                "\t  from rcontact rc\n"+
                "\t  where username=?";
        Cursor cursor = null;
        try {
            cursor = weChatDbRepository.rawQuery(sql, new String[]{wxid});
            while (cursor.moveToNext()) {
                String nick = cursor.getString(cursor.getColumnIndex("nickname"));
                return TextUtils.isEmpty(nick)?wxid:nick;
            }
        } catch (Exception e) {
            Log.d(TAG, "获取群成员信息", e);
            throw e;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return wxid;
    }

    /**
     * 查询好友
     * @return
     */
    public List<WechatFriend.Friend> queryFriend(){
        List<WechatFriend.Friend> friends = new ArrayList<>();
        String sql = "select rc.username as wxid\n"+
                "\t  ,rc.alias\n"+
                "\t  ,rc.conRemark\n"+
                "\t  ,rc.nickname\n"+
                "from rcontact rc";
        Cursor cursor = null;
        try {
            cursor = weChatDbRepository.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                WechatFriend.Friend friend = new WechatFriend.Friend();
                friend.alias = cursor.getString(cursor.getColumnIndex("alias"));
                friend.nickname = cursor.getString(cursor.getColumnIndex("nickname"));
                friend.remark = cursor.getString(cursor.getColumnIndex("conRemark"));
                friend.wxid = cursor.getString(cursor.getColumnIndex("wxid"));
                friends.add(friend);
            }
        } catch (Exception e) {
            Log.d(TAG, "获取好友信息", e);
            throw e;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return friends;
    }

    /**
     * 查询好友数
     * @return
     */
    public int queryFriendCount(){
        String sql = "SELECT * FROM rcontact where type != 33";
        Cursor cursor = null;
        try {
            cursor = weChatDbRepository.rawQuery(sql, null);
            return cursor.getCount();
        } catch (Exception e) {
            Log.d(TAG, "获取好友信息", e);
            throw e;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    /**
     * 查询好友&群信息
     * @return
     */
    public WechatFriend queryFriendsAndRoom(){
        WechatFriend wechatFriend = new WechatFriend();
        wechatFriend.wxname =queryCurrentWeChatUserInfo().WechatNo;
        List<WechatGroup> groups = queryGroups();
        List<WechatFriend.Friend> friends = queryFriend();
        for(WechatGroup group : groups){
            WechatFriend.Friend friend = new WechatFriend.Friend();
            friend.wxid = group.roomname;
            friend.alias = "";
            friend.remark = "";
            friend.nickname = group.selfDisplayName;
            friends.add(friend);
        }
        wechatFriend.friendlist = friends;
        return wechatFriend;
    }
}
