/***********************************************************************
 *  Copyright (C) 1999-2017,ctrip.com. All rights reserved.
 *
 *  Author:  chen.d
 *  Date:    2017/4/21
 *  Purpose:
 *  2017/4/21  chen.d    MethodHelper
 * ***********************************************************************/
package com.qunar.wechat.auto.utils;

import android.util.Log;

public class MethodHelper {
    private MethodHelper() {
    }

    /**
     * 重试传入的retryMethod方法
     *
     * @param retryMethod         需要重试的内容
     * @param assertFunc          根据重试的方法返回的值判定是否成功，成功则跳出重试并返回本次的值，失败则继续下一次
     * @param retryTimes          重试的次数
     * @param intervalMillisecond 重试间隔时间（毫秒），小于等于0时不间隔
     * @param <R>                 需要重试的方法的返回值
     * @return
     */
    public static <R> R retry(Function<Integer, R> retryMethod, Function<R, Boolean> assertFunc, int retryTimes, long intervalMillisecond) {
        if (retryTimes <= 0) {
            return retryMethod.apply(retryTimes);
        }

        R result = retryMethod.apply(retryTimes);
        boolean assertResult = assertFunc.apply(result);
        if (assertResult) {
            return result;
        }

        if (intervalMillisecond > 0) {
            try {
                Thread.sleep(intervalMillisecond);
            } catch (Exception e) {
                Log.w("MethodHelper.retry", e);
            }
        }

        return retry(retryMethod, assertFunc, --retryTimes, intervalMillisecond);
    }

    /**
     * 重试传入的retryMethod方法
     *
     * @param retryMethod 需要重试的内容
     * @param assertFunc  根据重试的方法返回的值判定是否成功，成功则跳出重试并返回本次的值，失败则继续下一次
     * @param retryTimes  重试的次数
     * @param <R>         需要重试的方法的返回值
     * @return
     */
    public static <R> R retry(Function<Integer, R> retryMethod, Function<R, Boolean> assertFunc, int retryTimes) {
        return retry(retryMethod, assertFunc, retryTimes, 0);
    }
}
