package com.example.ec200a_um982_app.main_fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.ec200a_um982_app.R;
import com.example.ec200a_um982_app.main_fragment.bluetooth_topfragment.Bluetooth_top1Fragment;
import com.example.ec200a_um982_app.main_fragment.bluetooth_topfragment.Bluetooth_top2Fragment;
import com.example.ec200a_um982_app.main_fragment.bluetooth_topfragment.Bluetooth_top3Fragment;
import com.example.ec200a_um982_app.main_fragment.um982_topfragment.Um982_top1Fragment;
import com.example.ec200a_um982_app.main_fragment.um982_topfragment.Um982_top2Fragment;
import com.example.ec200a_um982_app.main_fragment.um982_topfragment.Um982_top3Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BluetoothFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private BottomNavigationView mNavigationView;
    private FragmentManager mFragmentManager;
    private int lastFragment;
    private Fragment[] fragments;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BluetoothFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Um982Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BluetoothFragment newInstance(String param1, String param2) {
        BluetoothFragment fragment = new BluetoothFragment();
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
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);

        // Initialize BottomNavigationView
        mNavigationView = view.findViewById(R.id.main_bluetooth_top_navigation_bar);

        // Initialize fragments and listeners
        initFragment();
        initListener();

        // Delay loading other fragments to avoid crash
        new Handler().postDelayed(this::loadOtherFragments, 500);

        return view;
    }

    // Initialize and add the fragments to the FragmentManager
    private void initFragment() {
        Bluetooth_top1Fragment mBluetooth_top1Fragment = new Bluetooth_top1Fragment();
        Bluetooth_top2Fragment mBluetooth_top2Fragment = new Bluetooth_top2Fragment();
        Bluetooth_top3Fragment mBluetooth_top3Fragment = new Bluetooth_top3Fragment();

        // Store fragments in an array
        fragments = new Fragment[]{mBluetooth_top1Fragment, mBluetooth_top2Fragment, mBluetooth_top3Fragment};

        // Get the FragmentManager
        mFragmentManager = getChildFragmentManager();

        // Show the first fragment by default
        lastFragment = 0;
        mFragmentManager.beginTransaction()
                .replace(R.id.main_bluetooth_top_page_controller, mBluetooth_top1Fragment)
                .show(mBluetooth_top1Fragment)
                .commit();
    }

    // Initialize the BottomNavigationView listener
    private void initListener() {
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.bluetooth_top1) {
                    if (lastFragment != 0) {
                        switchFragment(lastFragment, 0);
                        lastFragment = 0;
                    }
                    return true;
                }
//                else if (i == R.id.bluetooth_top2) {
//                    if (lastFragment != 1) {
//                        switchFragment(lastFragment, 1);
//                        lastFragment = 1;
//                    }
//                    return true;
//                }
//                else if (i == R.id.bluetooth_top3) {
//                    if (lastFragment != 2) {
//                        switchFragment(lastFragment, 2);
//                        lastFragment = 2;
//                    }
//                    return true;
//                }
                return false;
            }
        });
    }

    // Switch between fragments
    private void switchFragment(int lastFragment, int index) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.hide(fragments[lastFragment]);
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.main_bluetooth_top_page_controller, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

    // Load other fragments and hide them
    private void loadOtherFragments() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        for (int i = 1; i < fragments.length; i++) {
            if (!fragments[i].isAdded()) {
                transaction.add(R.id.main_bluetooth_top_page_controller, fragments[i]);
                transaction.hide(fragments[i]);
            }
        }
        transaction.commitAllowingStateLoss();
    }
}