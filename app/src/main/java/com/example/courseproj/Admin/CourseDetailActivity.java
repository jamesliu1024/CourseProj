package com.example.courseproj.Admin;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.courseproj.Common.DB_MySQLConnectionUtil;
import com.example.courseproj.Common.DB_SQLiteDB;
import com.example.courseproj.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 显示课程的详细信息，并修改成绩
 */
public class CourseDetailActivity extends AppCompatActivity {
    LinearLayout layout;
    AnimationDrawable anim;
    int current_year, current_month, current_term;
    private DB_SQLiteDB dbHelper;
    private SQLiteDatabase db;
    EditText etScore;
    Button btnSubmit;
    String score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_course_detail);

        initView();

        // 获取数据库
        dbHelper = new DB_SQLiteDB(this);
        db = dbHelper.getSqliteObject(this);

        // 从Intent中获取课程信息
        Intent intent = getIntent();
        String scheduleId = intent.getStringExtra("schedule_id");  // 课程表id
        String courseId = intent.getStringExtra("course_id");  // 课程id
        String student_id = intent.getStringExtra("student_id");  // 学生id
        String student_name = intent.getStringExtra("student_name");  // 学生姓名
        current_year = intent.getIntExtra("current_year", 0);
        current_month = intent.getIntExtra("current_month", 0);
        current_term = intent.getIntExtra("current_term", 0);

        // 通过scheduleId从数据库查询课程信息
        String[] selectionArgs = {scheduleId, student_id};
        Cursor cursor = db.query("v_scores_schedules_courses", null,  "schedule_id = ? and student_id = ?", selectionArgs,
                null, null, null);
        cursor.moveToFirst();
        int courseNameIndex = cursor.getColumnIndex("course_name");
        int teacherNameIndex = cursor.getColumnIndex("teacher_name");
        int courseCreditIndex = cursor.getColumnIndex("course_credit");
        int courseHourIndex = cursor.getColumnIndex("course_hour");
        int courseWeekIndex = cursor.getColumnIndex("course_week");
        int courseTypeIndex = cursor.getColumnIndex("course_type");
        int scoreIndex = cursor.getColumnIndex("score");
        String courseName = courseNameIndex != -1 ? cursor.getString(courseNameIndex) : null;
        String teacherName = teacherNameIndex != -1 ? cursor.getString(teacherNameIndex) : null;
        String courseCredit = courseCreditIndex != -1 ? cursor.getString(courseCreditIndex) : null;
        String courseHour = courseHourIndex != -1 ? cursor.getString(courseHourIndex) : null;
        String courseWeek = courseWeekIndex != -1 ? cursor.getString(courseWeekIndex) : null;
        String courseType = courseTypeIndex != -1 ? cursor.getString(courseTypeIndex) : null;
        score = scoreIndex != -1 ? cursor.getString(scoreIndex) : null;
        cursor.close();

        // TEST
//        Log.i("CourseDetailActivity", "courseName: " + courseName + ", teacherName: " + teacherName + ", courseCredit: " + courseCredit
//                + ", courseHour: " + courseHour + ", courseWeek: " + courseWeek + ", courseType: " + courseType + ", score: " + score);

        // courseType: 0: 必修课 1: 选修课
        courseType = courseType.equals("0") ? "必修课" : "选修课";

        TextView tvStudentId = findViewById(R.id.tv_student_id);
        TextView tvStudentName = findViewById(R.id.tv_student_name);
        TextView tvCourseId = findViewById(R.id.tv_course_id);
        TextView tvCourseName = findViewById(R.id.tv_course_title);
        TextView tvTeacherName = findViewById(R.id.tv_teacher);
        TextView tvCourseCredit = findViewById(R.id.tv_credit);
        TextView tvCourseHour = findViewById(R.id.tv_hour);
        TextView tvCourseWeek = findViewById(R.id.tv_week);
        TextView tvCourseType = findViewById(R.id.tv_type);
        etScore = findViewById(R.id.tv_student_score);
        btnSubmit = findViewById(R.id.btn_submit);

        tvCourseId.setText(courseId);
        tvCourseName.setText(courseName);
        tvTeacherName.setText(teacherName);
        tvCourseCredit.setText(courseCredit);
        tvCourseHour.setText(courseHour);
        tvCourseWeek.setText(courseWeek);
        tvCourseType.setText(courseType);
        etScore.setText(score);
        tvStudentId.setText(student_id);
        tvStudentName.setText(student_name);

        // 点击查询按钮，更新成绩
        btnSubmit.setOnClickListener(v -> {
            String newScore = etScore.getText().toString();
            if (!newScore.equals(score)) {
                // 验证输入成绩是否为0-100的数字
                if (!newScore.matches("^[0-9]{1,3}$") || Integer.parseInt(newScore) < 0 || Integer.parseInt(newScore) > 100) {
                    etScore.setText(score);  // 输入不合法，恢复原成绩
                    Toast.makeText(this, "请输入0-100的数字", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 进行服务器的数据库操作
                new Thread(() -> {
                    Connection connection = null;
                    PreparedStatement preparedStatement = null;
                    ResultSet resultSet = null;
                    try {
                        connection = DB_MySQLConnectionUtil.getConnection();
                        if (connection == null) {
                            throw new SQLException("无法连接到数据库");
                        }
                        String sql = "update scores set score = ? where schedule_id = ? and student_id = ?";
                        preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, newScore);
                        preparedStatement.setString(2, scheduleId);
                        preparedStatement.setString(3, student_id);
                        preparedStatement.executeUpdate();

                        ContentValues values = new ContentValues();
                        values.put("score", newScore);
                        db.update("scores", values, "schedule_id = ? and student_id = ?", new String[]{scheduleId, student_id});
                        runOnUiThread(() -> {
                            score = newScore;
                            Toast.makeText(this, "成绩修改成功", Toast.LENGTH_SHORT).show();
                        });

                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            Log.i("CourseDetailActivity", "newScore: " + newScore + ", scheduleId: " + scheduleId + ", student_id: " + student_id);
                            Toast.makeText(this, "成绩修改失败", Toast.LENGTH_SHORT).show();
                        });
                        e.printStackTrace();
                    } finally {
                        try {
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
            }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Activity被销毁时，关闭数据库连接
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

}
