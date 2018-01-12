package com.qunar.wechat.auto.repository;

import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * Created by lihaibin.li on 2017/11/30.
 */

public class WeChatDbRepositoryImpl implements WeChatDbRepository{
    private final SQLiteDatabase enMicroMsgDb;

    public WeChatDbRepositoryImpl(SQLiteDatabase enMicroMsgDb) {
        this.enMicroMsgDb = enMicroMsgDb;
    }

    @Override
    public Cursor rawQuery(String sql, String[] params) {
        return enMicroMsgDb.rawQuery(sql, params);
    }

    @Override
    public int delete(String table, String whereClause, String[] whereArgs) {
        return enMicroMsgDb.delete(table, whereClause, whereArgs);
    }
}
