package com.example.courseproj.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.courseproj.Common.DB_SQLiteDB;
import com.example.courseproj.R;
public class EditScoreActivity extends AppCompatActivity {
    LinearLayout layout;
    AnimationDrawable anim;
    SharedPreferences sharedPreferences;
    private DB_SQLiteDB dbHelper;
    private SQLiteDatabase db;
    EditText edit_student_id;
    Button btn_query, btn_clear;
    ListView lv_courses;
    int current_year, current_month, current_term;
    String student_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_edit_score);
        initView();

        // 获取SharedPreferences对象
        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        Intent intent = getIntent();
        current_year = intent.getIntExtra("current_year", 0);
        current_month = intent.getIntExtra("current_month", 0);
        current_term = intent.getIntExtra("current_term", 0);

        // 获取数据库
        dbHelper = new DB_SQLiteDB(this);
        db = dbHelper.getSqliteObject(this);

        edit_student_id = findViewById(R.id.edit_student_id);
        btn_query = findViewById(R.id.btn_submit);
        btn_clear = findViewById(R.id.btn_clear);
        lv_courses = findViewById(R.id.lv_courses);

        // 查询按钮的点击事件
        btn_query.setOnClickListener(v -> {
            student_id = edit_student_id.getText().toString().trim();
            if (student_id.isEmpty()) {
                Toast.makeText(this, "请输入学号", Toast.LENGTH_SHORT).show();
                return;
            }
            // 查询学生的课程信息
            String[] selectionArgs = {student_id, String.valueOf(current_year), String.valueOf(current_term)};
            Log.i("EditScoreActivity", "selectionArgs: " + selectionArgs[0] + " " + selectionArgs[1] + " " + selectionArgs[2]);
            Cursor cursor = db.query("v_scores_schedules_courses", null,
                    "student_id = ? AND years = ? AND terms = ?", selectionArgs,
                    null, null, "course_day ASC, course_time ASC");
            // 创建适配器，将数据展示在ListView中
            String[] from = {"course_name", "teacher_name", "course_place", "course_day", "course_time", "schedule_id", "course_id", "teacher_id"};  // 这些字段是课表表中的字段
            int[] to = {R.id.tv_course_name, R.id.tv_teacher_name, R.id.tv_course_place, R.id.tv_course_day, R.id.tv_course_time};  // 这些ID是ListView的item布局中的TextView的ID
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item_courses, cursor, from, to, 0);
            lv_courses.setAdapter(adapter);
        });

        // 点击ListView中的item，跳转到课程详情页面
        lv_courses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取被点击的课程的信息
                Cursor cursor = (Cursor) lv_courses.getItemAtPosition(position);
                int scheduleIndex = cursor.getColumnIndex("schedule_id");
                int courseIndex = cursor.getColumnIndex("course_id");
                int teacherIndex = cursor.getColumnIndex("teacher_id");
                int studentNameIndex = cursor.getColumnIndex("student_name");
                int courseNameIndex = cursor.getColumnIndex("course_name");
                String scheduleId = scheduleIndex != -1 ? cursor.getString(scheduleIndex) : null;
                String courseId = courseIndex != -1 ? cursor.getString(courseIndex) : null;
                String teacherId = teacherIndex != -1 ? cursor.getString(teacherIndex) : null;
                String studentName = studentNameIndex != -1 ? cursor.getString(studentNameIndex) : null;
                String courseName = courseNameIndex != -1 ? cursor.getString(courseNameIndex) : null;

                // 启动新的Activity
                Intent intent = new Intent(EditScoreActivity.this, CourseDetailActivity.class);
                intent.putExtra("schedule_id", scheduleId);
                intent.putExtra("course_id", courseId);
                intent.putExtra("course_name", courseName);
                intent.putExtra("current_year", current_year);
                intent.putExtra("current_month", current_month);
                intent.putExtra("current_term", current_term);
                intent.putExtra("student_id",student_id);
                intent.putExtra("student_name",studentName);
                intent.putExtra("teacher_id", teacherId);

                startActivity(intent);
            }
        });

        // 清空按钮的点击事件
        btn_clear.setOnClickListener(v -> {
            edit_student_id.setText("");
            lv_courses.setAdapter(null);
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