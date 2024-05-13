package com.example.courseproj.DB;

import java.sql.*;

/**
 * MySQL数据库连接工具类
 */
public class MySQLConnectionUtil {
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String IP = "192.168.0.103";
    private static final String PORT = "55005";
    private static final String DB_NAME = "AndroidDB";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";
    private static final String DB_URL = "jdbc:mysql://" + IP + ":" + PORT + "/" + DB_NAME + "?useSSL=false";

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