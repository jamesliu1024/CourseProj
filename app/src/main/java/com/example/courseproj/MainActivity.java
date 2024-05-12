package com.example.courseproj;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;


public class MainActivity extends AppCompatActivity {

    ConstraintLayout layout;
    AnimationDrawable anim;
    EditText loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 将状态栏的背景颜色设置为透明
        // 将窗口的布局参数设置为 FLAG_LAYOUT_NO_LIMITS，这将使状态栏变为透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        layout = findViewById(R.id.welcomeLayout);
        loginPassword = findViewById(R.id.LoginPassword);

        // 设置渐变动画
        anim = (AnimationDrawable) layout.getBackground();
        anim.setEnterFadeDuration(2000); // 设置渐入效果持续时间
        anim.setExitFadeDuration(4000); // 设置渐出效果持续时间

        // 点击密码框右侧的图标，切换密码的可见性
        loginPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) { // 监听触摸事件
                final int DRAWABLE_RIGHT = 2; // 右侧图标的索引

                if(event.getAction() == MotionEvent.ACTION_UP) { // 当手指抬起时
                    if(event.getRawX() >= (loginPassword.getRight() - loginPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) { // 如果点击的位置在右侧图标的范围内
                        int visible_vertical_size = Math.round(getResources().getDimension(R.dimen.visible_vertical_size)); // 获取大小
                        int visible_horizontal_size = Math.round(getResources().getDimension(R.dimen.visible_horizontal_size));
                        int invisible_horizontal_size = Math.round(getResources().getDimension(R.dimen.invisible_horizontal_size));
                        if (loginPassword.getTransformationMethod() == PasswordTransformationMethod.getInstance()) { // 如果密码框是隐藏状态
                            Drawable visible = getResources().getDrawable(R.drawable.visible);
                            visible = resizeDrawable(visible, visible_vertical_size, visible_horizontal_size); // 调整图标大小
                            visible.setBounds(0, 0, visible_vertical_size, visible_horizontal_size); // 设置Drawable的边界
                            loginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            loginPassword.setCompoundDrawables(null, null, visible, null); // 更改图标为visible.png
                        } else { // 如果密码框是显示状态
                            Drawable invisible = getResources().getDrawable(R.drawable.invisible);
                            invisible = resizeDrawable(invisible, visible_vertical_size, visible_horizontal_size); // 调整图标大小
                            invisible.setBounds(0, 0, visible_vertical_size, invisible_horizontal_size); // 设置Drawable的边界
                            loginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            loginPassword.setCompoundDrawables(null, null, invisible, null); // 更改图标为invisible.png
                        }
                        return true; // 返回true表示事件已被处理
                    }
                }
                return false; // 返回false表示事件未被处理
            }
        });
    }

    // 开始播放动画：在onResume方法中开始播放渐变动画
    @Override
    protected void onResume() {
        super.onResume();
        if (anim != null && !anim.isRunning())
            anim.start();
    }

    // 停止播放动画：在onPause方法中停止播放渐变动画
    @Override
    protected void onPause() {
        super.onPause();
        if (anim != null && anim.isRunning())
            anim.stop();
    }

    // 设置图片大小
    private Drawable resizeDrawable(Drawable image, int width, int height) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, width, height, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }
}