package com.example.courseproj.Admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.courseproj.Common.DB_SQLiteDatabaseHelper;
import com.example.courseproj.LoginActivity;
import com.example.courseproj.R;

public class AdminActivity extends AppCompatActivity {
    LinearLayout layout;
    AnimationDrawable anim;
    SharedPreferences sharedPreferences;
    String admin_id, adminName, adminGender, adminBirthday, startTime;
    private TextView adminIdTextView, adminNameTextView,
            adminGenderTextView, adminBirthdayTextView,
            adminStartTimeTextView;
    private LinearLayout addCourseLayout, addStudentLayout,
            editScoreLayout, changePasswordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);
        initView();

        // 获取SharedPreferences对象
        sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        admin_id = sharedPreferences.getString("user_id", "");

        // 展示管理员的个人信息
        getAdminInfo();
        // 管理员ID  姓名  性别  出生日期  入职时间
        adminIdTextView = findViewById(R.id.admin_id);
        adminNameTextView = findViewById(R.id.admin_name);
        adminGenderTextView = findViewById(R.id.admin_gender);
        adminBirthdayTextView = findViewById(R.id.admin_birthday);
        adminStartTimeTextView = findViewById(R.id.admin_start_time);
        // 在admin中展示管理员的个人信息
        adminIdTextView.setText(admin_id);
        adminNameTextView.setText(adminName);
        if (adminGender != null) {
            adminGender = adminGender.equals("0") ? "男" : "女";
        }
        adminGenderTextView.setText(adminGender);
        adminBirthdayTextView.setText(adminBirthday);
        adminStartTimeTextView.setText(startTime);


        // 点击不同模块跳转到不同的Activity
        // 找到 LinearLayout
        addCourseLayout = findViewById(R.id.add_course_layout);
        addStudentLayout = findViewById(R.id.add_student_layout);
        editScoreLayout = findViewById(R.id.edit_score_layout);
        changePasswordLayout = findViewById(R.id.change_password_layout);
        // 为每个模块设置点击事件监听器
        addCourseLayout.setOnClickListener(v -> {
            // 启动添加课程的Activity
            Intent intent = new Intent(this, AddCourseActivity.class);
            startActivity(intent);
        });

        addStudentLayout.setOnClickListener(v -> {
            // 启动添加学生的Activity
            Intent intent = new Intent(this, AddStudentActivity.class);
            startActivity(intent);
        });

        editScoreLayout.setOnClickListener(v -> {
            // 启动修改成绩的Activity
            Intent intent = new Intent(this, EditScoreActivity.class);
            startActivity(intent);
        });

        changePasswordLayout.setOnClickListener(v -> {
            // 启动修改密码的Activity
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(intent);
        });


        // 退出登录
        Button btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(v -> {
            // 使用SharedPreferences.Editor对象来清除所有的数据
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            // 使用commit()或apply()方法来保存更改
            editor.apply();

            // 清空本地SQLite数据库
            boolean isDeleted = deleteDatabase("AndroidDB.db");
            if (isDeleted) {
//                Log.i("AdminActivity", "本地数据库已清空");
            } else {
                Log.i("AdminActivity", "本地数据库清空失败");
            }


            // 启动MainActivity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

            // 结束当前的Activity
            finish();
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
     * 从数据库中获取管理员的个人信息
     * 通过管理员ID从数据库中获取管理员的个人信息
     */
    private void getAdminInfo() {
        DB_SQLiteDatabaseHelper dbHelper = new DB_SQLiteDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // 执行查询
        String sql = "SELECT * FROM admins WHERE admin_id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{admin_id});
        // TEST
        // 获取并打印表头信息
//        String[] columnNames = cursor.getColumnNames();
//        Log.i("ProfileFragment", "Table Headers: " + Arrays.toString(columnNames));

        // 检查查询结果
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex("admin_name");
            int genderIndex = cursor.getColumnIndex("gender");
            int birthdayIndex = cursor.getColumnIndex("birthday");
            int startTimeIndex = cursor.getColumnIndex("start_time");

            adminName = nameIndex != -1 ? cursor.getString(nameIndex) : null;
            adminGender = genderIndex != -1 ? cursor.getString(genderIndex) : null;
            adminBirthday = birthdayIndex != -1 ? cursor.getString(birthdayIndex) : null;
            startTime = startTimeIndex != -1 ? cursor.getString(startTimeIndex) : null;

            // TEST 打印信息
//            Log.i("ProfileFragment", "studentName: " + studentName +
//                    ", studentGender: " + studentGender + ", studentBirthday: " + studentBirthday +
//                    ", startYear: " + startYear + ", years: " + years + ", teacherName: " + teacherName);
        }
        cursor.close();
    }
}
