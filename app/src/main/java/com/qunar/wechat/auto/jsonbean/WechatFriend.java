package com.qunar.wechat.auto.jsonbean;

import java.util.ArrayList;
import java.util.List;

/**
 * 需上传的联系人好友
 * Created by lihaibin.li on 2017/12/25.
 */

public class WechatFriend {
    public String wxname;
    public List<Friend> friendlist = new ArrayList<>();
    public static class Friend{
        public String wxid;
        public String alias;
        public String nickname;
        public String remark;
    }
}
