package com.example.ec200a_um982_app.main_fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ec200a_um982_app.MainActivity;
import com.example.ec200a_um982_app.R;
import com.example.ec200a_um982_app.SharedViewModel;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private SharedViewModel viewModel1;
    // 蓝牙操作相关变量
    private ListView BtList;  // 显示蓝牙设备列表的ListView
    private Button btn_Scan;  // 扫描蓝牙设备的按钮
    private Button btn_Send;  // 发送数据的按钮
    private Button DisconnectBluetoothButton; // 断开蓝牙连接的按钮
    private Intent intent; // 蓝牙启用的Intent
    private BluetoothAdapter bluetoothAdapter; // 蓝牙适配器
    private List<String> devicesNames; // 存储设备名称的列表
    private ArrayList<BluetoothDevice> readyDevices; // 存储找到的蓝牙设备
    private ArrayAdapter<String> btNames; // 蓝牙设备名称的适配器

    // BLE连接相关变量
    public static BluetoothGatt bluetoothGatt; // 蓝牙GATT
    public static BluetoothGattCharacteristic characteristic; // 蓝牙特征

    private static final int LOCATIONPERMISSION_REQUEST_CODE = 1;

    // BLE扫描相关变量
    private BluetoothLeScanner bluetoothLeScanner; // BLE扫描器
    private boolean isScanning = false; // 是否正在扫描
    private Set<String> deviceNamesSet; // 存储设备名称的集合，防止重复

    // 定时器相关变量
    private Handler timerHandler; // 定时器Handler
    private Runnable timerRunnable; // 定时器任务

    private int ScanTimeCount = 0; // 扫描时间计数

    // 创建Fragment的新实例
    public static BluetoothFragment newInstance(String param1, String param2) {
        BluetoothFragment fragment = new BluetoothFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BluetoothFragment() {
        // 必须的空构造函数
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel1 = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);


        // 为这个Fragment加载视图
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);

        BtList = view.findViewById(R.id.BtList);
        btn_Scan = view.findViewById(R.id.btn_Scan);
        btn_Send = view.findViewById(R.id.btn_Send);
        DisconnectBluetoothButton = view.findViewById(R.id.DisconnectBluetoothButton);

        // 初始化蓝牙适配器和扫描器
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        deviceNamesSet = new HashSet<>();

        if (!bluetoothAdapter.isEnabled()) {
            intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
        }

        readyDevices = new ArrayList<>();
        devicesNames = new ArrayList<>();
        btNames = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, devicesNames);
        BtList.setAdapter(btNames);

        // 设置扫描按钮的点击事件
        btn_Scan.setOnClickListener(v -> {
            if (isScanning) {
                stopScan(); // 如果正在扫描，则停止扫描
            } else {
                startScan(); // 否则开始扫描
            }
        });

        // 设置发送按钮的点击事件
        btn_Send.setOnClickListener(v -> {
            if (MainActivity.getBluetoothConFlag()) {
                characteristic.setValue("niganmaaiyo"); // 设置要发送的值
                bluetoothGatt.writeCharacteristic(characteristic); // 写入特征值
            } else {
                MainActivity.showToast(getActivity(), "请连接蓝牙");
            }
        });

        // 设置断开连接按钮的点击事件
        DisconnectBluetoothButton.setOnClickListener(v -> {
            if (MainActivity.getBluetoothConFlag()) {
                if (bluetoothGatt != null) {
                    bluetoothGatt.disconnect(); // 断开连接
                    bluetoothGatt.close(); // 关闭GATT
                    bluetoothGatt = null;
                    MainActivity.setBluetoothConFlag(false);
                    MainActivity.showToast(getActivity(), "蓝牙已断开");
                }
            } else {
                MainActivity.showToast(getActivity(), "当前没有连接的蓝牙设备");
            }
        });

        // 设置ListView的点击事件，连接选择的设备
        BtList.setOnItemClickListener((parent, view1, position, id) -> {
            if (bluetoothGatt != null) {
                bluetoothGatt.disconnect();
                bluetoothGatt.close();
                bluetoothGatt = null;
            }

            isScanning = false;
            btn_Scan.setText("搜索蓝牙");
            bluetoothLeScanner.stopScan(leScanCallback);

            BluetoothDevice device = readyDevices.get(position);
            bluetoothGatt = device.connectGatt(getActivity(), false, gattCallback);

            MainActivity.showToast(getActivity(), "正在连接 " + device.getName());
        });

        // 初始化并启动定时器
        timerHandler = new Handler(Looper.getMainLooper());
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                // 定时器任务代码
                if (ScanTimeCount > 0) {
                    ScanTimeCount--;
                    if (ScanTimeCount == 0) {
                        isScanning = false;
                        btn_Scan.setText("搜索蓝牙");
                        bluetoothLeScanner.stopScan(leScanCallback);
                    }
                }

                // 计划下一次执行
                timerHandler.postDelayed(this, 1000); // 每秒重复执行一次
            }
        };
        timerHandler.post(timerRunnable); // 启动定时器

        return view;
    }

    @SuppressLint("MissingPermission")
    private void loadPairedDevices() {
        // 加载已配对的设备
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices != null && pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                readyDevices.add(device);
                devicesNames.add(device.getName());
                deviceNamesSet.add(device.getName());
            }
            btNames.notifyDataSetChanged();
        } else {
            MainActivity.showToast(getActivity(), "没有设备已经配对！");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startScan() {
        // 开始扫描BLE设备
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // 请求权限
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    1);
            return;
        }

        devicesNames.clear();
        readyDevices.clear();
        deviceNamesSet.clear();
        btNames.notifyDataSetChanged();

        isScanning = true;
        btn_Scan.setText("停止搜索");
        bluetoothLeScanner.startScan(null, buildScanSettings(), leScanCallback);

        ScanTimeCount = 15; // 设置扫描时间为15秒
    }

    private void stopScan() {
        // 停止扫描BLE设备
        isScanning = false;
        btn_Scan.setText("搜索蓝牙");
        bluetoothLeScanner.stopScan(leScanCallback);
    }

    private ScanSettings buildScanSettings() {
        // 构建扫描设置
        return new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // 设置扫描模式为低延迟
                .build();
    }

    private final ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            // 扫描结果回调
            BluetoothDevice device = result.getDevice();
            if (device != null && device.getName() != null) {
                String deviceName = device.getName();
                if (!deviceNamesSet.contains(deviceName)) {
                    deviceNamesSet.add(deviceName);
                    devicesNames.add(deviceName);
                    readyDevices.add(device);
                    getActivity().runOnUiThread(() -> {
                        btNames.notifyDataSetChanged(); // 更新ListView
                    });
                }
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            // 批量扫描结果回调
            for (ScanResult result : results) {
                onScanResult(ScanSettings.CALLBACK_TYPE_ALL_MATCHES, result);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            // 扫描失败回调
            Log.e("BluetoothScan", "Scan failed with error: " + errorCode);
            MainActivity.showToast(getActivity(), "扫描失败: " + errorCode);
        }
    };

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            // 连接状态变化回调
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                getActivity().runOnUiThread(() -> {
                    MainActivity.showToast(getActivity(), "已连接 " + gatt.getDevice().getName());
                });
                gatt.discoverServices(); // 发现服务
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                getActivity().runOnUiThread(() -> {
                    MainActivity.showToast(getActivity(), "蓝牙已断开");
                });
                MainActivity.setBluetoothConFlag(false);
            }
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            // MTU变化回调
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // 获取服务
                BluetoothGattService service = gatt.getService(UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb"));
                if (service != null) {
                    // 获取特征
                    characteristic = service.getCharacteristic(UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb"));
                    if (characteristic != null) {
                        // 启用通知
                        gatt.setCharacteristicNotification(characteristic, true);
                        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString("0000fff3-0000-1000-8000-00805f9b34fb"));
                        if (descriptor != null) {
                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            gatt.writeDescriptor(descriptor); // 写入描述符
                        }

                        // 设置连接状态为已连接
                        MainActivity.setBluetoothConFlag(true);

                        // 播放连接声音
                        MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.bluetooth_connected);
                        mediaPlayer.start();
                    }
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            // 服务发现回调
            if (status == BluetoothGatt.GATT_SUCCESS) {
                gatt.requestMtu(512); // 请求MTU为512
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            // 特征写入回调
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // 处理成功写入
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            // 特征值变化回调
            byte[] data = characteristic.getValue();
            String receivedData = new String(data, StandardCharsets.UTF_8);

            // 解析数据，处理 \r\n
            // 如果收到的数据中包含 \r\n，则根据 \r\n 分割字符串
            String[] parts = receivedData.split("\r\n");

            // 查找第一个有效的数据片段
            for (String part : parts) {
                if (!part.trim().isEmpty()) {
                    // 只处理第一个有效的数据片段
                    getActivity().runOnUiThread(() -> {
                        viewModel1.setDataGroup1(part);
                    });
                    break; // 处理完第一个数据后，退出循环
                }
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timerHandler != null && timerRunnable != null) {
            timerHandler.removeCallbacks(timerRunnable); // 移除定时器任务
        }
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect(); // 断开蓝牙连接
            bluetoothGatt.close(); // 关闭GATT
            bluetoothGatt = null;
        }
        if (isScanning) {
            stopScan(); // 停止扫描
        }
    }
}
