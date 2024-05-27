package com.example.courseproj.Common;

import java.sql.*;

/**
 * MySQL数据库连接工具类
 */
public class DB_MySQLConnectionUtil {
    private static final String DRIVER = "com.mysql.jdbc.Driver";   // 驱动
    protected static final String IP = "192.168.0.103";  // 505路由器下Docker服务器IP
//    protected static final String IP = "192.168.1.177";  // 525路由器下Docker服务器IP
//    protected static final String IP = "192.168.1.54";  // 525路由器下Docker服务器IP
    protected static final String PORT = "55005";  // 端口号
    private static final String DB_NAME = "AndroidDB";  // MySQL数据库名称
    private static final String USERNAME = "root";  //MySQL用户
    private static final String PASSWORD = "12345678";  //MySQL密码
    private static final String DB_URL = "jdbc:mysql://" + IP + ":" + PORT + "/" + DB_NAME +
            "?useSSL=false&"  + // 不使用SSL
            "allowPublicKeyRetrieval=true&" +  // 允许公钥检索
            "serverTimezone=UTC&" + // 服务器时区
            "useUnicode=true&" + // 使用Unicode字符集
            "characterEncoding=utf-8"; // 字符编码

    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Failed to load the database driver", e);
        }
        return connection;
    }

    public static void MySQL_DB_close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}