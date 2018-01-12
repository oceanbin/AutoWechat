package com.qunar.wechat.auto.repository;

import android.database.Cursor;

import com.qunar.wechat.auto.xposed.QunarWechatContainer;

/**
 * Created by lihaibin.li on 2017/11/30.
 */

public class XposedWeChatDbRepositoryImpl implements WeChatDbRepository{
    @Override
    public Cursor rawQuery(String sql, String[] params) {
        return QunarWechatContainer.rawQuery(sql, params);
    }

    @Override
    public int delete(String table, String whereClause, String[] whereArgs) {
        return QunarWechatContainer.delete(table, whereClause, whereArgs);
    }
}
