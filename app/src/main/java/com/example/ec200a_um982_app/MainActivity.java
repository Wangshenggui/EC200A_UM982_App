package com.example.ec200a_um982_app;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.content.ServiceConnection;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ec200a_um982_app.main_fragment.BluetoothFragment;
import com.example.ec200a_um982_app.main_fragment.NtripFragment;
import com.example.ec200a_um982_app.main_fragment.SettingFragment;
import com.example.ec200a_um982_app.main_fragment.Um982Fragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static final Object BluetoothCon_lock = new Object();
    public static boolean BluetoothConFlag = false;
    public static boolean getBluetoothConFlag() {
        synchronized (BluetoothCon_lock) {
            return BluetoothConFlag;
        }
    }

    public static void setBluetoothConFlag(boolean flag) {
        synchronized (BluetoothCon_lock) {
            BluetoothConFlag = flag;
        }
    }

    private static Toast toast;

    private BottomNavigationView mNavigationView;
    private FragmentManager mFragmentManager;
    private Fragment[] fragments;
    private int lastFragment;

    BadgeDrawable badgeDrawable;
    BottomNavigationView bottomNavigationView;

    public static boolean isBound = false;
    public static SocketService socketService;


    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SocketService.LocalBinder binder = (SocketService.LocalBinder) service;
            socketService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 启动Socket服务
        Intent socketServiceIntent = new Intent(this, SocketService.class);
        startService(socketServiceIntent);

        setupNavigationView();
        // 延迟加载其他Fragment以避免崩溃
        new Handler().postDelayed(this::loadOtherFragments, 500);

        // 注册广播接收器
        IntentFilter filter = new IntentFilter("MY_CUSTOM_ACTION");
        registerReceiver(messageReceiver, filter);
    }

    private void setupNavigationView() {
        mNavigationView = findViewById(R.id.main_navigation_bar);
        initFragment();
        initListener();

        // 设置徽章
        Menu menu = mNavigationView.getMenu();
        MenuItem setItem = menu.findItem(R.id.Set);

        // 创建徽章并配置
        badgeDrawable = BadgeDrawable.create(this);
        badgeDrawable.setNumber(1); // 设置徽章上的数字
        badgeDrawable.setBackgroundColor(Color.RED); // 设置徽章背景颜色
        badgeDrawable.setBadgeTextColor(Color.WHITE); // 设置徽章文字颜色

        // 使用 BottomNavigationView 的 BadgeDrawable API
        bottomNavigationView = findViewById(R.id.main_navigation_bar);
        bottomNavigationView.getOrCreateBadge(R.id.Set).setNumber(1);
        bottomNavigationView.getOrCreateBadge(R.id.Set).setBackgroundColor(Color.RED);
        bottomNavigationView.getOrCreateBadge(R.id.Set).setBadgeTextColor(Color.WHITE);

        //默认情况下不显示
        bottomNavigationView.removeBadge(R.id.Set);
    }
    private void loadOtherFragments() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        for (int i = 1; i < fragments.length; i++) {
            if (!fragments[i].isAdded()) {
                transaction.add(R.id.main_page_controller, fragments[i]);
                transaction.hide(fragments[i]);
            }
        }
        transaction.commitAllowingStateLoss();
    }
    private void initFragment() {
        BluetoothFragment mBluetoothFragment = new BluetoothFragment();
        NtripFragment mNtripFragment = new NtripFragment();
        Um982Fragment mWebFragment = new Um982Fragment();
        SettingFragment mSettingFragment = new SettingFragment();
        fragments = new Fragment[]{mBluetoothFragment, mNtripFragment, mWebFragment, mSettingFragment};
        mFragmentManager = getSupportFragmentManager();
        lastFragment = 0;
        mFragmentManager.beginTransaction()
                .replace(R.id.main_page_controller, mBluetoothFragment)
                .show(mBluetoothFragment)
                .commit();
    }

    private void initListener() {
        mNavigationView.setOnNavigationItemSelectedListener(item -> {
            int i = item.getItemId();
            if (i == R.id.Bluetooth && lastFragment != 0) {
                switchFragment(lastFragment, 0);
                lastFragment = 0;
                return true;
            } else if (i == R.id.Ntrip && lastFragment != 1) {
                switchFragment(lastFragment, 1);
                lastFragment = 1;
                return true;
            } else if (i == R.id.Um982 && lastFragment != 2) {
                switchFragment(lastFragment, 2);
                lastFragment = 2;
                return true;
            } else if (i == R.id.Set && lastFragment != 3) {
                switchFragment(lastFragment, 3);
                lastFragment = 3;
                return true;
            }
            return false;
        });
    }
    public int stringToInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            // 处理可能的异常，比如字符串无法转换为数字
            e.printStackTrace();
            return 0; // 或者返回其他合适的值
        }
    }
    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            // 处理接收到的消息
            if(Objects.equals(message, "false")){
                //不显示
                bottomNavigationView.removeBadge(R.id.Set);
            } else {
                //显示
                badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.Set);
                badgeDrawable.setNumber(stringToInt(message));
                badgeDrawable.setBackgroundColor(Color.RED);
                badgeDrawable.setBadgeTextColor(Color.WHITE);
            }
        }
    };
    private void switchFragment(int lastFragment, int index) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.hide(fragments[lastFragment]);
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.main_page_controller, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, SocketService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public static void showToast(Context context, String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}