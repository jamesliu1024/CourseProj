package com.example.courseproj.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 用来创建存储用户数据的本地数据库
 * 数据库名：AndroidDB.db
 * 管理员表：admins
 * 教师表：teachers
 * 学生表：students
 * 课程信息表：courses
 * 课程安排表：schedules
 * 成绩表：scores
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AndroidDB.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
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
        String sqlScores =  new StringBuilder().
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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade...
    }
}