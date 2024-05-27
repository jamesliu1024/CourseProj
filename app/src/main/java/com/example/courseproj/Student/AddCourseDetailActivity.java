package com.example.courseproj.Student;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.courseproj.Common.DB_MySQLConnectionUtil;
import com.example.courseproj.Common.DB_SQLiteDB;
import com.example.courseproj.Common.DB_SQLiteDatabaseHelper;
import com.example.courseproj.R;
import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class AddCourseDetailActivity extends AppCompatActivity {
    LinearLayout layout;
    AnimationDrawable anim;
    private DB_SQLiteDB dbHelper;
    private SQLiteDatabase db;
    int current_year, current_month, current_term;
    String newCourseId, newScheduleId, newCourseName, newTeacherName, newCourseCredit,
            newCourseHour, newCourseWeek, newCourseType, newCourseDay, newCourseTime, newScoreId;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_add_course_detail);

        initView();

        // 获取数据库
        dbHelper = new DB_SQLiteDB(this);
        db = dbHelper.getSqliteObject(this);

        // 从sharedpreferences中获取学生学号
        String student_id = getSharedPreferences("user_data", 0).getString("user_id", "");

        // 从Intent中获取课程信息
        Intent intent = getIntent();
        newCourseId = intent.getStringExtra("course_id");  // 课程id
        newScheduleId = intent.getStringExtra("schedule_id");  // 课程表id
        current_year = intent.getIntExtra("current_year", 0);
        current_month = intent.getIntExtra("current_month", 0);
        current_term = intent.getIntExtra("current_term", 0);

        // 通过courseId从数据库查询课程信息
        String[] selectionArgs = {newCourseId, newScheduleId};
        Cursor cursor = db.rawQuery("SELECT *  " +
                "FROM courses  " +
                "JOIN schedules s on courses.course_id = s.course_id  " +
                "JOIN teachers t on s.teacher_id = t.teacher_id  " +
                "WHERE courses.course_id = ? and s.schedule_id = ?;", selectionArgs);

//        // TEST 获取selectionArgs
        Log.i("CourseDetailActivity", "selectionArgs: " + Arrays.toString(selectionArgs));
//        // TEST 获取并打印表头信息
//        String[] columnNames = cursor.getColumnNames();
//        Log.i("CourseDetailActivity", "cursor: " + cursor.getCount());
//        Log.i("CourseDetailActivity", "columnName: " + Arrays.toString(columnNames));

        cursor.moveToFirst();
        // 课程名称  任课教师  课程学分  课程学时 上课周数  课程类型
        int courseNameIndex = cursor.getColumnIndex("course_name");
        int teacherNameIndex = cursor.getColumnIndex("teacher_name");
        int courseCreditIndex = cursor.getColumnIndex("course_credit");
        int courseHourIndex = cursor.getColumnIndex("course_hour");
        int courseWeekIndex = cursor.getColumnIndex("course_week");
        int courseTypeIndex = cursor.getColumnIndex("course_type");
        int courseDayIndex = cursor.getColumnIndex("course_day");
        int courseTimeIndex = cursor.getColumnIndex("course_time");
        newCourseName = courseNameIndex != -1 ? cursor.getString(courseNameIndex) : null;
        newTeacherName = teacherNameIndex != -1 ? cursor.getString(teacherNameIndex) : null;
        newCourseCredit = courseCreditIndex != -1 ? cursor.getString(courseCreditIndex) : null;
        newCourseHour = courseHourIndex != -1 ? cursor.getString(courseHourIndex) : null;
        newCourseWeek = courseWeekIndex != -1 ? cursor.getString(courseWeekIndex) : null;
        newCourseType = courseTypeIndex != -1 ? cursor.getString(courseTypeIndex) : null;
        newCourseDay = courseDayIndex != -1 ? cursor.getString(courseDayIndex) : null;
        newCourseTime = courseTimeIndex != -1 ? cursor.getString(courseTimeIndex) : null;
        cursor.close();

        // newCourseType: 0: 必修课 1: 选修课
        newCourseType = newCourseType.equals("0") ? "必修课" : "选修课";

        // 显示课程信息
        TextView tvCourseName = findViewById(R.id.tv_course_title);
        TextView tvTeacherName = findViewById(R.id.tv_teacher);
        TextView tvCourseCredit = findViewById(R.id.tv_credit);
        TextView tvCourseHour = findViewById(R.id.tv_hour);
        TextView tvCourseWeek = findViewById(R.id.tv_week);
        TextView tvCourseType = findViewById(R.id.tv_type);

        tvCourseName.setText(newCourseName);
        tvTeacherName.setText(newTeacherName);
        tvCourseCredit.setText(newCourseCredit);
        tvCourseHour.setText(newCourseHour);
        tvCourseWeek.setText(newCourseWeek);
        tvCourseType.setText(newCourseType);

        // 在添加课程按钮的点击事件处理器中
        Button btnAddCourse = findViewById(R.id.btn_add_course);
        btnAddCourse.setOnClickListener(v -> {
            // 查询已选课程的时间
            String[] selectionArgs2 = {student_id, String.valueOf(current_year), String.valueOf(current_term), newCourseDay, newCourseTime};
            Cursor cursor1 = db.rawQuery("SELECT * FROM v_scores_schedules_courses  WHERE student_id = ? and years = ? " +
                    "and terms = ? and course_day = ? and course_time = ?", selectionArgs2);
            Log.i("AddCourseDetailActivity", "selectionArgs2: " + Arrays.toString(selectionArgs2));  //TEST
            while (cursor1.moveToNext()) {
                int courseNameIndex2 = cursor1.getColumnIndex("course_name");
                int courseDayIndex2 = cursor1.getColumnIndex("course_day");
                int courseTimeIndex2 = cursor1.getColumnIndex("course_time");
                String courseName = courseNameIndex2 != -1 ? cursor1.getString(courseNameIndex2) : null;
                String courseDay = courseDayIndex2 != -1 ? cursor1.getString(courseDayIndex2) : null;
                String courseTime = courseTimeIndex2 != -1 ? cursor1.getString(courseTimeIndex2) : null;
                // 检查新课程的时间是否与已选课程的时间冲突
                if (newCourseDay.equals(courseDay) && newCourseTime.equals(courseTime)) {
                    runOnUiThread(() -> {
                        Toast.makeText(AddCourseDetailActivity.this, "该时间段与已选课程：\n " + courseName + " \t有冲突", Toast.LENGTH_LONG).show();
                    });
                    return;
                }
            }
            cursor1.close();

            // 显示确认对话框
            new AlertDialog.Builder(AddCourseDetailActivity.this)
                    .setTitle("确认添加课程")
                    .setMessage("你确定要添加" + this.newCourseName + "吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            Log.i("AddCourseDetailActivity", "newCourseId: " + newCourseId + " ,newCourseName: " + newCourseName + " newScheduleId: " + newScheduleId);  //TEST
                            // 向服务器发送请求，将新课程添加到score表中
                            new Thread(() -> {
                                // 连接服务器MySQL
                                Connection connection = null;
                                PreparedStatement preparedStatement = null;
                                ResultSet resultSet = null;
                                try {
                                    connection = DB_MySQLConnectionUtil.getConnection();
                                    if (connection == null) {
                                        throw new SQLException("无法连接到数据库");
                                    }
                                    String sql = "INSERT INTO scores (student_id, schedule_id, score) VALUES (?, ?, 0)";
                                    preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                                    preparedStatement.setString(1, student_id);
                                    preparedStatement.setString(2, newScheduleId);
                                    preparedStatement.executeUpdate();
// 获取自增id
                                    resultSet = preparedStatement.getGeneratedKeys();
                                    if (resultSet == null) {
                                        throw new SQLException("添加课程插入失败，无法获取自增id");
                                    }
                                    resultSet.next();
                                    newScoreId = resultSet.getString(1);
                                    Log.i("AddCourseDetailActivity", "newScoreId: " + newScoreId);  //TEST

                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    try {
                                        if (resultSet != null) {
                                            resultSet.close();
                                        }
                                        if (preparedStatement != null) {
                                            preparedStatement.close();
                                        }
                                        if (connection != null) {
                                            connection.close();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                            // 等待1秒，如果newScoreId为空，表示添加课程失败
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (newScoreId == null) {
                                // 显示添加失败的提示信息
                                runOnUiThread(() -> {
                                    Toast.makeText(AddCourseDetailActivity.this, "添加课程失败", Toast.LENGTH_SHORT).show();
                                });
                                return;
                            }
                            // 将新课程添加到本地数据库
                            ContentValues values = new ContentValues();
                            values.put("score_id", newScoreId);
                            values.put("schedule_id", newScheduleId);
                            values.put("student_id", student_id);
                            values.put("score", 0);
                            db.insert("scores", null, values);

                            // 显示添加成功的提示信息
                            runOnUiThread(() -> {
                                Toast.makeText(AddCourseDetailActivity.this, "已添加" + AddCourseDetailActivity.this.newCourseName, Toast.LENGTH_SHORT).show();
                            });

                            // 刷新选课页面
                            refreshCoursePage();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        });
    }

    /**
     * 初始化视图
     * 设置状态栏透明
     * 设置渐变动画
     */
    private void initView() {
        // 将状态栏的背景颜色设置为透明
        // 将窗口的布局参数设置为 FLAG_LAYOUT_NO_LIMITS，这将使状态栏变为透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        layout = findViewById(R.id.bgLayout);

        // 设置渐变动画
        anim = (AnimationDrawable) layout.getBackground();
        anim.setEnterFadeDuration(2000); // 设置渐入效果持续时间
        anim.setExitFadeDuration(4000); // 设置渐出效果持续时间

        // 使用post()方法来延迟启动动画
        layout.post(new Runnable() {
            @Override
            public void run() {
                anim.start();
            }
        });
    }

    /**
     * 刷新选课页面
     * 重新加载CoursesFragment
     */
    private void refreshCoursePage() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("courseAdded", true);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Activity被销毁时，关闭数据库连接
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

}
