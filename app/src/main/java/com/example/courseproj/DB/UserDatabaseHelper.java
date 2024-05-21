package com.example.courseproj.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 用来创建存储用户数据的本地数据库
 * 数据库名：user_data.db
 * 数据表：student_data, teacher_data, admin_data
 */
public class UserDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user_data.db";
    private static final int DATABASE_VERSION = 1;

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlStudent = "CREATE TABLE IF NOT EXISTS student_data (" +
                "user_id TEXT PRIMARY KEY," +
                "name TEXT," +
                "gender INTEGER," +
                "birthday TEXT," +
                "start_year INTEGER," +
                "years INTEGER," +
                "login_time TEXT" +
                ")";
        db.execSQL(sqlStudent);

        String sqlTeacher = "CREATE TABLE IF NOT EXISTS teacher_data (" +
                "user_id TEXT PRIMARY KEY," +
                "name TEXT," +
                "gender INTEGER," +
                "birthday TEXT," +
                "start_time TEXT," +
                "login_time TEXT" +
                ")";
        db.execSQL(sqlTeacher);

        String sqlAdmin = "CREATE TABLE IF NOT EXISTS admin_data (" +
                "user_id TEXT PRIMARY KEY," +
                "name TEXT," +
                "login_time TEXT" +
                ")";
        db.execSQL(sqlAdmin);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade...
    }
}