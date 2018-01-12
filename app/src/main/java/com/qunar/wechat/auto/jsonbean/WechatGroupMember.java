package com.qunar.wechat.auto.jsonbean;

import java.util.ArrayList;
import java.util.List;

/**
 * 需上传的群成员
 * Created by lihaibin.li on 2017/12/25.
 */

public class WechatGroupMember {
    public String wxname;
    public List<Member> groupmemberlist = new ArrayList<>();
    public static class Member{
        public String wxid;
        public String memberwxid;
        public String displaynameingroup;
        public String ower;// 这个群成员是否是群的拥有者，1 是群主，0 ，不是群主
    }
}
