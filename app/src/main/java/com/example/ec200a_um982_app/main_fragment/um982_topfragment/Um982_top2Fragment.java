package com.example.ec200a_um982_app.main_fragment.um982_topfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ec200a_um982_app.MainActivity;
import com.example.ec200a_um982_app.R;
import com.example.ec200a_um982_app.SharedViewModel;
import com.example.ec200a_um982_app.SocketService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Um982_top2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Um982_top2Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private SharedViewModel viewModel1;

    TextView DatumCodeText;
    TextView LatOffsetText;
    TextView LonOffsetText;
    TextView AltOffsetText;
    TextView RfDatumCodeText;
    TextView UTCText;
    TextView LatExpText;
    TextView LonExpText;
    TextView AltExpText;
    TextView LatText;
    TextView LatDirText;
    TextView LonText;
    TextView LonDirText;
    TextView RtkModeText;
    TextView SatsText;
    TextView hdopText;
    TextView AltText;
    TextView a_unitsText;
    TextView DiffDataAgeText;
    TextView stnIDText;
    TextView statusText;
    TextView TrackTrueText;
    TextView speedText;
    TextView dateText;
    TextView magVarText;
    TextView varDirText;
    TextView rateText;
    TextView HeadingText;

    String DatumCode="";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Um982_top2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Um982_top2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Um982_top2Fragment newInstance(String param1, String param2) {
        Um982_top2Fragment fragment = new Um982_top2Fragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_um982_top2, container, false);

        DatumCodeText = view.findViewById(R.id.DatumCodeText);
        LatOffsetText = view.findViewById(R.id.LatOffsetText);
        LonOffsetText = view.findViewById(R.id.LonOffsetText);
        AltOffsetText = view.findViewById(R.id.AltOffsetText);
        RfDatumCodeText = view.findViewById(R.id.RfDatumCodeText);
        UTCText = view.findViewById(R.id.UTCText);
        LatExpText = view.findViewById(R.id.LatExpText);
        LonExpText = view.findViewById(R.id.LonExpText);
        AltExpText = view.findViewById(R.id.AltExpText);
        LatText = view.findViewById(R.id.LatText);
        LatDirText = view.findViewById(R.id.LatDirText);
        LonText = view.findViewById(R.id.LonText);
        LonDirText = view.findViewById(R.id.LonDirText);
        RtkModeText = view.findViewById(R.id.RtkModeText);
        SatsText = view.findViewById(R.id.SatsText);
        hdopText = view.findViewById(R.id.hdopText);
        AltText = view.findViewById(R.id.AltText);
        a_unitsText = view.findViewById(R.id.a_unitsText);
        DiffDataAgeText = view.findViewById(R.id.DiffDataAgeText);
        stnIDText = view.findViewById(R.id.stnIDText);
        statusText = view.findViewById(R.id.statusText);
        TrackTrueText = view.findViewById(R.id.TrackTrueText);
        speedText = view.findViewById(R.id.speedText);
        dateText = view.findViewById(R.id.dateText);
        magVarText = view.findViewById(R.id.magVarText);
        varDirText = view.findViewById(R.id.varDirText);
        rateText = view.findViewById(R.id.rateText);
        HeadingText = view.findViewById(R.id.HeadingText);


        return view;
    }

    public static List<String> getSpecificSubstrings(String input, int[] indices) {
        List<String> result = new ArrayList<>();

        if (input == null || indices == null) {
            return result; // 返回空列表
        }

        String[] parts = input.split(",");

        for (int index : indices) {
            if (index > 0 && index <= parts.length) {
                result.add(parts[index - 1]); // 索引从 1 开始
            }
        }

        return result; // 返回提取的部分
    }

    private StringBuilder dataBuffer = new StringBuilder();
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel1 = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel1.getDataGroup1().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String data) {
                // 将接收到的数据添加到缓冲区
                dataBuffer.append(data);

                // 检查是否包含 \r\n
                while (dataBuffer.indexOf("\r\n") != -1) {
                    String frames = dataBuffer.toString();
                    String[] messageArray = frames.split("::");
                    // 遍历输出拆分后的消息
                    for (String message : messageArray) {
                        // 处理接收到的数据
                        if (message != null && message.startsWith("$GNDTM,")) {
                            int[] indices = {2, 4, 6,8,9};
                            List<String> results = getSpecificSubstrings(message, indices);

                            DatumCodeText.setText("本地坐标系代码: " + results.get(0));
                            LatOffsetText.setText("纬度偏移量: " + results.get(1) + "′");
                            LonOffsetText.setText("经度偏移量: " + results.get(2) + "′");
                            AltOffsetText.setText("海拔偏移量: " + results.get(3) + " m");
                            RfDatumCodeText.setText("参考坐标系代码: " + results.get(4).substring(0,3));
                        } else if (message != null && message.startsWith("$GNGBS,")) {
                            int[] indices = {2,3,4,5};
                            List<String> results = getSpecificSubstrings(message, indices);

                            UTCText.setText("UTC时间: " + results.get(0));
                            LatExpText.setText("纬度预期误差: " + results.get(1) + " m");
                            LonExpText.setText("经度预期误差: " + results.get(2) + " m");
                            AltExpText.setText("海拔预期误差: " + results.get(3) + " m");
                        } else if (message != null && message.startsWith("$GNGGA,")) {
                            int[] indices = {3,4,5,6,7,8,9,10,11,14,15};
                            List<String> results = getSpecificSubstrings(message, indices);

                            LatText.setText("纬度: " + results.get(0));
                            LatDirText.setText("纬度方向: " + results.get(1));
                            LonText.setText("经度: " + results.get(2));
                            LonDirText.setText("经度方向: " + results.get(3));
                            if(Objects.equals(results.get(4), "0"))
                                RtkModeText.setText("定位模式: " + "定位无效");
                            else if(Objects.equals(results.get(4), "1"))
                                RtkModeText.setText("定位模式: " + "单点定位");
                            else if(Objects.equals(results.get(4), "2"))
                                RtkModeText.setText("定位模式: " + "差分定位");
                            else if(Objects.equals(results.get(4), "3"))
                                RtkModeText.setText("定位模式: " + "GPS PPS 模式");
                            else if(Objects.equals(results.get(4), "4"))
                                RtkModeText.setText("定位模式: " + "固定RTK模式");
                            else if(Objects.equals(results.get(4), "5"))
                                RtkModeText.setText("定位模式: " + "浮动RTK模式");
                            else if(Objects.equals(results.get(4), "6"))
                                RtkModeText.setText("定位模式: " + "惯导模式");
                            else if(Objects.equals(results.get(4), "7"))
                                RtkModeText.setText("定位模式: " + "手动输入模式");
                            else if(Objects.equals(results.get(4), "8"))
                                RtkModeText.setText("定位模式: " + "模拟器模式");

                            SatsText.setText("使用中的卫星数: " + results.get(5));
                            hdopText.setText("水平精度因子: " + results.get(6));
                            AltText.setText("海拔高度: " + results.get(7));
                            a_unitsText.setText("海拔高度单位: " + results.get(8));
                            DiffDataAgeText.setText("差分数据龄期: " + results.get(9));
                            if(results.get(10).length()>3) {
                                stnIDText.setText("差分基站ID: " + results.get(10).substring(0,3));
                            }

                        } else if (message != null && message.startsWith("$GNGGAH,")) {

                        } else if (message != null && message.startsWith("$GNGLL,")) {

                        } else if (message != null && message.startsWith("$GNGLLH,")) {

                        } else if (message != null && message.startsWith("$GNGNS,")) {

                        } else if (message != null && message.startsWith("$GNGNSH,")) {

                        } else if (message != null && message.startsWith("$GNGRS,")) {

                        } else if (message != null && message.startsWith("$GNGRSH,")) {

                        } else if (message != null && message.startsWith("$GNGSA,")) {

                        } else if (message != null && message.startsWith("$GNGSAH,")) {

                        } else if (message != null && message.startsWith("$GNGST,")) {

                        } else if (message != null && message.startsWith("$GNGSTH,")) {

                        } else if (message != null && message.startsWith("$GNGSV,")) {

                        } else if (message != null && message.startsWith("$GNGSVH,")) {

                        } else if (message != null && message.startsWith("$GNTHS,")) {
                            int[] indices = {2};
                            List<String> results = getSpecificSubstrings(message, indices);

                            HeadingText.setText("天线方向: " + results.get(0));
                        } else if (message != null && message.startsWith("$GNRMC,")) {
                            int[] indices = {14,8,9,10,11,12};
                            List<String> results = getSpecificSubstrings(message, indices);

                            statusText.setText("导航状态指示: " + results.get(0).substring(0,1));
                            float speed = Float.parseFloat(results.get(1));
                            speedText.setText("地面速率: " + speed*1.852 + "km/h\r\n\t" + speed*0.5144 + "m/s");
                            TrackTrueText.setText("地面航向: " + results.get(2));
                            dateText.setText("日期: " + results.get(3));
                            magVarText.setText("磁偏角: " + results.get(4));
                            varDirText.setText("磁偏角方向: " + results.get(5));
                        } else if (message != null && message.startsWith("$GNRMCH,")) {

                        } else if (message != null && message.startsWith("$GNROT,")) {
                            int[] indices = {2};
                            List<String> results = getSpecificSubstrings(message, indices);

                            rateText.setText("旋转速率: " + results.get(0) + "度/分");
                        } else if (message != null && message.startsWith("$GNVTG,")) {

                        } else if (message != null && message.startsWith("$GNVTGH,")) {

                        } else if (message != null && message.startsWith("$GNZDA,")) {

                        }
                    }
                    dataBuffer.setLength(0);
                }
            }
        });
    }
}