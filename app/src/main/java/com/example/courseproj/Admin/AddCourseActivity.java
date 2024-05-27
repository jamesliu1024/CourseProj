package com.example.courseproj.Admin;

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
import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddCourseActivity extends AppCompatActivity {
    LinearLayout layout;
    AnimationDrawable anim;
    SharedPreferences sharedPreferences;
    private DB_SQLiteDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private EditText courseNameEditText;
    private EditText courseCreditEditText;
    private EditText courseHourEditText;
    private EditText courseWeekEditText;
    private RadioGroup courseTypeEditText;
    private Button addButton, backButton;
    private Spinner teacherSpinner;
    private NumberPicker courseDayPicker, courseTimePicker;
    private EditText course_place;
    private EditText course_year;
    private RadioGroup course_term;
    private int newCourseId, newScheduleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_course);
        initView();

        courseNameEditText = findViewById(R.id.course_name);
        courseCreditEditText = findViewById(R.id.course_credit);
        courseHourEditText = findViewById(R.id.course_hour);
        courseWeekEditText = findViewById(R.id.course_week);
        courseTypeEditText = findViewById(R.id.course_type);
        teacherSpinner = findViewById(R.id.teacher_spinner);
        courseDayPicker = findViewById(R.id.course_day_picker);
        courseTimePicker = findViewById(R.id.course_time_picker);
        course_place = findViewById(R.id.course_place);
        course_year = findViewById(R.id.course_year);
        course_term = findViewById(R.id.course_term);
        addButton = findViewById(R.id.btn_add);
        backButton = findViewById(R.id.btn_back);

        // 设置课程时间选择器的最大值和最小值
        courseDayPicker.setMinValue(1);
        courseDayPicker.setMaxValue(7);
        courseTimePicker.setMinValue(1);
        courseTimePicker.setMaxValue(8);

        // 获取数据库
        dbHelper = new DB_SQLiteDatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        // 获取任课教师数据
        List<String> tutors = getTeachers();
        // 创建一个 ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tutors);
        // 设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 将adapter 添加到spinner中
        teacherSpinner.setAdapter(adapter);
        // 添加事件Spinner事件监听
        teacherSpinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        // 设置默认值
        teacherSpinner.setVisibility(View.VISIBLE);


        addCourse();

        // 点击返回
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

    }

    /**
     * 下拉列表的监听器
     */
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectedTutor = parent.getItemAtPosition(position).toString();
            // 这里的 tutorTextView 是你用来显示选中的名字的 TextView
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // 这里处理没有选中任何项的情况
        }
    }


    /**
     * 获取任课教师数据
     *
     * @return 任课教师数据
     */
    private List<String> getTeachers() {
        List<String> teachers = new ArrayList<>();

        // 执行查询
        Cursor cursor = db.rawQuery("SELECT teacher_id, teacher_name FROM teachers", null);

        // 遍历结果
        while (cursor.moveToNext()) {
            int teacherIdIndex = cursor.getColumnIndex("teacher_id");
            int teacherNameIndex = cursor.getColumnIndex("teacher_name");
            String id = cursor.getString(teacherIdIndex);
            String name = cursor.getString(teacherNameIndex);
            teachers.add(id + " " + name);

            // TEST
//            Log.i("AddCourseActivity", "teacher_id: " + id + ", teacher_name: " + name);
        }

        return teachers;
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
     * 添加课程
     * 将用户输入的数据添加到数据库
     */
    private void addCourse() {
        // 为提交按钮设置点击事件监听器
        addButton.setOnClickListener(v -> {

            // 获取用户输入的数据
            String courseName = courseNameEditText.getText().toString();  // 课程名
            int courseCredit = Integer.parseInt(courseCreditEditText.getText().toString());  // 学分
            int courseHour = Integer.parseInt(courseHourEditText.getText().toString());  // 学时
            int courseWeek = Integer.parseInt(courseWeekEditText.getText().toString());  // 周数
            int courseType = courseTypeEditText.getCheckedRadioButtonId();  // 课程类型
            courseType = courseType == R.id.course_type_0 ? 0 : 1;
            int finalCourseType = courseType; // 课程类型
            int teacherId = Integer.parseInt(teacherSpinner.getSelectedItem().toString().split(" ")[0]);  // 任课教师id
            int courseDay = courseDayPicker.getValue();  // 上课日
            int courseTime = courseTimePicker.getValue();  // 上课时间
            String coursePlace = course_place.getText().toString();  // 上课地点
            int courseYear = Integer.parseInt(course_year.getText().toString());  // 学年
            int term = course_term.getCheckedRadioButtonId();  // 学期
            term = term == R.id.course_term_0 ? 0 : 1;
            int finalTerm = term;  // 学期

            // TEST
            Log.i("AddCourseActivity", "INSERT INTO courses (course_name, course_credit, course_hour, course_week, course_type) VALUES (?, ?, ?, ?, ?)\n" +
                    courseName + ", " + courseCredit + ", " + courseHour + ", " + courseWeek + ", " + finalCourseType);
            Log.i("AddCourseActivity", "INSERT INTO schedules (teacher_id, course_id, course_day, course_time, course_place, years, terms) VALUES (?, ?, ?, ?, ?, ?, ?)\n" +
                    teacherId + ", " + "newCourseId" + ", " + courseDay + ", " + courseTime + ", " + coursePlace + ", " + courseYear + ", " + finalTerm);

            // 检查用户输入的数据是否合法
            if (courseName.isEmpty() || courseCredit == 0 || courseHour == 0 || courseWeek == 0 || coursePlace.isEmpty() || courseYear == 0) {
                Toast.makeText(this, "请填写完整的课程信息", Toast.LENGTH_LONG).show();
                return;
            }

            // TODO 判断输入老师的该年度、学期、日期、时间是否已被占用
            // TODO 判断输入教室的该年度、学期、日期、时间是否已被占用

            // 进行服务器的数据库操作

            new Thread(() -> {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                try (Connection conn = DB_MySQLConnectionUtil.getConnection()) {
                    try {
                        // 开始事务
                        conn.setAutoCommit(false);
                        String sql = "INSERT INTO courses (course_name, course_credit, course_hour, course_week, course_type) VALUES (?, ?, ?, ?, ?)";
                        // 执行插入语句或者更新语句
                        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                            pstmt.setString(1, courseName);
                            pstmt.setInt(2, courseCredit);
                            pstmt.setInt(3, courseHour);
                            pstmt.setInt(4, courseWeek);
                            pstmt.setInt(5, finalCourseType);
                            pstmt.executeUpdate();

                            // 获取新插入的课程的id
                            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                                if (rs.next()) {
                                    newCourseId = rs.getInt(1);
                                    // 插入课程时间地点信息
                                    sql = "INSERT INTO schedules (teacher_id, course_id, course_day, course_time, course_place, years, terms) VALUES (?, ?, ?, ?, ?, ?, ?)";
                                    try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                                        ps.setInt(1, teacherId);
                                        ps.setInt(2, newCourseId);
                                        ps.setInt(3, courseDay);
                                        ps.setInt(4, courseTime);
                                        ps.setString(5, coursePlace);
                                        ps.setInt(6, courseYear);
                                        ps.setInt(7, finalTerm);
                                        ps.executeUpdate();

                                        // 获取新插入的schedule_id
                                        try (ResultSet rs2 = ps.getGeneratedKeys()) {
                                            if (rs2.next()) {
                                                newScheduleId = rs2.getInt(1);
                                            }
                                        }
                                    }
                                }
                            }

                        }
                        // 如果上述语句执行成功，那么提交事务
                        conn.commit();
                    } catch (SQLException e) {
                        // 如果上述语句执行失败，那么回滚事务
                        conn.rollback();
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
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
            if (newCourseId == 0 || newScheduleId == 0) {
                // 显示添加失败的提示信息
                runOnUiThread(() -> {
                    Toast.makeText(this, "添加课程失败", Toast.LENGTH_LONG).show();
                });
                return;
            }
            // 将新课程添加到本地数据库
            ContentValues values = new ContentValues();
            values.put("course_id", newCourseId);
            values.put("course_name", courseName);
            values.put("course_credit", courseCredit);
            values.put("course_hour", courseHour);
            values.put("course_week", courseWeek);
            values.put("course_type", finalCourseType);
            db.insert("courses", null, values);

            // 将新课程时间地点信息添加到本地数据库
            values.clear();
            values.put("schedule_id", newScheduleId);
            values.put("teacher_id", teacherId);
            values.put("course_id", newCourseId);
            values.put("course_day", courseDay);
            values.put("course_time", courseTime);
            values.put("course_place", coursePlace);
            values.put("years", courseYear);
            values.put("terms", finalTerm);
            db.insert("schedules", null, values);

            // 显示添加成功的提示信息
            runOnUiThread(() -> {
                Toast.makeText(this, "ScheduleId: " + newScheduleId + "\nnewCourseId: " + newCourseId + "添加成功", Toast.LENGTH_LONG).show();
            });

            finish();
        });

    }

}
