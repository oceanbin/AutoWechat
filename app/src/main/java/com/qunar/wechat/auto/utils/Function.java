package com.qunar.wechat.auto.utils;

public interface Function<T, R> {
    R apply(T t);
}
