package com.example.courseproj.Common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                append("admin_id       int primary key auto_increment comment '管理员id',").
                append("admin_name     varchar(50) comment '管理员姓名',").
                append("admin_password varchar(50) comment '管理员密码',").
                append("gender         int comment '性别 0: 男 1: 女',").
                append("birthday       date comment '生日',").
                append("start_time     date comment '入职时间'").
                append(") engine = InnoDB").
                append("auto_increment = 1000").
                append("default charset = utf8;").
                toString();
        db.execSQL(sqlAdmins);

        //教师表：teachers
        String sqlTeachers = new StringBuilder().
                append("create table if not exists teachers").
                append("(").
                append("teacher_id       int primary key auto_increment comment '教师id',").
                append("teacher_name     varchar(50) comment '教师姓名',").
                append("teacher_password varchar(50) comment '教师密码',").
                append("gender           int comment '性别 0: 男 1: 女',").
                append("birthday         date comment '生日',").
                append("start_time       date comment '入职时间'").
                append(") engine = InnoDB").
                append("auto_increment = 1000").
                append("default charset = utf8;").
                toString();
        db.execSQL(sqlTeachers);

        //学生表：students
        String sqlStudents = new StringBuilder().
                append("create table if not exists students").
                append("(").
                append("student_id       int primary key auto_increment comment '学生id',").
                append("student_name     varchar(50) comment '学生姓名',").
                append("student_password varchar(50) comment '学生密码',").
                append("gender           int comment '性别 0: 男 1: 女',").
                append("birthday         date comment '生日',").
                append("start_year       int comment '入学年份',").
                append("years            int comment '学年制'").
                append(") engine = InnoDB").
                append("auto_increment = 1000").
                append("default charset = utf8;").
                toString();
        db.execSQL(sqlStudents);

//        课程信息表：courses
        String sqlCourses = new StringBuilder().
                append("create table if not exists courses").
                append("(").
                append("course_id     int primary key auto_increment comment '课程id',").
                append("course_name   varchar(50) comment '课程名称',").
                append("course_credit int comment '学分',").
                append("course_hour   int comment '学时',").
                append("course_week   int comment '周数',").
                append("course_type   int comment '课程类型 0:必修 1:选修'").
                append(") engine = InnoDB").
                append("auto_increment = 1000").
                append("default charset = utf8;").
                toString();
        db.execSQL(sqlCourses);

//        课程安排表：schedules
        String sqlSchedules = new StringBuilder().
                append("create table if not exists schedules").
                append("(").
                append("schedule_id  int primary key auto_increment comment '课程表id',").
                append("teacher_id   int comment '教师id',").
                append("course_id    int comment '课程id',").
                append("course_day   int comment '上课时间 1-7分别代表周一到周日',").
                append("course_time  int comment '上课时间 1-8分别代表第一节到第八节课',").
                append("course_place varchar(50) comment '上课地点',").
                append("years        int comment '学年',").
                append("terms        int comment '学期 0:上学期 1:下学期',").
                append("foreign key (teacher_id) references teachers (teacher_id),").
                append("foreign key (course_id) references courses (course_id)").
                append(") engine = InnoDB").
                append("auto_increment = 1000").
                append("default charset = utf8;").
                toString();
        db.execSQL(sqlSchedules);

//        成绩表：scores
        String sqlScores = new StringBuilder().
                append("create table if not exists scores").
                append("(").
                append("score_id    int primary key auto_increment comment '成绩id',").
                append("student_id  int comment '学生id',").
                append("course_id   int comment '课程id',").
                append("schedule_id int comment '课程表id',").
                append("score       int comment '成绩',").
                append("foreign key (student_id) references students (student_id),").
                append("foreign key (course_id) references courses (course_id),").
                append("foreign key (schedule_id) references schedules (schedule_id)").
                append(") engine = InnoDB").
                append("auto_increment = 1000").
                append("default charset = utf8;").
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
                        // 将数据存储到本地数据库
                        String sqlInsert = "insert into students (student_id, student_name, student_password, gender, birthday, start_year, years) values (?, ?, ?, ?, ?, ?, ?)";
                        db.execSQL(sqlInsert, new Object[]{student_id, student_name, student_password, gender, birthday, start_year, years});
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

                    // 获取所有成绩信息
                    sql = "select * from scores";
                    preparedStatement = connection.prepareStatement(sql);
                    resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        int score_id = resultSet.getInt("score_id");
                        int student_id = resultSet.getInt("student_id");
                        int course_id = resultSet.getInt("course_id");
                        int schedule_id = resultSet.getInt("schedule_id");
                        int score = resultSet.getInt("score");
                        // 将数据存储到本地数据库
                        String sqlInsert = "insert into scores (score_id, student_id, course_id, schedule_id, score) values (?, ?, ?, ?, ?)";
                        db.execSQL(sqlInsert, new Object[]{score_id, student_id, course_id, schedule_id, score});
                    }
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
                        // 将数据存储到本地数据库
                        String sqlInsert = "insert into teachers (teacher_id, teacher_name, teacher_password, gender, birthday, start_time) values (?, ?, ?, ?, ?, ?)";
                        db.execSQL(sqlInsert, new Object[]{teacher_id, teacher_name, teacher_password, gender, birthday, start_time});
                    }
                    preparedStatement = null;
                    sql = null;
                    resultSet = null;

                    // 关闭数据库连接
                    DB_MySQLConnectionUtil.MySQL_DB_close(connection, preparedStatement, resultSet);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade...
    }
}