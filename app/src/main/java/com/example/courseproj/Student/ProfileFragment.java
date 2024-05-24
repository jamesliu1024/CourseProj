package com.example.courseproj.Student;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.courseproj.LoginActivity;
import com.example.courseproj.R;
import org.jetbrains.annotations.NotNull;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.student_fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO 从数据库中获取学生课表信息
        // 获取ListView对象
        LinearLayout lv_courses = view.findViewById(R.id.lv_courses);
        // 设置ListView的Adapter
//        lv_courses.setAdapter(new CourseAdapter(getActivity(), R.layout.course_item, courses));


        // 退出登录
        Button btn_logout = view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(v -> {
            // 获取SharedPreferences对象
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);

            // 使用SharedPreferences.Editor对象来清除所有的数据
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();

            // 使用commit()或apply()方法来保存更改
            editor.apply();

            // 启动MainActivity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);

            // 结束当前的Activity
            getActivity().finish();
        });


    }

}