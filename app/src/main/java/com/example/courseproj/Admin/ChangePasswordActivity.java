package com.example.courseproj.Admin;

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
import com.example.courseproj.Common.DB_MySQLConnectionUtil;
import com.example.courseproj.Common.DB_SQLiteDatabaseHelper;
import com.example.courseproj.Common.MD5;
import com.example.courseproj.R;
import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChangePasswordActivity extends AppCompatActivity {
    LinearLayout layout;
    AnimationDrawable anim;
    private DB_SQLiteDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private TextView tvStudentName, tvGender, tvBirthday, tvStartYear, tvStudentPassword, tvYears, tvTeacher;
    private EditText tvStudentId, editModifyPassword;
    private Button btnQuery, btnSubmit;
    private String student_id, studentName, studentGender, studentBirthday, startYear, years, teacherName, teacherId, studentPassword, modifyPassword;
    private TableRow trInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_change_password);
        initView();

        // 获取组件
        tvStudentId = findViewById(R.id.edit_id);
        editModifyPassword = findViewById(R.id.edit_modify_password);
        tvStudentName = findViewById(R.id.tv_student_name);
        tvStudentPassword = findViewById(R.id.tv_student_password);
        tvGender = findViewById(R.id.tv_gender);
        tvBirthday = findViewById(R.id.tv_birthday);
        tvStartYear = findViewById(R.id.tv_start_year);
        tvYears = findViewById(R.id.tv_years);
        tvTeacher = findViewById(R.id.tv_teacher);
        btnQuery = findViewById(R.id.btn_query);
        btnSubmit = findViewById(R.id.btn_submit);
        trInput = findViewById(R.id.tr_input);

        // 获取数据库
        dbHelper = new DB_SQLiteDatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        // 查询学生信息
        btnQuery.setOnClickListener(v -> {
            queryStudentInfo();
        });

        // 提交修改
        btnSubmit.setOnClickListener(v -> {
            updatePassword();
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
     * 查询学生信息
     */
    private void queryStudentInfo() {
        student_id = tvStudentId.getText().toString();
        editModifyPassword.setText("");
        if (student_id.equals("")) {
            return;
        }
        // 查询学生信息
        String sql = "SELECT students.gender AS student_gender, teachers.gender AS teacher_gender, " +
                "students.birthday AS student_birthday, teachers.birthday AS teacher_birthday" +
                ", * FROM students JOIN teachers ON students.teacher_id = teachers.teacher_id WHERE students.student_id = ?";
        String[] selectionArgs = {student_id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        if (cursor.moveToNext()) {
            int nameIndex = cursor.getColumnIndex("student_name");
            int passwordIndex = cursor.getColumnIndex("student_password");
            int genderIndex = cursor.getColumnIndex("student_gender");
            int birthdayIndex = cursor.getColumnIndex("student_birthday");
            int start_yearIndex = cursor.getColumnIndex("start_year");
            int yearsIndex = cursor.getColumnIndex("years");
            int teacherIndex = cursor.getColumnIndex("teacher_name");
            int teacherIdIndex = cursor.getColumnIndex("teacher_id");

            studentName = nameIndex != -1 ? cursor.getString(nameIndex) : null;
            studentPassword = passwordIndex != -1 ? cursor.getString(passwordIndex) : null;
            studentGender = genderIndex != -1 ? cursor.getString(genderIndex) : null;
            studentBirthday = birthdayIndex != -1 ? cursor.getString(birthdayIndex) : null;
            startYear = start_yearIndex != -1 ? cursor.getString(start_yearIndex) : null;
            years = yearsIndex != -1 ? cursor.getString(yearsIndex) : null;
            teacherName = teacherIndex != -1 ? cursor.getString(teacherIndex) : null;
            teacherId = teacherIdIndex != -1 ? cursor.getString(teacherIdIndex) : null;

            if (studentGender != null) {
                studentGender = studentGender.equals("0") ? "男" : "女";
            }
            String teacher = teacherId + " " + teacherName;

            tvStudentName.setText(studentName);
            tvStudentPassword.setText(studentPassword);
            tvGender.setText(studentGender);
            tvBirthday.setText(studentBirthday);
            tvStartYear.setText(startYear);
            tvYears.setText(years);
            tvTeacher.setText(teacher);

            trInput.setVisibility(View.VISIBLE); // 显示输入框

        } else {
            tvStudentName.setText("未找到该学生");
            tvStudentPassword.setText("");
            tvGender.setText("");
            tvBirthday.setText("");
            tvStartYear.setText("");
            tvYears.setText("");
            tvTeacher.setText("");

            trInput.setVisibility(View.INVISIBLE); // 显示输入框
            Toast.makeText(this, "未找到该学生", Toast.LENGTH_LONG).show();
        }
        cursor.close();

    }


    /**
     * 更新密码
     */
    private void updatePassword() {
        modifyPassword = editModifyPassword.getText().toString().trim();
        modifyPassword = MD5.md5(modifyPassword);
        if (modifyPassword.equals("")) {
            Toast.makeText(this, "请输入新密码", Toast.LENGTH_LONG).show();
            return;
        }
        // 更新密码
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
                String sql = "UPDATE students SET student_password = ? WHERE student_id = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, modifyPassword);
                preparedStatement.setString(2, student_id);
                if (preparedStatement.executeUpdate() > 0) {
                    String sql2 = "UPDATE students SET student_password = ? WHERE student_id = ?";
                    Object[] bindArgs = {modifyPassword, student_id};
                    db.execSQL(sql2, bindArgs);
                    queryStudentInfo();
                    runOnUiThread(() -> {
                        Toast.makeText(this, "修改成功", Toast.LENGTH_LONG).show();
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "修改失败", Toast.LENGTH_LONG).show();
                    });
                }
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
    }

}
