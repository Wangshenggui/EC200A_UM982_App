package com.example.ec200a_um982_app.BtThread;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ConnectThread extends Thread {

    private static final String TAG = "ConnectThread";

    // 替换为你的实际 UUID
    private static final UUID SERVICE_UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    private static final UUID TX_CHARACTERISTIC_UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    private static final UUID RX_CHARACTERISTIC_UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    private static final UUID NOTIFICATION_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    private BluetoothDevice bluetoothDevice;
    private BluetoothGatt bluetoothGatt;
    private Context context;

    public ConnectThread(Context context, BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
        this.context = context;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void run() {
        super.run();
        bluetoothGatt = bluetoothDevice.connectGatt(context, false, bluetoothGattCallback);
    }

    private final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i(TAG, "已连接到 GATT 服务器。");
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i(TAG, "已从 GATT 服务器断开连接。");
                cancel();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "服务已发现。");
                BluetoothGattService service = gatt.getService(SERVICE_UUID);
                if (service != null) {
                    // 获取 TX 和 RX 特征
                    BluetoothGattCharacteristic txCharacteristic = service.getCharacteristic(TX_CHARACTERISTIC_UUID);
                    BluetoothGattCharacteristic rxCharacteristic = service.getCharacteristic(RX_CHARACTERISTIC_UUID);

                    if (rxCharacteristic != null) {
                        // 启用 RX 特征的通知
                        gatt.setCharacteristicNotification(rxCharacteristic, true);
                        BluetoothGattDescriptor descriptor = rxCharacteristic.getDescriptor(NOTIFICATION_DESCRIPTOR_UUID);
                        if (descriptor != null) {
                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            gatt.writeDescriptor(descriptor);
                        }
                        Log.i(TAG, "找到 RX 特征并启用通知。");
                    } else {
                        Log.w(TAG, "RX 特征未找到。");
                    }

                    if (txCharacteristic != null) {
                        // 发送示例命令到 TX 特征
                        txCharacteristic.setValue("$PAIR062,3,0*3D\r\n");
                        gatt.writeCharacteristic(txCharacteristic);
                        Log.i(TAG, "已写入 TX 特征。");
                    } else {
                        Log.w(TAG, "TX 特征未找到。");
                    }
                } else {
                    Log.w(TAG, "服务未找到。");
                }
            } else {
                Log.w(TAG, "onServicesDiscovered 接收到状态: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                byte[] data = characteristic.getValue();
                String readValue = new String(data, StandardCharsets.UTF_8);
                Log.i(TAG, "特征值读取成功: " + readValue);
            } else {
                Log.w(TAG, "特征值读取失败，状态: " + status);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "特征值写入成功。");
            } else {
                Log.w(TAG, "特征值写入失败，状态: " + status);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            byte[] data = characteristic.getValue();
            String receivedData = new String(data, StandardCharsets.UTF_8);
            Log.i(TAG, "特征值已更改: " + receivedData);
        }
    };

    public void cancel() {
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
            bluetoothGatt = null;
        }
    }
}
