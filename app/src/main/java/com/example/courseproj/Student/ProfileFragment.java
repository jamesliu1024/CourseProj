package com.example.courseproj.Student;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.courseproj.Common.DB_SQLiteDatabaseHelper;
import com.example.courseproj.LoginActivity;
import com.example.courseproj.R;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ProfileFragment extends Fragment {
    SharedPreferences sharedPreferences;
    String student_id,studentName,studentGender,studentBirthday,startYear,years,teacherName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.student_fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 获取SharedPreferences对象
        sharedPreferences = getActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        student_id = sharedPreferences.getString("user_id", "");

        // 展示学生的个人信息
        // 通过学生ID从数据库中获取学生的个人信息
        getStudentInfo();

        // 在student_fragment_profile中展示学生的个人信息
        TextView tv_student_id = view.findViewById(R.id.profile_student_id);  // 学号
        tv_student_id.setText(student_id);
        TextView tv_student_name = view.findViewById(R.id.profile_student_name);  // 姓名
        tv_student_name.setText(studentName);
        TextView tv_student_gender = view.findViewById(R.id.profile_student_gender);  // 性别 0:男 1:女
        if (studentGender != null) {
            studentGender = studentGender.equals("0") ? "男" : "女";
        }
        tv_student_gender.setText(studentGender);
        TextView tv_student_birthday = view.findViewById(R.id.profile_student_birthday);  // 出生日期
        tv_student_birthday.setText(studentBirthday);
        TextView tv_start_year = view.findViewById(R.id.profile_start_year);  // 入学年份
        tv_start_year.setText(startYear);
        TextView tv_years = view.findViewById(R.id.profile_years);  // 学制
        tv_years.setText(years);
        TextView tv_teacher_name = view.findViewById(R.id.profile_teacher_name);  // 导师姓名
        tv_teacher_name.setText(teacherName);


        // 退出登录
        Button btn_logout = view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(v -> {
            // 使用SharedPreferences.Editor对象来清除所有的数据
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            // 使用commit()或apply()方法来保存更改
            editor.apply();

            // 清空本地SQLite数据库
            boolean isDeleted = getActivity().deleteDatabase("AndroidDB.db");
            if (isDeleted) {
//                Log.i("ProfileFragment", "本地数据库已清空");
            } else {
                Log.i("ProfileFragment", "本地数据库清空失败");
            }


            // 启动MainActivity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);

            // 结束当前的Activity
            getActivity().finish();
        });


    }

    /**
     * 从数据库中获取学生的个人信息
     * 通过学生ID从数据库中获取学生的个人信息
     */
    private void getStudentInfo() {
        DB_SQLiteDatabaseHelper dbHelper = new DB_SQLiteDatabaseHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // 执行查询
        String sql = "SELECT students.gender AS student_gender, teachers.gender AS teacher_gender, " +
                "students.birthday AS student_birthday, teachers.birthday AS teacher_birthday" +
                ", * FROM students JOIN teachers ON students.teacher_id = teachers.teacher_id WHERE students.student_id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{student_id});
        // TEST
        // 获取并打印表头信息
//        String[] columnNames = cursor.getColumnNames();
//        Log.i("ProfileFragment", "Table Headers: " + Arrays.toString(columnNames));

        // 检查查询结果
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex("student_name");
            int genderIndex = cursor.getColumnIndex("student_gender");
            int birthdayIndex = cursor.getColumnIndex("student_birthday");
            int start_yearIndex = cursor.getColumnIndex("start_year");
            int yearsIndex = cursor.getColumnIndex("years");
            int teacherIndex = cursor.getColumnIndex("teacher_name");

            studentName = nameIndex != -1 ? cursor.getString(nameIndex) : null;
            studentGender = genderIndex != -1 ? cursor.getString(genderIndex) : null;
            studentBirthday = birthdayIndex != -1 ? cursor.getString(birthdayIndex) : null;
            startYear = start_yearIndex != -1 ? cursor.getString(start_yearIndex) : null;
            years = yearsIndex != -1 ? cursor.getString(yearsIndex) : null;
            teacherName = teacherIndex != -1 ? cursor.getString(teacherIndex) : null;

            // TEST 打印学生信息
//            Log.i("ProfileFragment", "studentName: " + studentName +
//                    ", studentGender: " + studentGender + ", studentBirthday: " + studentBirthday +
//                    ", startYear: " + startYear + ", years: " + years + ", teacherName: " + teacherName);
        }
        cursor.close();
    }

}