package com.example.courseproj.Admin;

import android.app.DatePickerDialog;
import android.content.ContentValues;
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
import com.example.courseproj.Student.AddCourseDetailActivity;
import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddStudentActivity extends AppCompatActivity {
    LinearLayout layout;
    AnimationDrawable anim;
    SharedPreferences sharedPreferences;
    private TextView birthdayTextView;
    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private NumberPicker yearsPicker;
    private Button submitButton;

    private RadioGroup genderRadioGroup;
    private EditText startYearEditText;
    private Spinner tutorSpinner;
    private String newStudentId;
    private DB_SQLiteDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_student);
        initView();

        genderRadioGroup = findViewById(R.id.edit_student_gender);
        birthdayTextView = findViewById(R.id.edit_student_birthday);
        startYearEditText = findViewById(R.id.edit_student_start_year);
        nameEditText = findViewById(R.id.edit_student_name);
        passwordEditText = findViewById(R.id.edit_student_password);
        confirmPasswordEditText = findViewById(R.id.edit_student_confirm_password);
        yearsPicker = findViewById(R.id.edit_student_years);
        submitButton = findViewById(R.id.submit);
        tutorSpinner = findViewById(R.id.edit_student_tutor);

        // 设置学制的范围
        yearsPicker.setMinValue(2);
        yearsPicker.setMaxValue(5);

        // 获取数据库
        dbHelper = new DB_SQLiteDatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        // 获取导师数据
        List<String> tutors = getTutors();
        // 创建一个 ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tutors);
        // 设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 将adapter 添加到spinner中
        tutorSpinner.setAdapter(adapter);
        // 添加事件Spinner事件监听
        tutorSpinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        // 设置默认值
        tutorSpinner.setVisibility(View.VISIBLE);


        // 为出生年月设置点击事件监听器
        birthdayTextView.setOnClickListener(v -> {
            // 弹出日期选择器
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> birthdayTextView.setText(year + "-" + (month + 1) + "-" + dayOfMonth),
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        addStudent();

        // 点击返回
        findViewById(R.id.back).setOnClickListener(v -> finish());

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

        // 确保 layout 不为 null
        if (layout != null) {
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


    /**
     * 下拉列表的监听器
     * 用于监听用户选择的导师
     * 并将选择的导师显示在 TextView 中
     * 如果用户没有选择任何导师，则显示提示信息
     */
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectedTutor = parent.getItemAtPosition(position).toString();
            // 这里的 tutorTextView 是你用来显示选中的导师的名字的 TextView
//            tutorTextView.setText(selectedTutor);
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // 这里处理没有选中任何项的情况
        }
    }

    /**
     * 获取导师数据
     * @return 导师数据
     */
    private List<String> getTutors() {
        List<String> tutors = new ArrayList<>();

        // 执行查询
        Cursor cursor = db.rawQuery("SELECT teacher_id, teacher_name FROM teachers", null);

        // 遍历结果
        while (cursor.moveToNext()) {
            int teacherIdIndex = cursor.getColumnIndex("teacher_id");
            int teacherNameIndex = cursor.getColumnIndex("teacher_name");
            String id = cursor.getString(teacherIdIndex);
            String name = cursor.getString(teacherNameIndex);
            tutors.add(id + " " + name);

            // TEST
//            Log.i("AddStudentActivity", "teacher_id: " + id + ", teacher_name: " + name);
        }

        return tutors;
    }

    /**
     * 添加学生
     * 将学生信息添加到服务器数据库和本地数据库
     */
    private void addStudent() {
        // 为提交按钮设置点击事件监听器
        submitButton.setOnClickListener(v -> {

            // 获取用户输入的数据
            String name = nameEditText.getText().toString();  // 姓名
            String password = passwordEditText.getText().toString();  // 密码
            String confirmPassword = confirmPasswordEditText.getText().toString();  // 确认密码
            int years = yearsPicker.getValue();  // 学制
            int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();  // 性别
            int gender = selectedGenderId == R.id.edit_student_gender_0 ? 0 : 1;
            String birthday = birthdayTextView.getText().toString();  // 出生年月
            int startYear = Integer.parseInt(startYearEditText.getText().toString());  // 入学年份
            int tutorId = Integer.parseInt(tutorSpinner.getSelectedItem().toString().split(" ")[0]);  // 导师id

            // 检查用户输入的数据是否合法
            if (name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || birthday.isEmpty()) {
                Toast.makeText(this, "请填写完整的信息", Toast.LENGTH_LONG).show();
                return;
            }
            if (!password.equals(confirmPassword)) {
                runOnUiThread(() -> {
                    passwordEditText.setText("");
                    confirmPasswordEditText.setText("");
                    Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_LONG).show();
                });
                return;
            }

            // 对密码进行MD5加密
            String finalPassword = MD5.md5(password);

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
                    String sql = "INSERT INTO students (student_name, student_password, gender, birthday, start_year, years, teacher_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, finalPassword);
                    preparedStatement.setInt(3,gender);
                    preparedStatement.setString(4,birthday);
                    preparedStatement.setInt(5,startYear);
                    preparedStatement.setInt(6,years);
                    preparedStatement.setInt(7,tutorId);
                    preparedStatement.executeUpdate();
                    // 获取自增id
                    resultSet = preparedStatement.getGeneratedKeys();
                    if (resultSet == null) {
                        throw new SQLException("添加学生失败，无法获取自增id");
                    }
                    resultSet.next();
                    newStudentId = resultSet.getString(1);
                    Log.i("AddStudentActivity", "newStudentId: " + newStudentId);  //TEST

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

            // 等待1秒，如果newStudentId为空，表示添加课程失败
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (newStudentId == null) {
                // 显示添加失败的提示信息
                runOnUiThread(() -> {
                    Toast.makeText(this, "添加学生失败", Toast.LENGTH_SHORT).show();
                });
                return;
            }
            // 将新学生添加到本地数据库
            ContentValues values = new ContentValues();
            values.put("student_id", newStudentId);
            values.put("student_name", name);
            values.put("student_password", password);
            values.put("gender", gender);
            values.put("birthday", birthday);
            values.put("start_year", startYear);
            values.put("years", years);
            values.put("teacher_id", tutorId);
            db.insert("students", null, values);

            // 显示添加成功的提示信息
            runOnUiThread(() -> {
                Toast.makeText(this, "已添加学号为：\t" + newStudentId + "\t的学生" , Toast.LENGTH_LONG).show();
            });

           finish();
        });

    }
}
