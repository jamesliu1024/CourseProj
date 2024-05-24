package com.example.courseproj.Common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DB_SQLiteDB {
    private DB_SQLiteDatabaseHelper dbHelper;
    private SQLiteDatabase sqlite;

    // 获取数据库对象
    public SQLiteDatabase getSqliteObject(Context context) {
        dbHelper = new DB_SQLiteDatabaseHelper(context);
        sqlite = dbHelper.getWritableDatabase();
        return sqlite;
    }

    // 关闭数据库
    public void close() {
        sqlite.close();
    }
}
