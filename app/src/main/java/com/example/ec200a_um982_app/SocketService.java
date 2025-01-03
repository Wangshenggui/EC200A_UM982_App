package com.example.ec200a_um982_app;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.ec200a_um982_app.main_fragment.NtripFragment;
import com.example.ec200a_um982_app.main_fragment.bluetooth_topfragment.Bluetooth_top1Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class SocketService extends Service {

    private static final String TAG = "SocketService";

    public static String CORSSSGString = "";

    String originalInput;
    String CORSAccountText;
    String CORSPasswordText;
    String MountPointText;

    private final IBinder binder = new LocalBinder();
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private Thread socketThread;

    private final ExecutorService executorService = Executors.newCachedThreadPool();  // 线程池管理
    private final ReentrantLock sendlock = new ReentrantLock();  // 发送锁
    private final ReentrantLock readlock = new ReentrantLock();  // 读取锁

    private Toast currentToast;
    private Handler mainHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mainHandler = new Handler(Looper.getMainLooper());
        startForegroundService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String corsAccount = intent.getStringExtra("CORSAccount");
        String corsPassword = intent.getStringExtra("CORSPassword");
        String mountPoint = intent.getStringExtra("MountPoint");
        String SocketClose = intent.getStringExtra("SocketClose");
        // 使用 corsAccount
        // 检查是否获取到数据
        if (corsAccount != null) {
            // 使用 corsAccount 进行你需要的操作
            CORSAccountText = corsAccount;
        }
        if (corsPassword != null) {
            // 使用 corsPassword 进行你需要的操作
            CORSPasswordText = corsPassword;
        }
        if (mountPoint != null) {
            // 使用 MountPoint 进行你需要的操作
            MountPointText = mountPoint;
        }
        if (SocketClose != null) {
            if (socket != null && !socket.isClosed()) {
                try {
                    socket.close();  // 关闭Socket连接
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.close_the_cors_connection);
                // mediaPlayer.start();
                showToast("连接已关闭");
            }
        }


        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        SocketService getService() {
            return SocketService.this;
        }
    }

    @SuppressLint("ForegroundServiceType")
    private void startForegroundService() {
        // 为 Android 8.0及以上版本创建通知频道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "socket_channel",       // 通道ID
                    "Socket Service",       // 通道名称
                    NotificationManager.IMPORTANCE_LOW // 通知重要性
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        // 创建通知对象
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new NotificationCompat.Builder(this, "socket_channel")
                    .setContentTitle("骞羽RTK")
                    .setContentText("骞羽RTK在后台运行")
                    .setSmallIcon(R.mipmap.rtk_icon)
                    .build();
        }

        // 确保通知对象非null
        if (notification != null) {
            startForeground(1, notification);
        } else {
            // 如果创建通知失败，可以在这里处理异常情况
            Log.e("StartForegroundService", "Notification creation failed!");
        }
    }


    public void connectToServer(String ipAddress, int port) {
        socketThread = new Thread(() -> {
            while (true) {
                try {
                    socket = new Socket(ipAddress, port);
                    outputStream = socket.getOutputStream();
                    inputStream = socket.getInputStream();

                    showToast("已连接服务器");

                    if(MainActivity.getBluetoothConFlag()){
                        originalInput = CORSAccountText + ":" + CORSPasswordText;
                        // 编码字符串
                        String encodedString = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
                        }

                        String message = "GET /" +
                                MountPointText +
                                " HTTP/1.0\r\nUser-Agent: NTRIP GNSSInternetRadio/1.4.10\r\nAccept: */*\r\nConnection: close\r\nAuthorization: Basic " +
                                encodedString +
                                "\r\n\r\n";

                        if (MainActivity.isBound) {
                            MainActivity.socketService.sendMessage(message);
                        }
                    }else{
                        MainActivity.showToast(this,"蓝牙未连接");
                    }

                    readFromSocket();
                    break;  // 连接成功后退出循环

                } catch (IOException e) {
                    Log.e(TAG, "连接失败: " + e.getMessage());
                    showToast("连接失败，正在重试...");

                    // 等待5秒后重新尝试连接
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }
        });
        socketThread.start();
    }

    public void sendMessage(String message) {
        sendlock.lock();  // 获取发送锁
        try {
            if (socket != null && outputStream != null) {
                executorService.submit(() -> {
                    try {
                        outputStream.write(message.getBytes(StandardCharsets.UTF_8));
                        outputStream.flush();
                    } catch (IOException e) {
                        Log.e(TAG, "消息发送失败: " + e.getMessage());
                        showToast("消息发送失败");
                    }
                });
            } else {
                showToast("套接字未连接");
            }
        } finally {
            sendlock.unlock();  // 释放发送锁
        }
    }

    private void readFromSocket() {
        readlock.lock();  // 获取读取锁
        try {
            if (inputStream != null) {
                try {
                    byte[] buffer = new byte[2048];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        byte[] rawMessage = Arrays.copyOf(buffer, bytesRead);

                        // 将字节数组转换为字符串
                        String receivedMessage = new String(rawMessage, StandardCharsets.UTF_8);

                        NtripFragment.RTCMString = rawMessage;

                        if(CORSSSGString.length() > 50){
                            if (validateNmeaChecksum(CORSSSGString)) {
                                MainActivity.socketService.sendMessage(CORSSSGString + "\r\n");
                            }
                        }

                        if (NtripFragment.RTCMString != null) {
                            int groupSize = 220;
                            int length = NtripFragment.RTCMString.length;
                            int numGroups = (length + groupSize - 1) / groupSize; // 计算分组数量

                            byte[][] groupedRTCM = new byte[numGroups][]; // 创建二维数组

                            for (int i = 0; i < numGroups; i++) {
                                int start = i * groupSize;
                                int size = Math.min(groupSize, length - start); // 计算当前组的大小

                                // 确保结束索引不超出数组的长度
                                groupedRTCM[i] = Arrays.copyOfRange(NtripFragment.RTCMString, start, start + size);
                            }

                            // 可以在这里打印分组信息以验证
                            for (int i = 0; i < numGroups; i++) {
                                System.out.println("Group " + i + " size: " + groupedRTCM[i].length);
                            }

//                            if (validateNmeaChecksum(CORSSSGString)) {
                                for (int i = 0; i < numGroups; i++) {
                                    Bluetooth_top1Fragment.characteristic.setValue(groupedRTCM[i]); // 设置要发送的值
                                    Bluetooth_top1Fragment.bluetoothGatt.writeCharacteristic(Bluetooth_top1Fragment.characteristic); // 写入特征值

                                    // 可选：在每次发送后等待一段时间，以确保数据能顺利传输
                                    try {
                                        Thread.sleep(5); // 例如，等待 100 毫秒
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
//                                }
                            }
                        }

                        // 定义预期的消息字符串
                        String expectedMessageOK = "ICY 200 OK\r\n";
                        String expectedMessageERROR = "ERROR - Bad Password\r\n";

                        // 比较接收到的消息与预期消息
                        if (receivedMessage.equals(expectedMessageOK)) {
                            // MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.cors_request_succeeded_rocedure);
                            // mediaPlayer.start();

                            if(CORSSSGString.length() > 50){
                                if (validateNmeaChecksum(CORSSSGString)) {
                                    MainActivity.socketService.sendMessage(CORSSSGString + "\r\n");
                                }
                            }
                        } else if (receivedMessage.equals(expectedMessageERROR)){
                            // MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.account_or_password_incorrec);
                            // mediaPlayer.start();
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "读取失败: " + e.getMessage());
                    showToast("读取失败");
                }
            }
        } finally {
            readlock.unlock();  // 释放读取锁
        }
    }

    public static boolean validateNmeaChecksum(String nmea) {
        // 分割 NMEA 语句，获取校验和部分
        String[] parts = nmea.split("\\*");
        if (parts.length != 2) return false; // 格式不正确，返回 false

        String sentence = parts[0].substring(1); // 去掉开头的 '$'
        String checksum = parts[1]; // 提取校验和

        int calculatedChecksum = 0; // 初始化计算的校验和
        // 计算校验和
        for (char c : sentence.toCharArray()) {
            calculatedChecksum ^= c; // 使用异或操作计算校验和
        }

        // 比较计算的校验和与提供的校验和
        return String.format("%02X", calculatedChecksum).equalsIgnoreCase(checksum);
    }


    private void showToast(final String message) {
        mainHandler.post(() -> {
            if (currentToast != null) {
                currentToast.cancel();  // 取消前一个Toast
            }
            currentToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            currentToast.show();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (socketThread != null && socketThread.isAlive()) {
            socketThread.interrupt();  // 停止Socket线程
        }

        if (executorService != null) {
            executorService.shutdown();  // 关闭线程池
        }

        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();  // 关闭Socket连接
                showToast("连接已关闭");
            }
        } catch (IOException e) {
            Log.e(TAG, "关闭套接字失败: " + e.getMessage());
            showToast("关闭套接字失败");
        }
    }
}
