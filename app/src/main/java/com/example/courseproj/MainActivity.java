package com.example.courseproj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.courseproj.Admin.AdminActivity;
import com.example.courseproj.Student.StudentActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 主界面
 * 在MainActivity中检查sharedperfrences是否已存在数据
 * 在这个界面检查用户是否已经登陆
 * 登陆则跳转到相应的界面
 * 没有登陆则跳转到登陆界面
 */
public class MainActivity extends AppCompatActivity {

    LinearLayout layout;
    AnimationDrawable anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();


        // 创建一个SharedPreferences对象
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);

        // 检查SharedPreferences中是否存在特定的键值对
        if (sharedPreferences.contains("user_id") && sharedPreferences.contains("identity") && sharedPreferences.contains("login_time")) {
            // 获取用户的登录信息
            String user_id = sharedPreferences.getString("user_id", "");  // 获取用户的id，如果没有则返回空字符串
            int identity = sharedPreferences.getInt("identity", -1);  // 获取用户的身份，如果没有则返回-1
            // 获取用户的登录时间，转换成Date类型
            Date login_time = null;
            try {
                login_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sharedPreferences.getString("login_time", ""));
            } catch (ParseException e) {
                Toast.makeText(MainActivity.this, "时间格式错误", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            // 如果用户登陆时间超过1天，跳转到LoginActivity
            if (login_time != null) {
                long time = new Date().getTime() - login_time.getTime();
                if (time > 1 * 24 * 60 * 60 * 1000) {
                    // 提示用户登录已过期，等待5秒后跳转到LoginActivity
                    Toast.makeText(MainActivity.this, "登录已过期，请重新登录", Toast.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // 结束当前Activity
                }
            }

            // 根据用户的身份跳转到相应的界面
            if (identity == 0) {
                // 跳转到学生界面
                Intent intent = new Intent(MainActivity.this, StudentActivity.class);
                startActivity(intent);
                finish();
//                Toast.makeText(MainActivity.this, "学生界面", Toast.LENGTH_SHORT).show();
            } else if (identity == 1) {
                // TODO 跳转到老师界面
//                Intent intent = new Intent(MainActivity.this, TeacherActivity.class);
//                startActivity(intent);
//                finish();
                Toast.makeText(MainActivity.this, "老师界面", Toast.LENGTH_SHORT).show();
            } else if (identity == 2) {
                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(intent);
                finish();
//                Toast.makeText(MainActivity.this, "管理员界面", Toast.LENGTH_SHORT).show();
            }
        } else {

            // 如果没有相关数据，跳转到LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // 结束当前Activity
        }

    }

//    // 开始播放动画：在onResume方法中开始播放渐变动画
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (anim != null && !anim.isRunning())
//            anim.start();
//    }
//
//    // 停止播放动画：在onPause方法中停止播放渐变动画
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (anim != null && anim.isRunning())
//            anim.stop();
//    }

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