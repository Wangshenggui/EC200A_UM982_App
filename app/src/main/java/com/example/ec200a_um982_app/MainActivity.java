package com.example.ec200a_um982_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

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
import com.example.ec200a_um982_app.main_fragment.Um982Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupNavigationView();
        // 延迟加载其他Fragment以避免崩溃
        new Handler().postDelayed(this::loadOtherFragments, 500);
    }

    private void setupNavigationView() {
        mNavigationView = findViewById(R.id.main_navigation_bar);
        initFragment();
        initListener();
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
        fragments = new Fragment[]{mBluetoothFragment, mNtripFragment, mWebFragment};
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
            }
            return false;
        });
    }
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