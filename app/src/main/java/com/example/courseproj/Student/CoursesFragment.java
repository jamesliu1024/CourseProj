package com.example.courseproj.Student;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.courseproj.Common.DB_SQLiteDatabaseHelper;
import com.example.courseproj.R;
import org.jetbrains.annotations.NotNull;

public class CoursesFragment extends Fragment {
    int current_year, current_month, current_term;
    // 创建一个数组来存储星期的名称
    final String[] weekDays = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
    // 创建一个数组来存储课程的时间
    final String[] courseTimes = {"1-2节 09:00-10:20", "3-4节 10:40-12:00", "5-6节 12:30-13:50", "7-8节 14:00-15:20",
            "9-10节 15:30-16:50", "11-12节 17:00-18:20", "13-14节 19:00-20:20", "15-16节 20:30-21:50"};

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
            // 如果term为1，表示去年年度下学期，否则表示今年年度上学期
            current_year = arguments.getInt("current_year");
            current_month = arguments.getInt("current_month");
            current_term = arguments.getInt("current_term");
        }
//        if (current_month >= 2 && current_month <= 8) {
//            current_year = current_year - 1;
//            current_term = 1;
//        } else {
//            current_term = 0;
//        }
//        Log.i("CoursesFragment", "current_year: " + current_year + ", current_month: " + current_month);

        // 从本地数据库中获取学生课表信息并展示在listview中
        // 从sharedpreferences中获取学生学号
        String student_id = getActivity().getSharedPreferences("user_data", 0).getString("user_id", "");
        // TEST 获取student_id
//        Log.i("CoursesFragment", "student_id: " + student_id);

        // 获取ListView对象
        ListView lv_courses = view.findViewById(R.id.lv_courses);
        // 获取数据库对象
        DB_SQLiteDatabaseHelper dbHelper = new DB_SQLiteDatabaseHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // 执行查询
        String[] selectionArgs = {student_id, String.valueOf(current_year), String.valueOf(current_term)};
        Cursor cursor = db.query("v_scores_schedules_courses", null,
                "student_id = ? AND years = ? AND terms = ?", selectionArgs,
                null, null, "course_day ASC, course_time ASC");
        // TEST 获取selectionArgs
//        Log.i("CoursesFragment", "selectionArgs: " + selectionArgs[0] + ", " + selectionArgs[1] + ", " + selectionArgs[2]);
        // TEST 获取并打印表头信息
//        Log.i("CoursesFragment", "cursor: " + cursor.getCount());
//        String[] columnNames = cursor.getColumnNames();
//        Log.i("CoursesFragment", "Table Headers: " + Arrays.toString(columnNames));
        // TEST Log测试获取到数据内容
//        Log.i("CoursesFragment", "cursor: " + cursor.getCount());


        // 创建适配器，将数据展示在ListView中
        String[] from = {"course_name", "teacher_name", "course_place", "course_day", "course_time", "schedule_id", "course_id", "teacher_id"};  // 这些字段是课表表中的字段
        int[] to = {R.id.tv_course_name, R.id.tv_teacher_name, R.id.tv_course_place, R.id.tv_course_day, R.id.tv_course_time};  // 这些ID是ListView的item布局中的TextView的ID
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.item_courses, cursor, from, to, 0);

        // 设置适配器
        lv_courses.setAdapter(adapter);

        // 设置ViewBinder，用于处理上课时间和上课日期的显示
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.tv_course_day) {
                    int courseDay = cursor.getInt(columnIndex);
                    String weekDay = weekDays[courseDay - 1];
                    ((TextView) view).setText(weekDay);
                    return true;  // true表示已经处理，不需要适配器继续处理
                }
                else if (view.getId() == R.id.tv_course_time) {
                    int courseTime = cursor.getInt(columnIndex);
                    String courseTimeStr = courseTimes[courseTime - 1];
                    ((TextView) view).setText(courseTimeStr);
                    return true;  // true表示已经处理，不需要适配器继续处理
                }
                return false;  // false表示未处理，需要适配器继续处理
            }
        });

        // 在CoursesFragment中
        lv_courses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取被点击的课程的信息
                Cursor cursor = (Cursor) lv_courses.getItemAtPosition(position);
                int scheduleIndex = cursor.getColumnIndex("schedule_id");
                int courseIndex = cursor.getColumnIndex("course_id");
//                int teacherIndex = cursor.getColumnIndex("teacher_id");
                String scheduleId = scheduleIndex != -1 ? cursor.getString(scheduleIndex) : null;
                String courseId = courseIndex != -1 ? cursor.getString(courseIndex) : null;
//                String teacherId = teacherIndex != -1 ? cursor.getString(teacherIndex) : null;

                // 启动新的Activity
                Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
                intent.putExtra("schedule_id", scheduleId);
                intent.putExtra("course_id", courseId);
                intent.putExtra("current_year", current_year);
                intent.putExtra("current_month", current_month);
                intent.putExtra("current_term", current_term);

//                intent.putExtra("teacher_id", teacherId);

                startActivity(intent);
            }
        });

    }
}