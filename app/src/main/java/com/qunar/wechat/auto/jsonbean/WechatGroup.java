package com.qunar.wechat.auto.jsonbean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihaibin.li on 2018/1/2.
 */

public class WechatGroup {
    public String roomname;
    public String roomowner;
    public String selfDisplayName;
    public List<Person> persons = new ArrayList<>();

    public static class Person{
        public String username;
        public String nickName;
    }
}
