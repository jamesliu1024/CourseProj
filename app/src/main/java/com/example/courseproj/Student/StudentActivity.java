package com.example.courseproj.Student;

import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.courseproj.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 学生主页面
 * 包含底部导航栏
 * 三个Fragment：CoursesFragment、AddCoursesFragment、ProfileFragment
 * 默认展示ProfileFragment
 * 通过底部导航栏切换Fragment
 * 通过Bundle传递当前学年、学期和月份
 * 通过SharedPreferences获取学生学号
 * 通过Intent获取课程信息
 */
public class StudentActivity extends AppCompatActivity {
    LinearLayout layout;
    AnimationDrawable anim;

    private BottomNavigationView bottomNavigationView;  // 底部导航栏
    int current_year, current_month, current_term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student);

        initView();

        // 展示当前的学年和学期
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        // 获取当前年月份
        current_year = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
        current_month = Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(new Date()));
        // 判断当前月份，如果是2-8月，上年度的下半学期1，9-1月是当前年度的上半学期0
        if (current_month >= 2 && current_month <= 8) {
            current_year = current_year - 1;
            current_term = 1;
        } else {
            current_term = 0;
        }
        if (current_term == 1) {
            toolbar_title.setText(current_year + "-" + (current_year + 1) + "学年\t下学期");
        } else {
            toolbar_title.setText(current_year + "-" + (current_year + 1) + "学年\t上学期");
        }
        // 创建一个Bundle对象
        Bundle bundle = new Bundle();
        // 将current_year、current_month和current_term放入Bundle
        bundle.putInt("current_year", current_year);
        bundle.putInt("current_month", current_month);
        bundle.putInt("current_term", current_term);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.navigation_courses) {
                    selectedFragment = new CoursesFragment();

                } else if (item.getItemId() == R.id.navigation_addCourses) {
                    selectedFragment = new AddCoursesFragment();
                } else if (item.getItemId() == R.id.navigation_profile) {
                    selectedFragment = new ProfileFragment();
                }
                // 将Bundle设置为Fragment的参数
                selectedFragment.setArguments(bundle);

                // 切换Fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container, selectedFragment).commit();

                return true;
            }
        });

        // 默认选择ProfileFragment
        Fragment defaultFragment = new ProfileFragment();
        defaultFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container, defaultFragment).commit();
        // 设置底部导航栏的默认选项为ProfileFragment对应的菜单项
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);

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
