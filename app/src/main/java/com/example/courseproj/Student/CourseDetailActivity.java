package com.example.courseproj.Student;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.courseproj.Common.DB_SQLiteDatabaseHelper;
import com.example.courseproj.R;

public class CourseDetailActivity extends AppCompatActivity {
    LinearLayout layout;
    AnimationDrawable anim;
    int current_year, current_month, current_term;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_detail);

        initView();

        DB_SQLiteDatabaseHelper dbHelper = new DB_SQLiteDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 从sharedpreferences中获取学生学号
        String student_id = getSharedPreferences("user_data", 0).getString("user_id", "");

        // 从Intent中获取课程信息
        Intent intent = getIntent();
        String scheduleId = intent.getStringExtra("schedule_id");  // 课程表id
        String courseId = intent.getStringExtra("course_id");  // 课程id
        current_year = intent.getIntExtra("current_year", 0);
        current_month = intent.getIntExtra("current_month", 0);
        current_term = intent.getIntExtra("current_term", 0);

        // 通过scheduleId从数据库查询课程信息
        String[] selectionArgs = {scheduleId};
        Cursor cursor = db.query("v_scores_schedules_courses", null,  "schedule_id = ?", selectionArgs,
                null, null, null);
        cursor.moveToFirst();
        // 课程名称  任课教师  课程学分  课程学时 上课周数  课程类型  成绩
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
        String score = scoreIndex != -1 ? cursor.getString(scoreIndex) : null;
        cursor.close();

        // 显示课程信息
        TextView tvCourseName = findViewById(R.id.tv_course_title);
        TextView tvTeacherName = findViewById(R.id.tv_teacher);
        TextView tvCourseCredit = findViewById(R.id.tv_credit);
        TextView tvCourseHour = findViewById(R.id.tv_hour);
        TextView tvCourseWeek = findViewById(R.id.tv_week);
        TextView tvCourseType = findViewById(R.id.tv_type);
        TextView tvScore = findViewById(R.id.tv_student_score);

        tvCourseName.setText(courseName);
        tvTeacherName.setText(teacherName);
        tvCourseCredit.setText(courseCredit);
        tvCourseHour.setText(courseHour);
        tvCourseWeek.setText(courseWeek);
        tvCourseType.setText(courseType);
        tvScore.setText(score);


        // TODO
        // 执行查询来获取同班同学的列表
//        String[] selectionArgs2 = {scheduleId, student_id};
//        Cursor cursor2 = db.query("v_scores_schedules_courses", null, "schedule_id = ? AND student_id != ?", selectionArgs2,
//                null, null, "student_id");
//
//        Log.i("CourseDetailActivity", "cursor2: " + cursor2.getColumnNames());
//
//        // 创建适配器，将数据展示在ListView中
//        String[] from = {"student_name", "student_id", ""};  // 这些字段是课表表中的字段
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
}
