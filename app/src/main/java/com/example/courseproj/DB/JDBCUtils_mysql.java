package com.example.courseproj.DB;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCUtils_mysql {
    private static final String TAG = "JDBCUtils_mysql";

    private static String driver = "com.mysql.jdbc.Driver";// MySql驱动

    private static String dbName = "AndroidDB";// 数据库名称

    private static String port = "55005"; // 端口号

    private static String user = "root";// 用户名

    private static String password = "123456";// 密码

    public static Connection mysql_getConn(){

        Connection connection = null;
        try{
            Class.forName(driver);// 动态加载类
            String ip = "localhost";// 写成本机地址，不能写成localhost，同时手机和电脑连接的网络必须是同一个

            // 尝试建立到给定数据库URL的连接
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + ip + ":" + port + "/" + dbName,
                    user, password);

        }catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }
}
