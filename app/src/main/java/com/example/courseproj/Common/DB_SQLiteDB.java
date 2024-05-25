package com.example.courseproj.Common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 该类主要用于获取数据库对象
 * 以及关闭数据库
 */
public class DB_SQLiteDB extends DB_SQLiteDatabaseHelper{
    private DB_SQLiteDatabaseHelper dbHelper;
    private SQLiteDatabase sqlite;

    public DB_SQLiteDB(Context context) {
        super(context);
        this.getWritableDatabase();  // 添加这行代码
    }

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
