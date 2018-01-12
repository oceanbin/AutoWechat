package com.qunar.wechat.auto.xposed;

import android.database.Cursor;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by lihaibin.li on 2017/11/30.
 */

public class QunarWechatContainer {
    private QunarWechatContainer() {
    }

    public static boolean IsInited = false;

    public static Object SqliteDatabaseObj = null;

    public static Class SqliteDatabaseClazz = null;

    public static Cursor rawQuery(String sql, String[] params) {
        return (Cursor) XposedHelpers.callMethod(QunarWechatContainer.SqliteDatabaseObj, "rawQuery", sql, params);
    }

    public static int delete(String table, String whereClause, String[] whereArgs) {
        return (int) XposedHelpers.callMethod(QunarWechatContainer.SqliteDatabaseObj, "delete", table, whereClause, whereArgs);
    }
}
