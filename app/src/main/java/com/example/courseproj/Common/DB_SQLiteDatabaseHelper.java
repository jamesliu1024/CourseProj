package com.example.courseproj.Common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 用来创建存储用户数据的本地数据库，以及将MySQL数据库数据存储到本地数据库
 * 数据库名：AndroidDB.db
 * 管理员表：admins
 * 教师表：teachers
 * 学生表：students
 * 课程信息表：courses
 * 课程安排表：schedules
 * 成绩表：scores
 */
public class DB_SQLiteDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AndroidDB.db";
    private static final int DATABASE_VERSION = 1;

    public DB_SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //管理员表：admins
        String sqlAdmins = new StringBuilder().
                append("create table if not exists admins").
                append("(").
                append("admin_id       INTEGER PRIMARY KEY,").
                append("admin_name     TEXT,").
                append("admin_password TEXT,").
                append("gender         INTEGER,").
                append("birthday       TEXT,").
                append("start_time     TEXT").
                append(")").
                toString();
        db.execSQL(sqlAdmins);

        //教师表：teachers
        String sqlTeachers = new StringBuilder().
                append("create table if not exists teachers").
                append("(").
                append("teacher_id       INTEGER PRIMARY KEY,").
                append("teacher_name     TEXT,").
                append("teacher_password TEXT,").
                append("gender           INTEGER,").
                append("birthday         TEXT,").
                append("start_time       TEXT").
                append(")").
                toString();
        db.execSQL(sqlTeachers);

        //学生表：students
        String sqlStudents = new StringBuilder().
                append("create table if not exists students").
                append("(").
                append("student_id       INTEGER PRIMARY KEY,").
                append("student_name     TEXT,").
                append("student_password TEXT,").
                append("gender           INTEGER,").
                append("birthday         TEXT,").
                append("start_year       INTEGER,").
                append("years            INTEGER,").
                append("teacher_id        INTEGER").
                append(")").
                toString();
        db.execSQL(sqlStudents);

//        课程信息表：courses
        String sqlCourses = new StringBuilder().
                append("create table if not exists courses").
                append("(").
                append("course_id     INTEGER PRIMARY KEY,").
                append("course_name   TEXT,").
                append("course_credit INTEGER,").
                append("course_hour   INTEGER,").
                append("course_week   INTEGER,").
                append("course_type   INTEGER").
                append(")").
                toString();
        db.execSQL(sqlCourses);

//        课程安排表：schedules
        String sqlSchedules = new StringBuilder().
                append("create table if not exists schedules").
                append("(").
                append("schedule_id  INTEGER PRIMARY KEY,").
                append("teacher_id   INTEGER,").
                append("course_id    INTEGER,").
                append("course_day   INTEGER,").
                append("course_time  INTEGER,").
                append("course_place TEXT,").
                append("years        INTEGER,").
                append("terms        INTEGER,").
                append("foreign key (teacher_id) references teachers (teacher_id),").
                append("foreign key (course_id) references courses (course_id)").
                append(")").
                toString();
        db.execSQL(sqlSchedules);

//        成绩表：scores
        String sqlScores = new StringBuilder().
                append("create table if not exists scores").
                append("(").
                append("score_id    INTEGER PRIMARY KEY,").
                append("student_id  INTEGER,").
                append("schedule_id INTEGER,").
                append("score       INTEGER,").
                append("foreign key (student_id) references students (student_id),").
                append("foreign key (schedule_id) references schedules (schedule_id)").
                append(")").
                toString();
        db.execSQL(sqlScores);


        // 获取MySQL数据库所有数据并存储
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                String sql = null;
                try {
                    connection = DB_MySQLConnectionUtil.getConnection();
                    if (connection == null) {
                        throw new SQLException("无法连接到数据库");
                    }

                    // 获取所有成绩信息
                    sql = "select * from scores";
                    preparedStatement = connection.prepareStatement(sql);
                    resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        int score_id = resultSet.getInt("score_id");
                        int student_id = resultSet.getInt("student_id");
                        int schedule_id = resultSet.getInt("schedule_id");
                        int score = resultSet.getInt("score");
                        // TEST
//                        Log.i("DB_SQLiteDatabaseHelper", "score_id: " + score_id + ", student_id: " + student_id + ", schedule_id: " + schedule_id + ", score: " + score);
                        // 将数据存储到本地数据库
                        String sqlInsert = "insert into scores (score_id, student_id, schedule_id, score) values (?, ?, ?, ?)";
                        db.execSQL(sqlInsert, new Object[]{score_id, student_id, schedule_id, score});
                    }
                    // TEST
//                    String sqlInsert2 = "insert into scores (score_id, student_id, schedule_id, score) values (?, ?, ?, ?)";
//                    db.execSQL(sqlInsert2, new Object[]{1000, 1000, 1000, 1000, 90});
//                    db.execSQL(sqlInsert2, new Object[]{1, 1, 1, 1, 1});
                    preparedStatement = null;
                    sql = null;
                    resultSet = null;


                    // 获取所有管理员信息
                    sql = "select * from admins";
                    preparedStatement = connection.prepareStatement(sql);
                    resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        int admin_id = resultSet.getInt("admin_id");
                        String admin_name = resultSet.getString("admin_name");
                        String admin_password = resultSet.getString("admin_password");
                        int gender = resultSet.getInt("gender");
                        String birthday = resultSet.getString("birthday");
                        String start_time = resultSet.getString("start_time");
                        // 将数据存储到本地数据库
                        String sqlInsert = "insert into admins (admin_id, admin_name, admin_password, gender, birthday, start_time) values (?, ?, ?, ?, ?, ?)";
                        db.execSQL(sqlInsert, new Object[]{admin_id, admin_name, admin_password, gender, birthday, start_time});
                    }
                    preparedStatement = null;
                    sql = null;
                    resultSet = null;

                    // 获取所有教师信息
                    sql = "select * from teachers";
                    preparedStatement = connection.prepareStatement(sql);
                    resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        int teacher_id = resultSet.getInt("teacher_id");
                        String teacher_name = resultSet.getString("teacher_name");
                        String teacher_password = resultSet.getString("teacher_password");
                        int gender = resultSet.getInt("gender");
                        String birthday = resultSet.getString("birthday");
                        String start_time = resultSet.getString("start_time");
                        // TEST
//                        Log.i("DB_SQLiteDatabaseHelper", "teacher_id: " + teacher_id + ", teacher_name: " + teacher_name + ", teacher_password: " + teacher_password + ", gender: " + gender + ", birthday: " + birthday + ", start_time: " + start_time);

                        // 将数据存储到本地数据库
                        String sqlInsert = "insert into teachers (teacher_id, teacher_name, teacher_password, gender, birthday, start_time) values (?, ?, ?, ?, ?, ?)";
                        db.execSQL(sqlInsert, new Object[]{teacher_id, teacher_name, teacher_password, gender, birthday, start_time});
                    }
                    preparedStatement = null;
                    sql = null;
                    resultSet = null;

                    // 获取所有学生信息
                    sql = "select * from students";
                    preparedStatement = connection.prepareStatement(sql);
                    resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        int student_id = resultSet.getInt("student_id");
                        String student_name = resultSet.getString("student_name");
                        String student_password = resultSet.getString("student_password");
                        int gender = resultSet.getInt("gender");
                        String birthday = resultSet.getString("birthday");
                        int start_year = resultSet.getInt("start_year");
                        int years = resultSet.getInt("years");
                        int teacher_id = resultSet.getInt("teacher_id");
                        // 将数据存储到本地数据库
                        String sqlInsert = "insert into students (student_id, student_name, student_password, gender, birthday, start_year, years, teacher_id) values (?, ?, ?, ?, ?, ?, ?, ?)";
                        db.execSQL(sqlInsert, new Object[]{student_id, student_name, student_password, gender, birthday, start_year, years, teacher_id});
                    }
                    preparedStatement = null;
                    sql = null;
                    resultSet = null;

                    // 获取所有课程信息
                    sql = "select * from courses";
                    preparedStatement = connection.prepareStatement(sql);
                    resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        int course_id = resultSet.getInt("course_id");
                        String course_name = resultSet.getString("course_name");
                        int course_credit = resultSet.getInt("course_credit");
                        int course_hour = resultSet.getInt("course_hour");
                        int course_week = resultSet.getInt("course_week");
                        int course_type = resultSet.getInt("course_type");
                        // 将数据存储到本地数据库
                        String sqlInsert = "insert into courses (course_id, course_name, course_credit, course_hour, course_week, course_type) values (?, ?, ?, ?, ?, ?)";
                        db.execSQL(sqlInsert, new Object[]{course_id, course_name, course_credit, course_hour, course_week, course_type});
                    }
                    preparedStatement = null;
                    sql = null;
                    resultSet = null;

                    // 获取所有课程安排信息
                    sql = "select * from schedules";
                    preparedStatement = connection.prepareStatement(sql);
                    resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        int schedule_id = resultSet.getInt("schedule_id");
                        int teacher_id = resultSet.getInt("teacher_id");
                        int course_id = resultSet.getInt("course_id");
                        int course_day = resultSet.getInt("course_day");
                        int course_time = resultSet.getInt("course_time");
                        String course_place = resultSet.getString("course_place");
                        int years = resultSet.getInt("years");
                        int terms = resultSet.getInt("terms");
                        // 将数据存储到本地数据库
                        String sqlInsert = "insert into schedules (schedule_id, teacher_id, course_id, course_day, course_time, course_place, years, terms) values (?, ?, ?, ?, ?, ?, ?, ?)";
                        db.execSQL(sqlInsert, new Object[]{schedule_id, teacher_id, course_id, course_day, course_time, course_place, years, terms});
                    }
                    preparedStatement = null;
                    sql = null;
                    resultSet = null;

                    // 关闭数据库连接
                    DB_MySQLConnectionUtil.MySQL_DB_close(connection, preparedStatement, resultSet);

                } catch (Exception e) {
                    Log.e("DB_SQLiteDatabaseHelper", "Error while fetching and inserting data", e);
                    throw new RuntimeException(e);
                }
            }
        }).start();

        // 创建学生已选课程信息视图
        db.execSQL("CREATE VIEW IF NOT EXISTS v_scores_schedules_courses " +
                "AS SELECT score_id AS _id, * FROM scores " +
                "JOIN schedules ON scores.schedule_id = schedules.schedule_id " +
                "JOIN courses ON schedules.course_id = courses.course_id " +
                "JOIN teachers ON schedules.teacher_id = teachers.teacher_id " +
                "JOIN students s on scores.student_id = s.student_id");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如果有更新，清空数据库
        db.execSQL("DROP TABLE IF EXISTS admins");
        db.execSQL("DROP TABLE IF EXISTS teachers");
        db.execSQL("DROP TABLE IF EXISTS students");
        db.execSQL("DROP TABLE IF EXISTS courses");
        db.execSQL("DROP TABLE IF EXISTS schedules");
        db.execSQL("DROP TABLE IF EXISTS scores");
        onCreate(db);
    }
}