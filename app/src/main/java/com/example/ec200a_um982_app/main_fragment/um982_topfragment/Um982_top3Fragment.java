package com.example.ec200a_um982_app.main_fragment.um982_topfragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.ec200a_um982_app.MainActivity;
import com.example.ec200a_um982_app.R;
import com.example.ec200a_um982_app.SharedViewModel;
import com.example.ec200a_um982_app.main_fragment.BluetoothFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import kotlinx.coroutines.channels.Send;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Um982_top3Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Um982_top3Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SharedViewModel viewModel1;

    CheckBox DTMselectCheck;
    TextView DTMselectText;

    CheckBox GBSselectCheck;
    TextView GBSselectText;

    CheckBox GGAselectCheck;
    TextView GGAselectText;

    CheckBox GGAHselectCheck;
    TextView GGAHselectText;

    CheckBox GLLselectCheck;
    TextView GLLselectText;

    CheckBox GLLHselectCheck;
    TextView GLLHselectText;

    CheckBox GNSselectCheck;
    TextView GNSselectText;

    CheckBox GNSHselectCheck;
    TextView GNSHselectText;

    CheckBox GSTselectCheck;
    TextView GSTselectText;

    CheckBox GSTHselectCheck;
    TextView GSTHselectText;

    CheckBox THSselectCheck;
    TextView THSselectText;

    CheckBox RMCselectCheck;
    TextView RMCselectText;

    CheckBox RMCHselectCheck;
    TextView RMCHselectText;

    CheckBox ROTselectCheck;
    TextView ROTselectText;

    CheckBox VTGselectCheck;
    TextView VTGselectText;

    CheckBox VTGHselectCheck;
    TextView VTGHselectText;

    CheckBox ZDAselectCheck;
    TextView ZDAselectText;

    String SendData = "";
    boolean SendDataFlag = false;

    int n=0;

    CheckBox CheckBoxIndex;
    TextView associatedTextView = null;


    private Handler handler;
    private Runnable updateDataRunnable;
    private static final int UPDATE_INTERVAL = 300; // 1 second


    public Um982_top3Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Um982_top3Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Um982_top3Fragment newInstance(String param1, String param2) {
        Um982_top3Fragment fragment = new Um982_top3Fragment();
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
        View view = inflater.inflate(R.layout.fragment_um982_top3, container, false);

        DTMselectCheck = view.findViewById(R.id.DTMselectCheck);
        GBSselectCheck = view.findViewById(R.id.GBSselectCheck);
        GGAselectCheck = view.findViewById(R.id.GGAselectCheck);
        GGAHselectCheck = view.findViewById(R.id.GGAHselectCheck);
        GLLselectCheck = view.findViewById(R.id.GLLselectCheck);
        GLLHselectCheck = view.findViewById(R.id.GLLHselectCheck);
        GNSselectCheck = view.findViewById(R.id.GNSselectCheck);
        GNSHselectCheck = view.findViewById(R.id.GNSHselectCheck);
        GSTselectCheck = view.findViewById(R.id.GSTselectCheck);
        GSTHselectCheck = view.findViewById(R.id.GSTHselectCheck);
        THSselectCheck = view.findViewById(R.id.THSselectCheck);
        RMCselectCheck = view.findViewById(R.id.RMCselectCheck);
        RMCHselectCheck = view.findViewById(R.id.RMCHselectCheck);
        ROTselectCheck = view.findViewById(R.id.ROTselectCheck);
        VTGselectCheck = view.findViewById(R.id.VTGselectCheck);
        VTGHselectCheck = view.findViewById(R.id.VTGHselectCheck);
        ZDAselectCheck = view.findViewById(R.id.ZDAselectCheck);

        DTMselectText = view.findViewById(R.id.DTMselectText);
        GBSselectText = view.findViewById(R.id.GBSselectText);
        GGAselectText = view.findViewById(R.id.GGAselectText);
        GGAHselectText = view.findViewById(R.id.GGAHselectText);
        GLLselectText = view.findViewById(R.id.GLLselectText);
        GLLHselectText = view.findViewById(R.id.GLLHselectText);
        GNSselectText = view.findViewById(R.id.GNSselectText);
        GNSHselectText = view.findViewById(R.id.GNSHselectText);
        GSTselectText = view.findViewById(R.id.GSTselectText);
        GSTHselectText = view.findViewById(R.id.GSTHselectText);
        THSselectText = view.findViewById(R.id.THSselectText);
        RMCselectText = view.findViewById(R.id.RMCselectText);
        RMCHselectText = view.findViewById(R.id.RMCHselectText);
        ROTselectText = view.findViewById(R.id.ROTselectText);
        VTGselectText = view.findViewById(R.id.VTGselectText);
        VTGHselectText = view.findViewById(R.id.VTGHselectText);
        ZDAselectText = view.findViewById(R.id.ZDAselectText);


        // 为每个 CheckBox 设置监听器
        DTMselectCheck.setOnCheckedChangeListener(CheckBoxCallbackHandler);
        GBSselectCheck.setOnCheckedChangeListener(CheckBoxCallbackHandler);
        GGAselectCheck.setOnCheckedChangeListener(CheckBoxCallbackHandler);
        GGAHselectCheck.setOnCheckedChangeListener(CheckBoxCallbackHandler);
        GLLselectCheck.setOnCheckedChangeListener(CheckBoxCallbackHandler);
        GLLHselectCheck.setOnCheckedChangeListener(CheckBoxCallbackHandler);
        GNSselectCheck.setOnCheckedChangeListener(CheckBoxCallbackHandler);
        GNSHselectCheck.setOnCheckedChangeListener(CheckBoxCallbackHandler);
        GSTselectCheck.setOnCheckedChangeListener(CheckBoxCallbackHandler);
        GSTHselectCheck.setOnCheckedChangeListener(CheckBoxCallbackHandler);
        THSselectCheck.setOnCheckedChangeListener(CheckBoxCallbackHandler);
        RMCselectCheck.setOnCheckedChangeListener(CheckBoxCallbackHandler);
        RMCHselectCheck.setOnCheckedChangeListener(CheckBoxCallbackHandler);
        ROTselectCheck.setOnCheckedChangeListener(CheckBoxCallbackHandler);
        VTGselectCheck.setOnCheckedChangeListener(CheckBoxCallbackHandler);
        VTGHselectCheck.setOnCheckedChangeListener(CheckBoxCallbackHandler);
        ZDAselectCheck.setOnCheckedChangeListener(CheckBoxCallbackHandler);

        handler = new Handler();
        updateDataRunnable = new Runnable() {
            @Override
            public void run() {
                if (SendDataFlag) {
                    if (MainActivity.getBluetoothConFlag()) {
                        BluetoothFragment.characteristic.setValue(SendData);
                        BluetoothFragment.bluetoothGatt.writeCharacteristic(BluetoothFragment.characteristic);
                    } else {
                        MainActivity.showToast(getActivity(), "请连接蓝牙");
                        SendDataFlag = false;
                        handler.removeCallbacks(updateDataRunnable);

                        CheckBoxIndex.setOnCheckedChangeListener(null);  // 移除监听器
                        CheckBoxIndex.setChecked(!CheckBoxIndex.isChecked());
                        if (associatedTextView != null) {
                            changeTextColor(associatedTextView, SendDataFlag);
                        }
                        CheckBoxIndex.setOnCheckedChangeListener(CheckBoxCallbackHandler);
                    }

                    handler.postDelayed(this, UPDATE_INTERVAL);
                }
            }
        };

//        handler.post(updateDataRunnable);

        return view;
    }

    // 统一的 CheckBox 状态变化监听器
    private final CompoundButton.OnCheckedChangeListener CheckBoxCallbackHandler = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int id = buttonView.getId();


            if (id == R.id.DTMselectCheck) {
                CheckBoxIndex = DTMselectCheck;
                associatedTextView = DTMselectText;
            } else if (id == R.id.GBSselectCheck) {
                CheckBoxIndex = GBSselectCheck;
                associatedTextView = GBSselectText;
            } else if (id == R.id.GGAselectCheck) {
                CheckBoxIndex = GGAselectCheck;
                associatedTextView = GGAselectText;
            } else if (id == R.id.GGAHselectCheck) {
                CheckBoxIndex = GGAHselectCheck;
                associatedTextView = GGAHselectText;
            } else if (id == R.id.GLLselectCheck) {
                CheckBoxIndex = GLLselectCheck;
                associatedTextView = GLLselectText;
            } else if (id == R.id.GLLHselectCheck) {
                CheckBoxIndex = GLLHselectCheck;
                associatedTextView = GLLHselectText;
            } else if (id == R.id.GNSselectCheck) {
                CheckBoxIndex = GNSselectCheck;
                associatedTextView = GNSselectText;
            } else if (id == R.id.GNSHselectCheck) {
                CheckBoxIndex = GNSHselectCheck;
                associatedTextView = GNSHselectText;
            } else if (id == R.id.GSTselectCheck) {
                CheckBoxIndex = GSTselectCheck;
                associatedTextView = GSTselectText;
            } else if (id == R.id.GSTHselectCheck) {
                CheckBoxIndex = GSTHselectCheck;
                associatedTextView = GSTHselectText;
            } else if (id == R.id.THSselectCheck) {
                CheckBoxIndex = THSselectCheck;
                associatedTextView = THSselectText;
            } else if (id == R.id.RMCselectCheck) {
                CheckBoxIndex = RMCselectCheck;
                associatedTextView = RMCselectText;
            } else if (id == R.id.RMCHselectCheck) {
                CheckBoxIndex = RMCHselectCheck;
                associatedTextView = RMCHselectText;
            } else if (id == R.id.ROTselectCheck) {
                CheckBoxIndex = ROTselectCheck;
                associatedTextView = ROTselectText;
            } else if (id == R.id.VTGselectCheck) {
                CheckBoxIndex = VTGselectCheck;
                associatedTextView = VTGselectText;
            } else if (id == R.id.VTGHselectCheck) {
                CheckBoxIndex = VTGHselectCheck;
                associatedTextView = VTGHselectText;
            } else if (id == R.id.ZDAselectCheck) {
                CheckBoxIndex = ZDAselectCheck;
                associatedTextView = ZDAselectText;
            }

            if (associatedTextView != null) {
                handleBluetoothCommand(associatedTextView, isChecked);
                changeTextColor(associatedTextView, isChecked);
            }
        }
    };

    private AlertDialog dialog;  // 全局引用对话框
    private void handleBluetoothCommand(TextView textView, boolean isChecked) {
        String input = textView.getText().toString();
        String result = input.split(" ")[0];

        if (isChecked) {
            SendData = "AT+UM982=" + result + " 1" + "\r\n";
            SendDataFlag = true;
        } else {
            SendData = "AT+UM982=" + "UNLOG " + result + "\r\n";
            SendDataFlag = true;
        }

        handler.post(updateDataRunnable);

        n=10;

        // 弹出对话框
        showConfirmationDialog(result, isChecked);
    }

    private void showConfirmationDialog(String result, boolean isChecked) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("操作中");
        String message = isChecked ? "正在启用 " + result : "正在禁用 " + result;

        // 创建对话框
        dialog = builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // 关闭对话框
                    }
                })
                .create();

        // 显示对话框
        dialog.show();

        // 启动计时器更新存在时间
        final long startTime = System.currentTimeMillis();
        final Handler handler = new Handler();

        Runnable updateRunnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    String elapsedMessage = String.format("%s\n\t\t\t %ds",
                            message, elapsedTime / 1000);
                    dialog.setMessage(elapsedMessage);

                    // 每秒更新一次
                    handler.postDelayed(this, 1000);

                    // 超过10秒自动关闭
                    if (elapsedTime >= 10000) {
                        SendDataFlag = false;
                        handler.removeCallbacks(updateDataRunnable);

                        CheckBoxIndex.setOnCheckedChangeListener(null);  // 移除监听器
                        CheckBoxIndex.setChecked(!CheckBoxIndex.isChecked());
                        if (associatedTextView != null) {
                            changeTextColor(associatedTextView, SendDataFlag);
                        }
                        CheckBoxIndex.setOnCheckedChangeListener(CheckBoxCallbackHandler);
                        dialog.dismiss();
                    }
                }
            }
        };

        handler.post(updateRunnable); // 启动更新
    }

    private void changeTextColor(TextView textView, boolean isChecked) {
        if (isChecked) {
            textView.setTextColor(0xFF00BFFF); // 深天蓝
        } else {
            textView.setTextColor(0xFF333333); // 深灰色
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel1 = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel1.getDataGroup1().observe(getViewLifecycleOwner(), new Observer<String>() {

            @Override
            public void onChanged(String data) {
                if (data.length() >= 2 && data.charAt(0) == 'O' && data.charAt(1) == 'K'){
//                    MainActivity.showToast(getActivity(), SendData + data);
                    SendDataFlag = false;
                    handler.removeCallbacks(updateDataRunnable);

                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();  // 手动关闭对话框
                    }
                    n=10;
                }

                if (n>0) {
                    n--;
                }
                // 处理接收到的数据
                if (data.startsWith("$GN") && !SendDataFlag && n==0) {
                    if (data.startsWith("$GNDTM,")) {
                        updateCheckBoxAndText(DTMselectCheck, DTMselectText);
                    } else if (data.startsWith("$GNGBS,")) {
                        updateCheckBoxAndText(GBSselectCheck, GBSselectText);
                    } else if (data.startsWith("$GNGGA,")) {
                        updateCheckBoxAndText(GGAselectCheck, GGAselectText);
                    } else if (data.startsWith("$GNGGAH,")) {
                        updateCheckBoxAndText(GGAHselectCheck, GGAHselectText);
                    } else if (data.startsWith("$GNGLL,")) {
                        updateCheckBoxAndText(GLLselectCheck, GLLselectText);
                    } else if (data.startsWith("$GNGLLH,")) {
                        updateCheckBoxAndText(GLLHselectCheck, GLLHselectText);
                    } else if (data.startsWith("$GNGNS,")) {
                        updateCheckBoxAndText(GNSselectCheck, GNSselectText);
                    } else if (data.startsWith("$GNGNSH,")) {
                        updateCheckBoxAndText(GNSHselectCheck, GNSHselectText);
                    } else if (data.startsWith("$GNGST,")) {
                        updateCheckBoxAndText(GSTselectCheck, GSTselectText);
                    } else if (data.startsWith("$GNGSTH,")) {
                        updateCheckBoxAndText(GSTHselectCheck, GSTHselectText);
                    } else if (data.startsWith("$GNTHS,")) {
                        updateCheckBoxAndText(THSselectCheck, THSselectText);
                    } else if (data.startsWith("$GNRMC,")) {
                        updateCheckBoxAndText(RMCselectCheck, RMCselectText);
                    } else if (data.startsWith("$GNRMCH,")) {
                        updateCheckBoxAndText(RMCHselectCheck, RMCHselectText);
                    } else if (data.startsWith("$GNROT,")) {
                        updateCheckBoxAndText(ROTselectCheck, ROTselectText);
                    } else if (data.startsWith("$GNVTG,")) {
                        updateCheckBoxAndText(VTGselectCheck, VTGselectText);
                    } else if (data.startsWith("$GNVTGH,")) {
                        updateCheckBoxAndText(VTGHselectCheck, VTGHselectText);
                    } else if (data.startsWith("$GNZDA,")) {
                        updateCheckBoxAndText(ZDAselectCheck, ZDAselectText);
                    }
                }
//                MainActivity.showToast(getActivity(), data);
            }
            // 封装重复的逻辑到一个函数
            private void updateCheckBoxAndText(CheckBox check, TextView text) {
                check.setOnCheckedChangeListener(null);  // 移除监听器
                check.setChecked(true);  // 设置为选中状态
                if (text != null) {
                    changeTextColor(text, true);  // 修改 TextView 颜色
                }
                check.setOnCheckedChangeListener(CheckBoxCallbackHandler);  // 恢复监听器
            }
        });
    }
}