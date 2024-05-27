package com.example.courseproj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.courseproj.Admin.AdminActivity;
import com.example.courseproj.Common.*;
import com.example.courseproj.Student.StudentActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * 登录界面
 * 在LoginActivity中处理用户的登录
 * 用户输入账号和密码，选择身份后，点击登录按钮
 * 通过数据库查询，判断用户输入的账号和密码是否正确
 * 如果用户登录成功，则将用户的登录信息存储到SharedPreferences中
 * 跳转到相应的界面
 */
public class LoginActivity extends AppCompatActivity {
    LinearLayout layout;
    AnimationDrawable anim;
    EditText loginAccount, loginPassword;
    Button loginButton;
    Spinner identitySpinner;
    int identity;  // 0:学生 1:老师 2:管理员

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView();

        layout = findViewById(R.id.bgLayout);
        loginAccount = findViewById(R.id.LoginAccount);
        loginPassword = findViewById(R.id.LoginPassword);
        loginButton = findViewById(R.id.LoginButton);
        identitySpinner = findViewById(R.id.identitySpinner);


        // 点击密码框右侧的图标，切换密码的可见性
        loginPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (loginPassword.getRight() - loginPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
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
                        return true;
                    }
                }
                return false;
            }
        });

        // 获取identitySpinner的值，确认用户的身份是学生、老师还是管理员
        identitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                identity = parent.getSelectedItemPosition();

                // TEST 显示用户选择的身份
//                String ii = String.valueOf(identity);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(LoginActivity.this, ii, Toast.LENGTH_SHORT).show();
//                    }
//                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 未选择身份
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "未选择身份", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // 点击登录按钮，处理登录
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 检查网络状态
                int networkStatus = NetworkUtil.checkNetworkStatus(LoginActivity.this);
                if (networkStatus == -1) { // 无网络权限
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "无网络权限", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                } else if (networkStatus == 0) { // 网络异常
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "无法连接到服务器，请检查网络是否通畅", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                String user_id = loginAccount.getText().toString();
                String password = loginPassword.getText().toString();
                handleLogin(user_id, password, identity);
            }
        });
    }

    // 设置图片大小
    private Drawable resizeDrawable(Drawable image, int width, int height) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, width, height, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }

    /**
     * 处理登录
     * 通过数据库查询，判断用户输入的账号和密码是否正确
     * 如果用户登录成功，则将用户的登录信息存储到SharedPreferences中
     * 跳转到相应的界面
     *
     * @param user_id  用户输入的账号
     * @param password 用户输入的密码
     * @param identity 0:学生 1:老师 2:管理员
     */
    public void handleLogin(String user_id, String password, int identity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                try {
                    connection = DB_MySQLConnectionUtil.getConnection();
                    if (connection == null) {
                        throw new SQLException("无法连接到数据库");
                    }
                    String sql = "";
                    String md5_password = MD5.md5(password);
                    if (identity == 1) {
                        // 老师
                        sql = "SELECT * FROM teachers WHERE teacher_id = ? AND teacher_password = ?";
                    } else if (identity == 2) {
                        // 管理员
                        sql = "SELECT * FROM admins WHERE admin_id = ? AND admin_password = ?";
                    } else {
                        // 学生
                        sql = "SELECT * FROM students WHERE student_id = ? AND student_password = ?";
                    }
                    // TEST 显示用户输入的信息
//                    System.out.println(identity);
//                    System.out.println(sql);

                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, user_id);
                    preparedStatement.setString(2, md5_password);
                    resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {

                        String user_name;
                        int gender;
                        String birthday;
                        String start_time;
                        int start_year;
                        int years;

                        if (identity == 1) { // 老师
                            user_name = resultSet.getString("teacher_name");
                            gender = resultSet.getInt("gender");
                            birthday = resultSet.getString("birthday");
                            start_time = resultSet.getString("start_time");

                            start_year = 0;
                            years = 0;
                        } else if (identity == 2) { // 管理员
                            user_name = resultSet.getString("admin_name");
                            gender = resultSet.getInt("gender");
                            birthday = resultSet.getString("birthday");
                            start_time = resultSet.getString("start_time");

                            start_year = 0;
                            years = 0;
                        } else { // 学生
                            user_name = resultSet.getString("student_name");
                            gender = resultSet.getInt("gender");
                            birthday = resultSet.getString("birthday");
                            start_year = resultSet.getInt("start_year");
                            years = resultSet.getInt("years");

                            start_time = "";
                        }

                        handleLoginSuccess(user_id, identity, user_name, gender, birthday, start_time, start_year, years);
                        if (identity == 0) {
                            Intent intent = new Intent(LoginActivity.this, StudentActivity.class);
                            startActivity(intent);
                            finish();
//                                    Toast.makeText(LoginActivity.this, "学生界面", Toast.LENGTH_SHORT).show();
                        } else if (identity == 1) {
                            // TODO 跳转到老师界面
                            // Intent intent = new Intent(LoginActivity.this, TeacherActivity.class);
                            // startActivity(intent);
//                                    finish();
//                            Toast.makeText(LoginActivity.this, "老师界面", Toast.LENGTH_SHORT).show();
                            Log.i("LoginActivity", "老师界面");
                        } else if (identity == 2) {
                            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                            startActivity(intent);
                            finish();
//                            Toast.makeText(LoginActivity.this, "管理员界面", Toast.LENGTH_SHORT).show();
//                            Log.i("LoginActivity", "管理员界面");
                        }

                    } else {
                        // 用户名和密码不匹配，提示用户输入的数据错误，且清空密码框
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loginPassword.setText("");
                                Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "无法连接到数据库", Toast.LENGTH_SHORT).show();
                        }
                    });
                } finally {
                    // 关闭资源
                    DB_MySQLConnectionUtil.MySQL_DB_close(connection, preparedStatement, resultSet);
                }
            }
        }).start();
    }

    /**
     * 处理登录成功
     * 将用户的登录信息存储到SharedPreferences中
     * 用于在用户登录成功后，保存用户的登录信息
     * 保存用户的user_id和identity
     */
    public void handleLoginSuccess(String user_id, int identity, String user_name, int gender, String birthday, String start_time, int start_year, int years) {
        // 创建一个SharedPreferences对象
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);

        String current_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // 使用SharedPreferences.Editor对象来存储用户的登录信息
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_id", user_id);
        editor.putInt("identity", identity);
        editor.putString("user_name", user_name);
        editor.putInt("gender", gender);
        editor.putString("birthday", birthday);
        editor.putString("start_time", start_time);
        editor.putInt("start_year", start_year);
        editor.putInt("years", years);
        editor.putString("login_time", current_time);

        // 使用commit()或apply()方法来保存更改
        editor.apply();

        // 创建SQLite数据库
        new Thread(new Runnable() {
            @Override
            public void run() {
                DB_SQLiteDatabaseHelper dbHelper = new DB_SQLiteDatabaseHelper(LoginActivity.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
            }
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
