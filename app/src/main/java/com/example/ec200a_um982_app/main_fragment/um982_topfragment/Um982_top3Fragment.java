package com.example.ec200a_um982_app.main_fragment.um982_topfragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.ec200a_um982_app.MainActivity;
import com.example.ec200a_um982_app.R;
import com.example.ec200a_um982_app.SharedViewModel;
import com.example.ec200a_um982_app.SocketService;
import com.example.ec200a_um982_app.main_fragment.BluetoothFragment;
import com.example.ec200a_um982_app.main_fragment.bluetooth_topfragment.Bluetooth_top1Fragment;

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

    // 创建一个 String 数组来保存每个 TextView 对应的详细信息
    final String[] textDetails = {
            "$GNDTM 坐标信息\n" +
                    "\n" +
                    "\u3000\u3000" + "本指令用于输出大地坐标系信息。\n" +
                    "\u3000\u3000" + "该指令提供了与大地坐标系（如 WGS84、NAD83 等）相关的参数，包括纬度、经度、原点坐标、比例因子和偏移量等。",
            "$GNGBS GPS故障检测信息\n" +
                    "\n" +
                    "\u3000\u3000" + "本指令用于输出GPS故障检测信息。\n" +
                    "\u3000\u3000" + "该指令提供了关于GPS定位精度和可靠性的详细信息，包括位置误差、速度误差、使用的卫星数量等。它帮助评估当前GPS信号的质量，特别是在定位存在问题或误差时非常有用。",
            "$GNGGA 卫星定位信息\n" +
                    "\n" +
                    "\u3000\u3000" + "本指令用于输出GPS卫星定位信息。\n" +
                    "\u3000\u3000" + "该指令提供了GPS接收器获取的当前定位信息，包括时间戳、定位质量、使用的卫星数量、水平精度因子（HDOP）、天线高度等关键信息。它通常用于监控和获取定位精度以及卫星状态。",
            "$GNGGAH 从天线卫星定位信息\n" +
                    "\n" +
                    "\u3000\u3000" + "本指令用于输出GPS卫星定位信息。\n" +
                    "\u3000\u3000" + "该指令提供了GPS接收器从天线获取的当前定位信息，包括时间戳、定位质量、使用的卫星数量、水平精度因子（HDOP）、天线高度等关键信息。它通常用于监控和获取定位精度以及卫星状态。",
            "$GNGLL 地理位置信息\n" +
                    "\n" +
                    "\u3000\u3000" + "本指令用于输出地理位置信息。\n" +
                    "\u3000\u3000" + "该指令提供了当前GPS位置的纬度、经度、定位状态以及UTC时间等信息，帮助用户了解当前的位置以及定位的有效性。",
            "$GNGLLH 从天线地理位置信息\n" +
                    "\n" +
                    "\u3000\u3000" + "本指令用于输出从天线地理位置信息。\n" +
                    "\u3000\u3000" + "该指令提供了当前GPS从天线位置的纬度、经度、定位状态以及UTC时间等信息，帮助用户了解当前的从天线位置以及定位的有效性。",
            "$GNGNS 定位数据输出\n" +
                    "\n" +
                    "\u3000\u3000" + "本指令用于输出GPS定位数据。\n" +
                    "\u3000\u3000" + "该指令提供了GPS接收器的定位状态、使用的卫星数量、当前位置的经纬度、定位精度等信息，帮助用户获取当前位置的详细定位数据。",
            "$GNGNSH 从天线定位数据输出\n" +
                    "\n" +
                    "\u3000\u3000" + "本指令用于输出GPS从天线定位数据。\n" +
                    "\u3000\u3000" + "该指令提供了GPS接收器从天线的定位状态、使用的卫星数量、当前从天线位置的经纬度、定位精度等信息，帮助用户获取当前从天线位置的详细定位数据。",
            "$GNGST 伪距观测误差信息\n" +
                    "\n" +
                    "\u3000\u3000" + "本指令用于输出伪距观测误差信息。\n" +
                    "\u3000\u3000" + "该指令提供了与伪距观测相关的误差信息，主要用于描述GPS系统定位过程中可能出现的误差类型。伪距是从接收机到卫星的距离测量，它可能受到多个因素（如大气延迟、卫星钟差等）的影响。$GNGST 消息帮助用户评估定位精度和误差。",
            "$GNGSTH 从天线伪距观测误差信息\n" +
                    "\n" +
                    "\u3000\u3000" + "本指令用于输出从天线伪距观测误差信息。\n" +
                    "\u3000\u3000" + "该指令提供了与从天线伪距观测相关的误差信息，主要用于描述GPS系统定位过程中可能出现的误差类型。伪距是从接收机到卫星的距离测量，它可能受到多个因素（如大气延迟、卫星钟差等）的影响。$GNGSTH 消息帮助用户评估定位精度和误差。",
            "$GNTHS 航向信息\n" +
                    "\u3000\u3000" + "本指令用于输出航向、状态等信息。",
            "$GNRMC 卫星定位信息\n" +
                    "\n" +
                    "\u3000\u3000" + "本指令用于输出GPS卫星定位信息。\n" +
                    "\u3000\u3000" + "该指令提供了当前位置的经纬度、速度、航向、日期以及定位状态等关键信息，通常用于导航系统和位置追踪。它是 GPS 定位系统中常见的一种信息格式，广泛应用于航海、航空和汽车导航等领域。",
            "$GNRMCH 从天线卫星定位信息\n" +
                    "\n" +
                    "\u3000\u3000" + "本指令用于输出GPS卫星从天线定位信息。\n" +
                    "\u3000\u3000" + "该指令提供了当前从天线位置的经纬度、速度、航向、日期以及定位状态等关键信息，通常用于导航系统和位置追踪。它是 GPS 定位系统中常见的一种信息格式，广泛应用于航海、航空和汽车导航等领域。",
            "$GNROT 旋转速度和方向信息\n" +
                    "\n" +
                    "\u3000\u3000" + "本指令用于输出旋转速度和方向信息。\n" +
                    "\u3000\u3000" + "该指令提供了与 旋转速率 和 航向变化方向 相关的实时数据。它通常用于描述物体在三维空间中的旋转状态，广泛应用于高精度导航、航空、船舶和其他需要跟踪物体旋转状态的领域。",
            "$GNVTG 地面航向与速度信息\n" +
                    "\n" +
                    "\u3000\u3000" + "本指令用于输出地面航向与速度信息。\n" +
                    "\u3000\u3000" + "该指令提供了地面航向、地面速度以及航向变化方向等与运动相关的重要数据。$GNVTG 指令通常用于导航系统中，帮助用户获得当前运动方向和速度，尤其在航海、航空和陆地导航等应用中非常重要。",
            "$GNVTGH 从天线地面航向与速度信息\n" +
                    "\n" +
                    "\u3000\u3000" + "本指令用于输出从天线地面航向与速度信息。\n" +
                    "\u3000\u3000" + "该指令提供了从天线地面航向、地面速度以及航向变化方向等与运动相关的重要数据。$GNVTGH 指令通常用于导航系统中，帮助用户获得当前运动方向和速度，尤其在航海、航空和陆地导航等应用中非常重要。",
            "$GNZDA 日期和时间\n" +
                    "\u3000\u3000" + "本指令用于输出 UTC 时间和日期信息。"
    };

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

    Button SaveTheRTKSettingButtun;

    String SendData = "";
    boolean SendDataFlag = false;

    int n=0;

    CheckBox CheckBoxIndex;
    TextView associatedTextView = null;

    SpannableString spannableString= SpannableString.valueOf("");

    int stringArrayIntex=0;
    // 用来保存数据输出频率
    String[] stringArray = {"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"};

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

        SaveTheRTKSettingButtun = view.findViewById(R.id.SaveTheRTKSettingButtun);

        // 保存按钮的点击事件
        SaveTheRTKSettingButtun.setOnClickListener(v -> {
            if (MainActivity.getBluetoothConFlag()) {
                SendData = "AT+UM982=" + "SAVECONFIG" + "\r\n";
                SendDataFlag = true;

                handler.post(updateDataRunnable);

                n=1;
            } else {
                MainActivity.showToast(getActivity(), "请连接蓝牙");
            }
        });

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
        // 设置每个TextView可点击
        DTMselectText.setClickable(true);
        GBSselectText.setClickable(true);
        GGAselectText.setClickable(true);
        spannableString = new SpannableString("GNGGA  1Hz");
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 5, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        GGAselectText.setText(spannableString);

        GGAHselectText.setClickable(true);
        GLLselectText.setClickable(true);
        GLLHselectText.setClickable(true);
        GNSselectText.setClickable(true);
        GNSHselectText.setClickable(true);
        GSTselectText.setClickable(true);
        GSTHselectText.setClickable(true);
        THSselectText.setClickable(true);
        spannableString = new SpannableString("GNTHS  1Hz");
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 5, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        THSselectText.setText(spannableString);

        RMCselectText.setClickable(true);
        spannableString = new SpannableString("GNRMC  1Hz");
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 5, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        RMCselectText.setText(spannableString);

        RMCHselectText.setClickable(true);
        ROTselectText.setClickable(true);
        VTGselectText.setClickable(true);
        spannableString = new SpannableString("GNVTG  1Hz");
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 5, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        VTGselectText.setText(spannableString);

        VTGHselectText.setClickable(true);
        ZDAselectText.setClickable(true);
        // 为每个TextView设置相同的点击监听器
        DTMselectText.setOnClickListener(clickListenerCallbackHandler);
        GBSselectText.setOnClickListener(clickListenerCallbackHandler);
        GGAselectText.setOnClickListener(clickListenerCallbackHandler);
        GGAHselectText.setOnClickListener(clickListenerCallbackHandler);
        GLLselectText.setOnClickListener(clickListenerCallbackHandler);
        GLLHselectText.setOnClickListener(clickListenerCallbackHandler);
        GNSselectText.setOnClickListener(clickListenerCallbackHandler);
        GNSHselectText.setOnClickListener(clickListenerCallbackHandler);
        GSTselectText.setOnClickListener(clickListenerCallbackHandler);
        GSTHselectText.setOnClickListener(clickListenerCallbackHandler);
        THSselectText.setOnClickListener(clickListenerCallbackHandler);
        RMCselectText.setOnClickListener(clickListenerCallbackHandler);
        RMCHselectText.setOnClickListener(clickListenerCallbackHandler);
        ROTselectText.setOnClickListener(clickListenerCallbackHandler);
        VTGselectText.setOnClickListener(clickListenerCallbackHandler);
        VTGHselectText.setOnClickListener(clickListenerCallbackHandler);
        ZDAselectText.setOnClickListener(clickListenerCallbackHandler);

        // 为每个TextView设置相同的长按监听器
        DTMselectText.setOnLongClickListener(setOnLongClickListenerCallbackHandler);
        GBSselectText.setOnLongClickListener(setOnLongClickListenerCallbackHandler);
        GGAselectText.setOnLongClickListener(setOnLongClickListenerCallbackHandler);
        GGAHselectText.setOnLongClickListener(setOnLongClickListenerCallbackHandler);
        GLLselectText.setOnLongClickListener(setOnLongClickListenerCallbackHandler);
        GLLHselectText.setOnLongClickListener(setOnLongClickListenerCallbackHandler);
        GNSselectText.setOnLongClickListener(setOnLongClickListenerCallbackHandler);
        GNSHselectText.setOnLongClickListener(setOnLongClickListenerCallbackHandler);
        GSTselectText.setOnLongClickListener(setOnLongClickListenerCallbackHandler);
        GSTHselectText.setOnLongClickListener(setOnLongClickListenerCallbackHandler);
        THSselectText.setOnLongClickListener(setOnLongClickListenerCallbackHandler);
        RMCselectText.setOnLongClickListener(setOnLongClickListenerCallbackHandler);
        RMCHselectText.setOnLongClickListener(setOnLongClickListenerCallbackHandler);
        ROTselectText.setOnLongClickListener(setOnLongClickListenerCallbackHandler);
        VTGselectText.setOnLongClickListener(setOnLongClickListenerCallbackHandler);
        VTGHselectText.setOnLongClickListener(setOnLongClickListenerCallbackHandler);
        ZDAselectText.setOnLongClickListener(setOnLongClickListenerCallbackHandler);


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
                        Bluetooth_top1Fragment.characteristic.setValue(SendData);
                        Bluetooth_top1Fragment.bluetoothGatt.writeCharacteristic(Bluetooth_top1Fragment.characteristic);
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

    /**
     * 根据当前值更新数组中的元素
     *
     * @param stringArray 目标字符串数组
     * @param index       要更新的数组索引
     */
    public static void updateStringArray(String[] stringArray, int index) {
        // 判断stringArray[index]当前的值并进行更新
        if (stringArray[index].equals("1")) {
            stringArray[index] = "0.2";  // 5Hz
        } else if (stringArray[index].equals("0.2")) {
            stringArray[index] = "0.1";  // 10Hz
        } else if (stringArray[index].equals("0.1")) {
            stringArray[index] = "1";  // 1Hz
        }
    }
    // 创建统一的点击监听器
    View.OnClickListener clickListenerCallbackHandler = new View.OnClickListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {
            TextView clickedTextView = (TextView) v;

            if (v.getId() == R.id.DTMselectText) {
                clickedTextView.setText("GNDTM  1Hz");
            } else if (v.getId() == R.id.GBSselectText) {
                clickedTextView.setText("GNGBS  1Hz");
            } else if (v.getId() == R.id.GGAselectText) {
                // 假设 stringArray 已经定义并初始化
                updateStringArray(stringArray,2);
                clickedTextView.setText("GNGGA  " + (int)(1/Double.parseDouble(stringArray[2])) + "Hz");
                spannableString = new SpannableString("GNGGA  " + (int)(1/Double.parseDouble(stringArray[2])) + "Hz");
                spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 5, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                GGAselectText.setText(spannableString);
            } else if (v.getId() == R.id.GGAHselectText) {
                clickedTextView.setText("GNGGAH  1Hz");
            } else if (v.getId() == R.id.GLLselectText) {
                clickedTextView.setText("GNGLL  1Hz");
            } else if (v.getId() == R.id.GLLHselectText) {
                clickedTextView.setText("GNGLLH  1Hz");
            } else if (v.getId() == R.id.GNSselectText) {
                clickedTextView.setText("GNGNS  1Hz");
            } else if (v.getId() == R.id.GNSHselectText) {
                clickedTextView.setText("GNGNSH  1Hz");
            } else if (v.getId() == R.id.GSTselectText) {
                clickedTextView.setText("GNGST  1Hz");
            } else if (v.getId() == R.id.GSTHselectText) {
                clickedTextView.setText("GNGSTH  1Hz");
            } else if (v.getId() == R.id.THSselectText) {
                updateStringArray(stringArray,10);
                clickedTextView.setText("GNTHS  " + (int)(1/Double.parseDouble(stringArray[10])) + "Hz");
                spannableString = new SpannableString("GNTHS  " + (int)(1/Double.parseDouble(stringArray[10])) + "Hz");
                spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 5, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                THSselectText.setText(spannableString);
            } else if (v.getId() == R.id.RMCselectText) {
                updateStringArray(stringArray,11);
                clickedTextView.setText("GNRMC  " + (int)(1/Double.parseDouble(stringArray[11])) + "Hz");
                spannableString = new SpannableString("GNRMC  " + (int)(1/Double.parseDouble(stringArray[11])) + "Hz");
                spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 5, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                RMCselectText.setText(spannableString);
            } else if (v.getId() == R.id.RMCHselectText) {
                clickedTextView.setText("GNRMCH  1Hz");
            } else if (v.getId() == R.id.ROTselectText) {
                clickedTextView.setText("GNROT  1Hz");
            } else if (v.getId() == R.id.VTGselectText) {
                updateStringArray(stringArray,14);
                clickedTextView.setText("GNVTG  " + (int)(1/Double.parseDouble(stringArray[14])) + "Hz");
                spannableString = new SpannableString("GNVTG  " + (int)(1/Double.parseDouble(stringArray[14])) + "Hz");
                spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 5, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                VTGselectText.setText(spannableString);
            } else if (v.getId() == R.id.VTGHselectText) {
                clickedTextView.setText("GNVTGH  1Hz");
            } else if (v.getId() == R.id.ZDAselectText) {
                clickedTextView.setText("GNZDA  1Hz");
            }
        }
    };


    // 创建一个方法来显示包含详细信息的 AlertDialog
    private void showDetailsDialog(View view, String text) {
        // 构造提示框的标题和内容
        String title = "详细信息";

        // 创建并显示 AlertDialog
        new AlertDialog.Builder(view.getContext())
                .setTitle(title)  // 设置对话框标题
                .setMessage(text)  // 设置对话框消息内容
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击确定按钮时的操作
                        dialog.dismiss();  // 关闭对话框
                    }
                })
                .setCancelable(true)  // 允许点击外部区域关闭对话框
                .show();  // 显示对话框
    }
    // 创建一个 OnLongClickListener 实现
    View.OnLongClickListener setOnLongClickListenerCallbackHandler = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            TextView LongClickTextView = (TextView) v;

            // 获取长按的 TextView 索引
            int index = getTextViewIndex(v);

            if (index != -1) {
                // 根据索引获取详细信息
                String details = textDetails[index];
                // 创建并显示 AlertDialog
                showDetailsDialog(v, details);
            }
            // 返回 true 表示事件已处理
            return true;
        }

        // 根据 View 获取其对应的索引
        private int getTextViewIndex(View v) {
            // 根据 View 的 ID 判断是哪一个 TextView
            if (v.getId() == R.id.DTMselectText) {
                return 0;
            } else if (v.getId() == R.id.GBSselectText) {
                return 1;
            } else if (v.getId() == R.id.GGAselectText) {
                return 2;
            } else if (v.getId() == R.id.GGAHselectText) {
                return 3;
            } else if (v.getId() == R.id.GLLselectText) {
                return 4;
            } else if (v.getId() == R.id.GLLHselectText) {
                return 5;
            } else if (v.getId() == R.id.GNSselectText) {
                return 6;
            } else if (v.getId() == R.id.GNSHselectText) {
                return 7;
            } else if (v.getId() == R.id.GSTselectText) {
                return 8;
            } else if (v.getId() == R.id.GSTHselectText) {
                return 9;
            } else if (v.getId() == R.id.THSselectText) {
                return 10;
            } else if (v.getId() == R.id.RMCselectText) {
                return 11;
            } else if (v.getId() == R.id.RMCHselectText) {
                return 12;
            } else if (v.getId() == R.id.ROTselectText) {
                return 13;
            } else if (v.getId() == R.id.VTGselectText) {
                return 14;
            } else if (v.getId() == R.id.VTGHselectText) {
                return 15;
            } else if (v.getId() == R.id.ZDAselectText) {
                return 16;
            }
            return -1;
        }
    };
    // 统一的 CheckBox 状态变化监听器
    private final CompoundButton.OnCheckedChangeListener CheckBoxCallbackHandler = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int id = buttonView.getId();


            if (id == R.id.DTMselectCheck) {
                stringArrayIntex=0;
                CheckBoxIndex = DTMselectCheck;
                associatedTextView = DTMselectText;
            } else if (id == R.id.GBSselectCheck) {
                stringArrayIntex=1;
                CheckBoxIndex = GBSselectCheck;
                associatedTextView = GBSselectText;
            } else if (id == R.id.GGAselectCheck) {
                stringArrayIntex=2;
                CheckBoxIndex = GGAselectCheck;
                associatedTextView = GGAselectText;
            } else if (id == R.id.GGAHselectCheck) {
                stringArrayIntex=3;
                CheckBoxIndex = GGAHselectCheck;
                associatedTextView = GGAHselectText;
            } else if (id == R.id.GLLselectCheck) {
                stringArrayIntex=4;
                CheckBoxIndex = GLLselectCheck;
                associatedTextView = GLLselectText;
            } else if (id == R.id.GLLHselectCheck) {
                stringArrayIntex=5;
                CheckBoxIndex = GLLHselectCheck;
                associatedTextView = GLLHselectText;
            } else if (id == R.id.GNSselectCheck) {
                stringArrayIntex=6;
                CheckBoxIndex = GNSselectCheck;
                associatedTextView = GNSselectText;
            } else if (id == R.id.GNSHselectCheck) {
                stringArrayIntex=7;
                CheckBoxIndex = GNSHselectCheck;
                associatedTextView = GNSHselectText;
            } else if (id == R.id.GSTselectCheck) {
                stringArrayIntex=8;
                CheckBoxIndex = GSTselectCheck;
                associatedTextView = GSTselectText;
            } else if (id == R.id.GSTHselectCheck) {
                stringArrayIntex=9;
                CheckBoxIndex = GSTHselectCheck;
                associatedTextView = GSTHselectText;
            } else if (id == R.id.THSselectCheck) {
                stringArrayIntex=10;
                CheckBoxIndex = THSselectCheck;
                associatedTextView = THSselectText;
            } else if (id == R.id.RMCselectCheck) {
                stringArrayIntex=11;
                CheckBoxIndex = RMCselectCheck;
                associatedTextView = RMCselectText;
            } else if (id == R.id.RMCHselectCheck) {
                stringArrayIntex=12;
                CheckBoxIndex = RMCHselectCheck;
                associatedTextView = RMCHselectText;
            } else if (id == R.id.ROTselectCheck) {
                stringArrayIntex=13;
                CheckBoxIndex = ROTselectCheck;
                associatedTextView = ROTselectText;
            } else if (id == R.id.VTGselectCheck) {
                stringArrayIntex=14;
                CheckBoxIndex = VTGselectCheck;
                associatedTextView = VTGselectText;
            } else if (id == R.id.VTGHselectCheck) {
                stringArrayIntex=15;
                CheckBoxIndex = VTGHselectCheck;
                associatedTextView = VTGHselectText;
            } else if (id == R.id.ZDAselectCheck) {
                stringArrayIntex=16;
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
        result = result.replaceFirst("N", "P");  // 将所有 N 替换为 P


        if (isChecked) {
            SendData = "AT+UM982=" + result + " " + stringArray[stringArrayIntex] + "\r\n";
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

    private StringBuilder dataBuffer = new StringBuilder();
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel1 = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel1.getDataGroup1().observe(getViewLifecycleOwner(), new Observer<String>() {

            @Override
            public void onChanged(String data) {
                if (data.contains("OK"))
//                if (index != -1 && index < data.length() - 1 && data.charAt(index + 1) == 'K')
                {
//                    MainActivity.showToast(getActivity(),"data");
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

                // 将接收到的数据添加到缓冲区
                dataBuffer.append(data);

                // 检查是否包含 \r\n
                while (dataBuffer.indexOf("\r\n") != -1) {
                    String frames = dataBuffer.toString();
                    String[] messageArray = frames.split("::");
                    // 遍历输出拆分后的消息
                    for (String message : messageArray) {
                        // 处理接收到的数据
                        if (message.startsWith("$GN") && !SendDataFlag && n==0) {
                            if (message.startsWith("$GNDTM,")) {
                                updateCheckBoxAndText(DTMselectCheck, DTMselectText);
                            } else if (message.startsWith("$GNGBS,")) {
                                updateCheckBoxAndText(GBSselectCheck, GBSselectText);
                            } else if (message.startsWith("$GNGGA,")) {
                                updateCheckBoxAndText(GGAselectCheck, GGAselectText);
                            } else if (message.startsWith("$GNGGAH,")) {
                                updateCheckBoxAndText(GGAHselectCheck, GGAHselectText);
                            } else if (message.startsWith("$GNGLL,")) {
                                updateCheckBoxAndText(GLLselectCheck, GLLselectText);
                            } else if (message.startsWith("$GNGLLH,")) {
                                updateCheckBoxAndText(GLLHselectCheck, GLLHselectText);
                            } else if (message.startsWith("$GNGNS,")) {
                                updateCheckBoxAndText(GNSselectCheck, GNSselectText);
                            } else if (message.startsWith("$GNGNSH,")) {
                                updateCheckBoxAndText(GNSHselectCheck, GNSHselectText);
                            } else if (message.startsWith("$GNGST,")) {
                                updateCheckBoxAndText(GSTselectCheck, GSTselectText);
                            } else if (message.startsWith("$GNGSTH,")) {
                                updateCheckBoxAndText(GSTHselectCheck, GSTHselectText);
                            } else if (message.startsWith("$GNTHS,")) {
                                updateCheckBoxAndText(THSselectCheck, THSselectText);
                            } else if (message.startsWith("$GNRMC,")) {
                                updateCheckBoxAndText(RMCselectCheck, RMCselectText);
                            } else if (message.startsWith("$GNRMCH,")) {
                                updateCheckBoxAndText(RMCHselectCheck, RMCHselectText);
                            } else if (message.startsWith("$GNROT,")) {
                                updateCheckBoxAndText(ROTselectCheck, ROTselectText);
                            } else if (message.startsWith("$GNVTG,")) {
                                updateCheckBoxAndText(VTGselectCheck, VTGselectText);
                            } else if (message.startsWith("$GNVTGH,")) {
                                updateCheckBoxAndText(VTGHselectCheck, VTGHselectText);
                            } else if (message.startsWith("$GNZDA,")) {
                                updateCheckBoxAndText(ZDAselectCheck, ZDAselectText);
                            }
                        }
                    }
                    dataBuffer.setLength(0);
                }
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