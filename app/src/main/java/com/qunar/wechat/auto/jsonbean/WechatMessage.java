package com.qunar.wechat.auto.jsonbean;

import java.util.ArrayList;
import java.util.List;

/**
 * 需上传的消息体
 * Created by lihaibin.li on 2017/12/25.
 */

public class WechatMessage {

    public String wxname;
    public long lastMessageTime;
    public List<ML> ml = new ArrayList<>();

    public static class ML{
        public int MsgType;
        public String Content;
        public String FromUserName;
        public String ToUserName;
        public int issend;
        public String MsgId;
        public String Timestamp;
    }
}
