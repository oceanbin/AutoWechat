package com.qunar.wechat.auto.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lihaibin.li on 2017/12/15.
 */

public class RemarkUtils {
    private static Map<String, String> dateMap = new HashMap<>();

    static {
        dateMap.put("01", "A");
        dateMap.put("1", "A");
        dateMap.put("02", "B");
        dateMap.put("2", "B");
        dateMap.put("03", "C");
        dateMap.put("3", "C");
        dateMap.put("04", "D");
        dateMap.put("4", "D");
        dateMap.put("05", "E");
        dateMap.put("5", "E");
        dateMap.put("06", "F");
        dateMap.put("6", "F");
        dateMap.put("07", "G");
        dateMap.put("7", "G");
        dateMap.put("08", "H");
        dateMap.put("8", "H");
        dateMap.put("09", "I");
        dateMap.put("9", "I");
        dateMap.put("10", "J");
        dateMap.put("11", "K");
        dateMap.put("12", "L");
    }

    public static String makeRemark() {
        StringBuilder mark = new StringBuilder();
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-HH");
        String[] date = sdf.format(d).split("-");
        mark.append(dateMap.get(date[0]));

        String day = date[1];
        if (day.length() < 2) {
            day = "0" + day;
        }
        mark.append(day);
        mark.append(",");

        int hour = Integer.parseInt(date[2]);
        if (hour < 11) {
            mark.append("10");
        } else if (hour < 17) {
            mark.append("16");
        } else if (hour < 24) {
            mark.append("23");
        }
        mark.append(",,,,,,,,,");
        return mark.toString();
    }
}
