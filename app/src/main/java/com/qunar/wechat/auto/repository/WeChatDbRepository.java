package com.qunar.wechat.auto.repository;

import android.database.Cursor;

/**
 * Created by lihaibin.li on 2017/11/30.
 */

public interface WeChatDbRepository {
    /**
     * 查询
     * @param sql
     * @param params
     * @return
     */
    Cursor rawQuery(String sql, String[] params);

    /**
     * 删除
     * @param table
     * @param whereClause
     * @param whereArgs
     * @return
     */
    int delete(String table, String whereClause, String[] whereArgs);
}
