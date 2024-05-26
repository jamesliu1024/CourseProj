package com.example.courseproj.Admin;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.courseproj.R;

import java.util.Calendar;

public class AddCourseActivity extends AppCompatActivity {
    LinearLayout layout;
    AnimationDrawable anim;
    SharedPreferences sharedPreferences;
    private TextView birthdayTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);
        initView();

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
