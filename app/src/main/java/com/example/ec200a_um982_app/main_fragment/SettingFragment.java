package com.example.ec200a_um982_app.main_fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ec200a_um982_app.MainActivity;
import com.example.ec200a_um982_app.R;
import com.example.ec200a_um982_app.SharedViewModel;
import com.example.ec200a_um982_app.SocketService;
import com.example.ec200a_um982_app.main_fragment.um982_topfragment.Um982_top2Fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView notification_badge;
    TextView notification_badge1;
    private static final String APK_BASE_URL = "http://47.109.46.41:3000/EC200A_UM982_App-";
    private static final String APK_EXTENSION = ".apk";
    private static final String VERSION_URL = "http://47.109.46.41/file_download/ec200a_um982/version.txt"; // 替换为实际的版本 URL
    private String mApkUrl; // 用于保存 APK 下载链接

    Intent intent;

    private Handler handler = new Handler();
    private Runnable toastRunnable;

    String DetectionCurrentVersion;
    String DetectionServerVersion;

    private SharedViewModel viewModel2;

    int SysUpdate = 0;
    int Module4G = 0;

    int NumUpdate = 0;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // 创建 Intent 并设置要传递的数据
        intent = new Intent("MY_CUSTOM_ACTION");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Find views
        LinearLayout accountSettingsContainer = view.findViewById(R.id.account_settings_container);
        LinearLayout update4gcodeContainer = view.findViewById(R.id.update_4gcode_container);

        // Set click listeners
        accountSettingsContainer.setOnClickListener(v -> {
            // Handle account settings click
            navigateToAccountSettings();
        });
        update4gcodeContainer.setOnClickListener(v -> {
            navigateTo4GUpdate();
        });

        notification_badge = view.findViewById(R.id.notification_badge);
        notification_badge.setVisibility(View.GONE);
        notification_badge1 = view.findViewById(R.id.notification_badge1);
        notification_badge1.setVisibility(View.GONE);

        checkForUpdateInit();


        toastRunnable = new Runnable() {
            @Override
            public void run() {
//                checkForUpdateInit();

                new Thread(() -> {
                    try {
                        // 获取服务器上的版本号
                        URL url = new URL(VERSION_URL);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");

                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String serverVersion = reader.readLine().trim(); // 获取版本号
                        reader.close();

                        DetectionServerVersion = serverVersion;

                        // 获取当前应用的版本号
                        String currentVersion = getCurrentAppVersion();

                        DetectionCurrentVersion = currentVersion;

                    } catch (IOException e) {
                        e.printStackTrace();
                        requireActivity().runOnUiThread(() -> MainActivity.showToast(getActivity(), "检查更新失败"));
                    }
                }).start();

                // 对比版本号
                if (DetectionCurrentVersion != null && compareVersionStrings(DetectionCurrentVersion, DetectionServerVersion) < 0) {
                    // 当前版本小于服务器版本，提示用户更新
                    notification_badge.setVisibility(View.VISIBLE);

                    SysUpdate = 1;
                } else {
                    // 当前版本已是最新
                    notification_badge.setVisibility(View.GONE);

                    SysUpdate = 0;
                }
                NumUpdate = SysUpdate + Module4G;
                if(NumUpdate == 0) {
                    intent.putExtra("message", "false");
                } else if (NumUpdate == 1) {
                    intent.putExtra("message", "1");
                } else if (NumUpdate == 2) {
                    intent.putExtra("message", "2");
                }

                // 发送广播
                requireContext().sendBroadcast(intent);

                // 继续每秒钟执行
                handler.postDelayed(this, 1000);
            }
        };
        // 启动定时任务
        handler.post(toastRunnable);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel2 = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel2.getDataGroup2().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String data) {
                if (data != null && data.startsWith("$UPDATE,")) {
                    int[] indices = {2};
                    List<String> results = Um982_top2Fragment.getSpecificSubstrings(data, indices);
                    String outstring = removeAsteriskAndAfter(results.get(0));

                    if (outstring.equals("TRUE")) {
                        notification_badge1.setVisibility(View.VISIBLE);
                        Module4G = 1;
                    } else {
                        notification_badge1.setVisibility(View.GONE);

                        Module4G = 0;
                    }
                }
            }
            public String removeAsteriskAndAfter(String input) {
                int index = input.indexOf('*');
                if (index != -1) {
                    return input.substring(0, index);  // 返回 * 之前的部分
                }
                return input;  // 如果没有 *，返回原字符串
            }
        });
    }

    private void navigateToAccountSettings() {
        // Navigate to account settings
        checkForUpdate();
    }
    private void navigateTo4GUpdate() {
        // Navigate to account settings
        checkFor4GUpdate();
    }

    private void checkForUpdateInit() {
        new Thread(() -> {
            try {
                // 获取服务器上的版本号
                URL url = new URL(VERSION_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String serverVersion = reader.readLine().trim(); // 获取版本号
                reader.close();

                // 获取当前应用的版本号
                String currentVersion = getCurrentAppVersion();

                // 对比版本号
                if (currentVersion != null && compareVersionStrings(currentVersion, serverVersion) < 0) {
                    // 当前版本小于服务器版本，提示用户更新
//                    notification_badge.setVisibility(View.VISIBLE);
                    intent.putExtra("message", "true");
                    SysUpdate = 1;
                } else {
                    // 当前版本已是最新
//                    notification_badge.setVisibility(View.GONE);
                    intent.putExtra("message", "false");
                    SysUpdate = 0;
                }
                // 发送广播
                requireContext().sendBroadcast(intent);
            } catch (IOException e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() -> MainActivity.showToast(getActivity(), "检查更新失败"));
            }
        }).start();
    }

    // 生成 SHA-256 密钥
    private String generateKey(String filename, long timestamp, String secretKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String input = filename + timestamp + secretKey;
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void checkForUpdate() {
        new Thread(() -> {
            try {
                // 获取服务器上的版本号
                URL url = new URL(VERSION_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String serverVersion = reader.readLine().trim(); // 获取版本号
                reader.close();

                // 保存 APK 下载链接
                mApkUrl = APK_BASE_URL + serverVersion + APK_EXTENSION;

                long currentTime = System.currentTimeMillis() / 1000;
                String filename = "EC200A_UM982_App-" + serverVersion + APK_EXTENSION;
                String secretKey = "123456";
                String key = generateKey(filename, currentTime, secretKey);
                mApkUrl = "http://47.109.46.41:3000/" + "ec200a_um982/" + filename + "?time=" + currentTime + "&key=" + key;

                // 获取当前应用的版本号
                String currentVersion = getCurrentAppVersion();

                // 对比版本号
                if (currentVersion != null && compareVersionStrings(currentVersion, serverVersion) < 0) {
                    // 当前版本小于服务器版本，提示用户更新
                    requireActivity().runOnUiThread(() -> showUpdateDialog(serverVersion));
                } else {
                    // 当前版本已是最新
                    requireActivity().runOnUiThread(() -> MainActivity.showToast(getActivity(), "当前已是最新版本"));
                }

            } catch (IOException e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() -> MainActivity.showToast(getActivity(), "检查更新失败"));
            }
        }).start();
    }
    private void checkFor4GUpdate() {
        new Thread(() -> {
            if (Module4G == 1) {
                requireActivity().runOnUiThread(() -> show4GUpdateDialog());
            } else {
                // 当前版本已是最新
                requireActivity().runOnUiThread(() -> MainActivity.showToast(getActivity(), "当前已是最新版本"));
            }
        }).start();
    }

    private String getCurrentAppVersion() {
        try {
            PackageManager packageManager = requireContext().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(requireContext().getPackageName(), 0);
            return packageInfo.versionName;  // 获取应用的版本名
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    private int compareVersionStrings(String version1, String version2) {
        String[] version1Parts = version1.split("\\.");
        String[] version2Parts = version2.split("\\.");

        for (int i = 0; i < Math.min(version1Parts.length, version2Parts.length); i++) {
            int v1 = Integer.parseInt(version1Parts[i]);
            int v2 = Integer.parseInt(version2Parts[i]);

            if (v1 != v2) {
                return Integer.compare(v1, v2);
            }
        }

        return Integer.compare(version1Parts.length, version2Parts.length);
    }
    private void showUpdateDialog(String serverVersion) {
        new AlertDialog.Builder(requireContext())
                .setTitle("更新可用")
                .setMessage("发现新版本 " + serverVersion + "，是否下载更新？")
                .setPositiveButton("下载", (dialog, which) -> startDownload())
                .setNegativeButton("取消", null)
                .show();
    }
    private void show4GUpdateDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("更新可用")
                .setMessage("发现新版本，是否下载更新？")
                .setPositiveButton("下载", (dialog, which) -> start4GDownload())
                .setNegativeButton("取消", null)
                .show();
    }

    private void startDownload() {
        if (mApkUrl != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mApkUrl)); // 使用动态构建的 APK URL
            startActivity(intent);
        } else {
            MainActivity.showToast(getActivity(), "下载链接无效");
        }
    }
    private void start4GDownload() {
        BluetoothFragment.characteristic.setValue("AT+UPDATE=TRUE\r\n");
        BluetoothFragment.bluetoothGatt.writeCharacteristic(BluetoothFragment.characteristic);

        MainActivity.showToast(getActivity(),"发送更新指令到EC200A");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 移除所有回调和任务
        handler.removeCallbacks(toastRunnable);
    }

}