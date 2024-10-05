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

import java.util.ArrayList;
import java.util.List;

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


    private SharedViewModel viewModel;

    TextView DatumCodeText;
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
                result.add(parts[index]); // 索引从 1 开始
            }
        }

        return result; // 返回提取的部分
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String data) {
                // 处理接收到的数据
                if (data != null && data.startsWith("$GNDTM,")) {
                    // 示例：提取第 1、3 和 6 部分
                    int[] indices = {1, 3, 6};
                    List<String> results = getSpecificSubstrings(data, indices);
                    
                    DatumCodeText.setText("本地坐标系代码: " + results.get(0));
                } else if (data != null && data.startsWith("$GNGBS,")) {

                } else if (data != null && data.startsWith("$GNGGA,")) {

                } else if (data != null && data.startsWith("$GNGGAH,")) {

                } else if (data != null && data.startsWith("$GNGLL,")) {

                } else if (data != null && data.startsWith("$GNGLLH,")) {

                } else if (data != null && data.startsWith("$GNGNS,")) {

                } else if (data != null && data.startsWith("$GNGNSH,")) {

                } else if (data != null && data.startsWith("$GNGRS,")) {

                } else if (data != null && data.startsWith("$GNGRSH,")) {

                } else if (data != null && data.startsWith("$GNGSA,")) {

                } else if (data != null && data.startsWith("$GNGSAH,")) {

                } else if (data != null && data.startsWith("$GNGST,")) {

                } else if (data != null && data.startsWith("$GNGSTH,")) {

                } else if (data != null && data.startsWith("$GNGSV,")) {

                } else if (data != null && data.startsWith("$GNGSVH,")) {

                } else if (data != null && data.startsWith("$GNTHS,")) {

                } else if (data != null && data.startsWith("$GNRMC,")) {

                } else if (data != null && data.startsWith("$GNRMCH,")) {

                } else if (data != null && data.startsWith("$GNROT,")) {

                } else if (data != null && data.startsWith("$GNVTG,")) {

                } else if (data != null && data.startsWith("$GNVTGH,")) {

                } else if (data != null && data.startsWith("$GNZDA,")) {

                }
            }
        });
    }
}