package com.example.courseproj.Student;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.courseproj.Common.DB_SQLiteDatabaseHelper;
import com.example.courseproj.R;
import org.jetbrains.annotations.NotNull;

public class CoursesFragment extends Fragment {
    int current_year, current_month, current_term;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.student_fragment_courses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();  // 获取传递过来的参数
        if (arguments != null) {
            // 获取当前年月，如果是2-8月，上年度的下半学期1，9-1月是当前年度的上半学期0
            current_year = arguments.getInt("current_year");
            current_month = arguments.getInt("current_month");
        }
        if (current_month >= 2 && current_month <= 8) {
            current_term = 1;
        } else {
            current_term = 0;
        }
//        Log.i("CoursesFragment", "current_year: " + current_year + ", current_month: " + current_month);

        // TODO 从本地数据库中获取学生课表信息并展示在listview中
        // 从sharedpreferences中获取学生学号
        String student_id = getActivity().getSharedPreferences("user_data", 0).getString("user_id", "");
        // TEST
//        Log.i("CoursesFragment", "student_id: " + student_id);

        // 获取ListView对象
        ListView lv_courses = view.findViewById(R.id.lv_courses);
        // 获取数据库对象
        DB_SQLiteDatabaseHelper dbHelper = new DB_SQLiteDatabaseHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // 执行查询
        String[] selectionArgs = {student_id, String.valueOf(current_year), String.valueOf(current_term)};
        Cursor cursor = db.query("v_scores_schedules_courses", null, "student_id = ? AND years = ? AND terms = ?", selectionArgs, null, null, null);
        // Log测试获取到数据内容
        Log.i("CoursesFragment", "cursor: " + cursor.getCount());
        // 创建适配器
        String[] from = {"course_name", "teacher_name", "course_place", "course_day", "course_time"};  // 这些字段是课表表中的字段
        int[] to = {R.id.tv_course_name, R.id.tv_teacher_name, R.id.tv_course_place, R.id.tv_course_day, R.id.tv_course_time};  // 这些ID是ListView的item布局中的TextView的ID
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.item_courses, cursor, from, to, 0);
        // 设置适配器
        lv_courses.setAdapter(adapter);

    }
}