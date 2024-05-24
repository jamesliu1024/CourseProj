package com.example.courseproj.Common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * 检查网络状态
 * 1: 网络正常
 * 0: 网络异常
 * -1: 无网络权限
 */
public class NetworkUtil {
    private static final String SERVER_IP = DB_MySQLConnectionUtil.IP;
    private static final int SERVER_PORT = Integer.parseInt(DB_MySQLConnectionUtil.PORT);  // MySQL default port

    // 检查网络状态
    // 返回值为1，说明网络正常；返回0，说明网络异常；返回-1，说明无网络权限
    public static int checkNetworkStatus(Context context) {
        if (!checkNetworkPermission(context)) { // 检查网络权限
            return -1;
        }

        if (!checkNetworkConnection(context)) { // 检查网络连接
            return 0;
        }

        final boolean[] canConnectToServer = new boolean[1];  // 用于存储服务器连接状态
        Thread thread = new Thread(new Runnable() { // 创建一个线程来检查服务器连接状态
            @Override
            public void run() {
                canConnectToServer[0] = checkServerConnection();  // 检查服务器连接
            }
        });
        thread.start();  // 启动线程
        try {
            thread.join(); // 等待线程结束
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!canConnectToServer[0]) {  // 如果服务器连接失败
            return 0;
        }

        return 1;  // 网络正常
    }


    // 返回值为PackageManager.PERMISSION_GRANTED，说明应用有INTERNET权限，返回true；否则返回false
    private static boolean checkNetworkPermission(Context context) {
        int permissionState = context.getPackageManager().checkPermission(
                android.Manifest.permission.INTERNET,
                context.getPackageName());

        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    // 通过ConnectivityManager获取网络连接信息，如果网络连接信息不为空且网络连接状态为已连接或正在连接，返回true；否则返回false
    private static boolean checkNetworkConnection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    // 通过创建一个Socket连接到服务器的IP和端口，来检查服务器是否连接
    // 如果连接成功，返回true；否则返回false
    private static boolean checkServerConnection() {
        try (Socket socket = new Socket()) {
            SocketAddress socketAddress = new InetSocketAddress(SERVER_IP, SERVER_PORT);
            socket.connect(socketAddress, 2000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}