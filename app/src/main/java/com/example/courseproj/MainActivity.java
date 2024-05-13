package com.example.courseproj;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.courseproj.DB.MySQLConnectionUtil;
import com.example.courseproj.DB.MD5;
import com.example.courseproj.DB.UserDatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 主界面
 * 1. 用户输入账号和密码
 * 2. 用户选择身份
 * 3. 用户点击登录按钮
 * 4. 根据用户输入的账号、密码和身份，查询数据库，判断用户是否存在
 * 5. 如果用户存在，提示用户登录成功
 * 6. 如果用户不存在，提示用户用户名或密码错误
 * 7. 如果数据库连接失败，提示用户连接数据库失败
 */
public class MainActivity extends AppCompatActivity {

    ConstraintLayout layout;
    AnimationDrawable anim;
    EditText loginAccount,loginPassword;
    Button loginButton;
    Spinner identitySpinner;
    int identity;  // 0:学生 1:老师 2:管理员

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
        loginAccount = findViewById(R.id.LoginAccount);
        loginPassword = findViewById(R.id.LoginPassword);
        loginButton = findViewById(R.id.LoginButton);
        identitySpinner = findViewById(R.id.identitySpinner);


        // 设置渐变动画
        anim = (AnimationDrawable) layout.getBackground();
        anim.setEnterFadeDuration(2000); // 设置渐入效果持续时间
        anim.setExitFadeDuration(4000); // 设置渐出效果持续时间

//        loginPassword.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) { // 监听触摸事件
//                final int DRAWABLE_RIGHT = 2; // 右侧图标的索引
//
//                if(event.getAction() == MotionEvent.ACTION_UP) { // 当手指抬起时
//                    if(event.getRawX() >= (loginPassword.getRight() - loginPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) { // 如果点击的位置在右侧图标的范围内
//                        int visible_vertical_size = Math.round(getResources().getDimension(R.dimen.visible_vertical_size)); // 获取大小
//                        int visible_horizontal_size = Math.round(getResources().getDimension(R.dimen.visible_horizontal_size));
//                        int invisible_horizontal_size = Math.round(getResources().getDimension(R.dimen.invisible_horizontal_size));
//                        if (loginPassword.getTransformationMethod() == PasswordTransformationMethod.getInstance()) { // 如果密码框是隐藏状态
//                            Drawable visible = getResources().getDrawable(R.drawable.visible);
//                            visible = resizeDrawable(visible, visible_vertical_size, visible_horizontal_size); // 调整图标大小
//                            visible.setBounds(0, 0, visible_vertical_size, visible_horizontal_size); // 设置Drawable的边界
//                            loginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                            loginPassword.setCompoundDrawables(null, null, visible, null); // 更改图标为visible.png
//                        } else { // 如果密码框是显示状态
//                            Drawable invisible = getResources().getDrawable(R.drawable.invisible);
//                            invisible = resizeDrawable(invisible, visible_vertical_size, visible_horizontal_size); // 调整图标大小
//                            invisible.setBounds(0, 0, visible_vertical_size, invisible_horizontal_size); // 设置Drawable的边界
//                            loginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                            loginPassword.setCompoundDrawables(null, null, invisible, null); // 更改图标为invisible.png
//                        }
//                        v.performClick(); // 执行点击事件
//                        return true; // 返回true表示事件已被处理
//                    }
//                }
//                return false; // 返回false表示事件未被处理
//            }
//        });

        // 点击密码框右侧的图标，切换密码的可见性
        loginPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int DRAWABLE_RIGHT = 2; // 右侧图标的索引

                if(loginPassword.getCompoundDrawables()[DRAWABLE_RIGHT] != null) {
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
                }
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
//                        Toast.makeText(MainActivity.this, ii, Toast.LENGTH_SHORT).show();
//                    }
//                });
//
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 未选择身份
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "未选择身份", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // 点击登录按钮，处理登录
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = loginAccount.getText().toString();
                String password = loginPassword.getText().toString();
                handleLogin(user_id, password,identity);
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

    // 处理登录
    public void handleLogin(String user_id, String password, int identity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                try {
                    connection = MySQLConnectionUtil.getConnection();
                    if (connection == null) {
                        throw new SQLException("Cannot connect to the database");
                    }
                    String sql = "";
                    String md5_password = MD5.md5(password);
                    if (identity==1) {
                        // 老师
                        sql = "SELECT * FROM teachers WHERE teacher_id = ? AND teacher_password = ?";
                    } else if (identity==2){
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
                        // TODO 用户名和密码匹配，进行下一步
                        //  TEST 提示用户登录成功
                        String login_success_test =  identity + "\n" + user_id + "\n" + password + "\n" + md5_password;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, login_success_test, Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        // 用户名和密码不匹配，提示用户输入的数据错误，且清空密码框
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loginPassword.setText("");
                                Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Failed to connect to the database", Toast.LENGTH_SHORT).show();
                } finally {
                    // 关闭资源
                    MySQLConnectionUtil.MySQL_DB_close(connection, preparedStatement, resultSet);
                }
            }
        }).start();
    }

    // 处理登录成功
    // 将用户数据插入到SQLite数据库中
    // user_id: 用户ID
    // identity: 用户身份
    public void handleLoginSuccess(String user_id, int identity) {
        UserDatabaseHelper dbHelper = new UserDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String tableName;
        switch (identity) {
            case 0:
                tableName = "admin_data";
                break;
            case 1:
                tableName = "teacher_data";
                break;
            case 2:
                tableName = "student_data";
                break;
            default:
                throw new IllegalArgumentException("Invalid identity: " + identity);
        }

        // 检查用户数据是否已经存在于SQLite数据库中
        // 如果存在，更新数据
        // 如果不存在，从MySQL数据库中获取数据并插入到SQLite数据库中
        Cursor cursor = db.query(tableName, new String[]{"user_id"}, "user_id = ?", new String[]{user_id}, null, null, null);
        if (cursor.moveToFirst()) {
            // 用户数据已经存在，更新数据...
            // TODO: 更新用户数据

        } else {
            // 用户数据不存在，从MySQL数据库中获取数据并插入到SQLite数据库中
            // 新建线程，从MySQL数据库中获取用户数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Connection connection = null;
                    PreparedStatement preparedStatement = null;
                    ResultSet resultSet = null;
                    try {
                        connection = MySQLConnectionUtil.getConnection();
                        if (connection == null) {
                            throw new SQLException("Cannot connect to the database");
                        }
                        String sql = "SELECT * FROM " + tableName + " WHERE user_id = ?";
                        preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, user_id);
                        resultSet = preparedStatement.executeQuery();
                        if (resultSet.next()) {
                            // Insert user data into SQLite database
                            ContentValues values = new ContentValues();
                            values.put("user_id", user_id);
                            values.put("name", resultSet.getString("name"));
                            values.put("gender", resultSet.getInt("gender"));
                            values.put("birthday", resultSet.getString("birthday"));
                            values.put("login_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                            if (identity == 1) {
                                values.put("start_time", resultSet.getString("start_time"));
                            } else if (identity == 2) {
                                values.put("start_year", resultSet.getInt("start_year"));
                                values.put("years", resultSet.getInt("years"));
                            }
                            db.insert(tableName, null, values);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Failed to connect to the database", Toast.LENGTH_SHORT).show();
                    } finally {
                        // 关闭资源
                        MySQLConnectionUtil.MySQL_DB_close(connection, preparedStatement, resultSet);
                    }
                }
            }).start();
        }
        cursor.close();
    }


}