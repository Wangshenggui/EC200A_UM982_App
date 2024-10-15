package com.example.ec200a_um982_app.main_fragment;

import android.Manifest;
import android.animation.ObjectAnimator;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ec200a_um982_app.MainActivity;
import com.example.ec200a_um982_app.R;
import com.example.ec200a_um982_app.SharedViewModel;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothFragment extends Fragment {

    private String mParam1;
    private String mParam2;

    private SharedViewModel viewModel1;
    private ListView BtList;
    private Button btn_Scan;
    private Intent intent;
    private BluetoothAdapter bluetoothAdapter;
    private List<String> devicesNames;
    private ArrayList<BluetoothDevice> readyDevices;
    private ArrayList<Boolean> ConDisDevices = new ArrayList<>();
    private BluetoothListCustomAdapter btNames;

    public static BluetoothGatt bluetoothGatt;
    public static BluetoothGattCharacteristic characteristic;

    private static final int LOCATIONPERMISSION_REQUEST_CODE = 1;


    private HashMap<Button, ObjectAnimator> animators = new HashMap<>(); // 存储每个按钮的动画

    private BluetoothLeScanner bluetoothLeScanner;
    private boolean isScanning = false;
    private Set<String> deviceNamesSet;

    private Handler timerHandler;
    private Runnable timerRunnable;

    private int ScanTimeCount = 0;

    public static BluetoothFragment newInstance(String param1, String param2) {
        BluetoothFragment fragment = new BluetoothFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BluetoothFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("param1");
            mParam2 = getArguments().getString("param2");
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel1 = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);

        BtList = view.findViewById(R.id.BtList);
        btn_Scan = view.findViewById(R.id.btn_Scan);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        deviceNamesSet = new HashSet<>();

        devicesNames = new ArrayList<>();
        readyDevices = new ArrayList<>();
        btNames = new BluetoothListCustomAdapter(getActivity(), devicesNames,ConDisDevices);
        BtList.setAdapter(btNames);

        if (!bluetoothAdapter.isEnabled()) {
            intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
        }

        btn_Scan.setOnClickListener(v -> {
            if (isScanning) {
                stopScan();
                stopRotating(btn_Scan); // 停止旋转
            } else {
                startScan();
                startRotating(btn_Scan); // 开始旋转
            }
        });

        BtList.setOnItemClickListener((parent, view1, position, id) -> {
            BluetoothDevice device = readyDevices.get(position);

            // 检查选定设备是否已连接
            if (MainActivity.getBluetoothConFlag() && bluetoothGatt != null && bluetoothGatt.getDevice().equals(device)) {
                // 如果设备已连接，则断开连接
                disconnectCurrentDevice();
                return;
            }

            // 如果有设备连接，先断开连接
            if (bluetoothGatt != null) {
                BluetoothDevice previousDevice = bluetoothGatt.getDevice();
                bluetoothGatt.disconnect();
                bluetoothGatt.close();
                bluetoothGatt = null;

                // 更新上一个设备的连接状态
                if (previousDevice != null) {
                    int previousPosition = readyDevices.indexOf(previousDevice);
                    if (previousPosition != -1) {
                        ConDisDevices.set(previousPosition, false);
                        btNames.notifyDataSetChanged(); // 更新适配器
                    }
                }
            }

            // 重置扫描状态
            ScanTimeCount = 0;
            isScanning = false;
            stopRotating(btn_Scan); // 停止旋转
            bluetoothLeScanner.stopScan(leScanCallback);

            // 连接选定设备
            bluetoothGatt = device.connectGatt(getActivity(), false, gattCallback);
            MainActivity.showToast(getActivity(), "正在连接 " + device.getName());
        });


        timerHandler = new Handler(Looper.getMainLooper());
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (ScanTimeCount > 0) {
                    ScanTimeCount--;
                    if (ScanTimeCount == 0) {
                        isScanning = false;
//                        btn_Scan.setText("搜索蓝牙");
                        stopRotating(btn_Scan); // 停止旋转
                        bluetoothLeScanner.stopScan(leScanCallback);
                    }
                }
                timerHandler.postDelayed(this, 1000);
            }
        };
        timerHandler.post(timerRunnable);

        return view;
    }

    private void disconnectCurrentDevice() {
        if (MainActivity.getBluetoothConFlag()) {
            if (bluetoothGatt != null) {
                bluetoothGatt.disconnect();
                bluetoothGatt.close();
                bluetoothGatt = null;
                MainActivity.setBluetoothConFlag(false);
                MainActivity.showToast(getActivity(), "蓝牙已断开");

                // 更新连接状态为未连接
                for (int i = 0; i < readyDevices.size(); i++) {
                    ConDisDevices.set(i, false);
                }
                btNames.notifyDataSetChanged(); // 更新适配器
            }
        }
    }

    private void startRotating(Button button) {
        // 如果动画已经存在，先取消它
        stopRotating(button); // 停止当前动画（如果存在）

        // 创建新的旋转动画
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(button, "rotation", 0f, 360f);
        rotationAnimator.setDuration(1000); // 设置旋转一次的持续时间
        rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE); // 设置为无限循环
        rotationAnimator.start(); // 启动动画

        // 将动画存储在 Map 中
        animators.put(button, rotationAnimator);
    }

    private void stopRotating(Button button) {
        ObjectAnimator rotationAnimator = animators.get(button);
        if (rotationAnimator != null) {
            rotationAnimator.cancel(); // 停止当前动画

            // 先旋转到 0 度
            ObjectAnimator resetAnimator = ObjectAnimator.ofFloat(button, "rotation", button.getRotation(), 0f);
            resetAnimator.setDuration(300); // 设置旋转到 0 度的持续时间
            resetAnimator.start(); // 启动旋转到 0 度的动画

            animators.remove(button); // 从 Map 中移除
        }
    }

    @SuppressLint("MissingPermission")
    private void loadPairedDevices() {
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
        devicesNames.clear();
        readyDevices.clear();
        deviceNamesSet.clear();
        ConDisDevices.clear(); // 清空连接状态列表
        btNames.notifyDataSetChanged();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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
//        btn_Scan.setText("停止搜索");
        bluetoothLeScanner.startScan(null, buildScanSettings(), leScanCallback);

        ScanTimeCount = 15;
    }

    private void stopScan() {
        isScanning = false;
//        btn_Scan.setText("搜索蓝牙");
        bluetoothLeScanner.stopScan(leScanCallback);
    }

    private ScanSettings buildScanSettings() {
        return new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();
    }

    private final ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            if (device != null && device.getName() != null) {
                String deviceName = device.getName();
                int rssi = result.getRssi(); // 获取信号强度

                // 设备类型判断
                String deviceType;
                switch (device.getType()) {
                    case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                        deviceType = "Classic";
                        break;
                    case BluetoothDevice.DEVICE_TYPE_LE:
                        deviceType = "BLE";
                        break;
                    case BluetoothDevice.DEVICE_TYPE_DUAL:
                        deviceType = "Dual";
                        break;
                    default:
                        deviceType = "未知设备";
                        break;
                }

                // 去重和更新设备列表
                if (!deviceNamesSet.contains(deviceName)) {
                    deviceNamesSet.add(deviceName);
                    devicesNames.add(String.format("%s (%ddBm, %s)", deviceName, rssi, deviceType));
                    readyDevices.add(device);

                    // 更新连接状态（初始为未连接）
                    ConDisDevices.add(false); // 添加到连接状态列表

                    // 在 UI 线程中更新适配器
                    getActivity().runOnUiThread(() -> btNames.notifyDataSetChanged());

                    // 日志记录
                    Log.d("BluetoothScan", "Found device: " + deviceName + " with RSSI: " + rssi);
                }
            }
        }



        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult result : results) {
                onScanResult(ScanSettings.CALLBACK_TYPE_ALL_MATCHES, result);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("BluetoothScan", "Scan failed with error: " + errorCode);
            MainActivity.showToast(getActivity(), "扫描失败: " + errorCode);
        }
    };

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            BluetoothDevice device = gatt.getDevice();
            if (device == null) {
                return; // 如果设备为 null，直接返回
            }

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                getActivity().runOnUiThread(() -> {
                    MainActivity.showToast(getActivity(), "已连接 " + device.getName());

                    // 更新连接状态
                    int position = readyDevices.indexOf(device);
                    if (position != -1) {
                        ConDisDevices.set(position, true); // 设置为已连接状态
                        btNames.notifyDataSetChanged(); // 更新适配器
                    }
                });
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                getActivity().runOnUiThread(() -> {
                    MainActivity.showToast(getActivity(), "蓝牙已断开");

                    // 更新连接状态
                    int position = readyDevices.indexOf(device);
                    if (position != -1) {
                        ConDisDevices.set(position, false); // 设置为未连接状态
                        btNames.notifyDataSetChanged(); // 更新适配器
                    }
                });
                MainActivity.setBluetoothConFlag(false);
            }
        }


        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                BluetoothGattService service = gatt.getService(UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb"));
                if (service != null) {
                    characteristic = service.getCharacteristic(UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb"));
                    if (characteristic != null) {
                        gatt.setCharacteristicNotification(characteristic, true);
                        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString("0000fff3-0000-1000-8000-00805f9b34fb"));
                        if (descriptor != null) {
                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            gatt.writeDescriptor(descriptor);
                        }
                        MainActivity.setBluetoothConFlag(true);
                        MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.bluetooth_connected);
                        mediaPlayer.start();
                    }
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                gatt.requestMtu(512);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // Handle successful write
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] data = characteristic.getValue();
            String receivedData = new String(data, StandardCharsets.UTF_8);
            String[] parts = receivedData.split("\r\n");

            for (String part : parts) {
                if (!part.trim().isEmpty()) {
                    getActivity().runOnUiThread(() -> {
                        viewModel1.setDataGroup1(part);
                    });
                    break;
                }
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timerHandler != null && timerRunnable != null) {
            timerHandler.removeCallbacks(timerRunnable);
        }
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
            bluetoothGatt = null;
        }
        if (isScanning) {
            stopScan();
        }
    }
}
