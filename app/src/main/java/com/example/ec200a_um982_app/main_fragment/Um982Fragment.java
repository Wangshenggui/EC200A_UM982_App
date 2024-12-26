package com.example.ec200a_um982_app.main_fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.ec200a_um982_app.R;
import com.example.ec200a_um982_app.main_fragment.um982_topfragment.Um982_top1Fragment;
import com.example.ec200a_um982_app.main_fragment.um982_topfragment.Um982_top2Fragment;
import com.example.ec200a_um982_app.main_fragment.um982_topfragment.Um982_top3Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * 一个简单的 {@link Fragment} 子类。
 * 使用 {@link Um982Fragment#newInstance} 工厂方法来创建该 Fragment 的实例。
 */
public class Um982Fragment extends Fragment {

    private BottomNavigationView mNavigationView;  // 底部导航栏
    private FragmentManager mFragmentManager;      // 管理 Fragment 的 FragmentManager
    private int lastFragment;                      // 记录上一个显示的 Fragment
    private Fragment[] fragments;                  // 存储所有的 Fragment
    private Handler mHandler;                      // 用于处理定时任务
    private Runnable mFragmentSwitchRunnable;      // 用于自动切换 Fragment 的 Runnable

    public Um982Fragment() {
        // 空的构造函数，Fragment 必须要有这个构造函数
    }

    /**
     * 创建该 Fragment 的新实例，带有传入的参数。
     */
    public static Um982Fragment newInstance(String param1, String param2) {
        Um982Fragment fragment = new Um982Fragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);  // 将 param1 放入 Bundle 中
        args.putString("param2", param2);  // 将 param2 放入 Bundle 中
        fragment.setArguments(args);        // 设置参数
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // 获取传入的参数
            String mParam1 = getArguments().getString("param1");
            String mParam2 = getArguments().getString("param2");
        }

        mHandler = new Handler();  // 初始化 Handler
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 填充布局并返回视图
        View view = inflater.inflate(R.layout.fragment_um982, container, false);

        // 初始化 BottomNavigationView
        mNavigationView = view.findViewById(R.id.main_um982_top_navigation_bar);

        // 初始化 Fragments 和监听器
        initFragment();
        initListener();

        // 启动自动切换 Fragment 的功能
        startFragmentSwitching();

        return view;
    }

    // 初始化并添加 Fragments
    private void initFragment() {
        Um982_top1Fragment mUm982_top1Fragment = new Um982_top1Fragment();
        Um982_top2Fragment mUm982_top2Fragment = new Um982_top2Fragment();
        Um982_top3Fragment mUm982_top3Fragment = new Um982_top3Fragment();

        // 将所有 Fragment 存储在数组中
        fragments = new Fragment[]{mUm982_top1Fragment, mUm982_top2Fragment, mUm982_top3Fragment};

        // 获取 FragmentManager
        mFragmentManager = getChildFragmentManager();

        // 开始 FragmentTransaction，添加所有的 Fragment
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        // 确保所有 Fragment 被提前添加
        if (!mUm982_top1Fragment.isAdded()) {
            transaction.add(R.id.main_um982_top_page_controller, mUm982_top1Fragment);
        }
        if (!mUm982_top2Fragment.isAdded()) {
            transaction.add(R.id.main_um982_top_page_controller, mUm982_top2Fragment);
        }
        if (!mUm982_top3Fragment.isAdded()) {
            transaction.add(R.id.main_um982_top_page_controller, mUm982_top3Fragment);
        }

        // 隐藏其他 Fragment，只有第一个 Fragment 会显示
        transaction.hide(mUm982_top2Fragment)
                .hide(mUm982_top3Fragment)
                .show(mUm982_top1Fragment) // 默认显示第一个 Fragment
                .commit();

        // 设置 lastFragment 为第一个 Fragment
        lastFragment = 0;
    }


    // 初始化 BottomNavigationView 的监听器
    private void initListener() {
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.um982_top1) {
                    // 如果点击的是第一个菜单项，且当前不是第一个 Fragment，则切换到第一个 Fragment
                    if (lastFragment != 0) {
                        switchFragment(lastFragment, 0);
                        lastFragment = 0;
                    }
                    return true;
                } else if (i == R.id.um982_top2) {
                    // 如果点击的是第二个菜单项，且当前不是第二个 Fragment，则切换到第二个 Fragment
                    if (lastFragment != 1) {
                        switchFragment(lastFragment, 1);
                        lastFragment = 1;
                    }
                    return true;
                } else if (i == R.id.um982_top3) {
                    // 如果点击的是第三个菜单项，且当前不是第三个 Fragment，则切换到第三个 Fragment
                    if (lastFragment != 2) {
                        switchFragment(lastFragment, 2);
                        lastFragment = 2;
                    }
                    return true;
                }
                return false;
            }
        });
    }

    // 切换 Fragment
    private void switchFragment(int lastFragment, int index) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.hide(fragments[lastFragment]); // 隐藏当前显示的 Fragment
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.main_um982_top_page_controller, fragments[index]); // 如果 Fragment 还没有添加，则添加它
        }
        transaction.show(fragments[index]).commitAllowingStateLoss(); // 显示目标 Fragment
    }

    public void switchToFragment(int index) {
        // 检查 index 是否有效
        if (index < 0 || index >= fragments.length) {
            return;  // 如果 index 不在有效范围内，则不做任何操作
        }

        // 切换 Fragment
        switchFragment(lastFragment, index); // 切换 Fragment
        lastFragment = index; // 更新 lastFragment 为当前显示的 Fragment 索引

        // 更新 BottomNavigationView 的选中项
        if (index == 0) {
            mNavigationView.setSelectedItemId(R.id.um982_top1); // 更新为第一个菜单项
        } else if (index == 1) {
            mNavigationView.setSelectedItemId(R.id.um982_top2); // 更新为第二个菜单项
        } else if (index == 2) {
            mNavigationView.setSelectedItemId(R.id.um982_top3); // 更新为第三个菜单项
        }
    }

    // 启动自动切换 Fragment 的功能，每 5 秒切换一次
    private void startFragmentSwitching() {
        // 创建 Runnable，每 5 秒切换一次 Fragment
        mFragmentSwitchRunnable = new Runnable() {
            @Override
            public void run() {
                // 计算下一个要显示的 Fragment
                int nextFragment = (lastFragment + 1) % fragments.length;
//                switchToFragment(nextFragment);

                // 3 秒后再次执行该任务，确保循环执行
                mHandler.postDelayed(this, 3000);
            }
        };

        // 启动第一次切换任务，延迟 3 秒后执行
        mHandler.postDelayed(mFragmentSwitchRunnable, 3000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 在视图销毁时移除延迟任务，避免内存泄漏
        mHandler.removeCallbacks(mFragmentSwitchRunnable);
    }
}
