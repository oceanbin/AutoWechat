package com.qunar.wechat.auto.utils;

import java.util.Random;

/**
 * Created by lihaibin.li on 2017/11/20.
 */

public class RandomUtil {

    static Random random = new Random();

    public static int getRandomSeconds() {
        return (random.nextInt(2) + 1) * 500;
    }
}
