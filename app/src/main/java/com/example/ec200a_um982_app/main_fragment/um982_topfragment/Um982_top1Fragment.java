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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Um982_top1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Um982_top1Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SharedViewModel viewModel;

    private TextView nmea_dtm;
    private TextView nmea_gbs;
    private TextView nmea_gga;
    private TextView nmea_ggah;
    private TextView nmea_gll;
    private TextView nmea_gllh;
    private TextView nmea_gns;
    private TextView nmea_gnsh;
    private TextView nmea_gst;
    private TextView nmea_gsth;
    private TextView nmea_ths;
    private TextView nmea_rmc;
    private TextView nmea_rmch;
    private TextView nmea_rot;
    private TextView nmea_vtg;
    private TextView nmea_vtgh;
    private TextView nmea_zda;

    public Um982_top1Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Um982_top1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Um982_top1Fragment newInstance(String param1, String param2) {
        Um982_top1Fragment fragment = new Um982_top1Fragment();
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
        View view = inflater.inflate(R.layout.fragment_um982_top1, container, false);

        nmea_dtm = view.findViewById(R.id.nmea_dtm);
        nmea_gbs = view.findViewById(R.id.nmea_gbs);
        nmea_gga = view.findViewById(R.id.nmea_gga);
        nmea_ggah = view.findViewById(R.id.nmea_ggah);
        nmea_gll = view.findViewById(R.id.nmea_gll);
        nmea_gllh = view.findViewById(R.id.nmea_gllh);
        nmea_gns = view.findViewById(R.id.nmea_gns);
        nmea_gnsh = view.findViewById(R.id.nmea_gnsh);
        nmea_gst = view.findViewById(R.id.nmea_gst);
        nmea_gsth = view.findViewById(R.id.nmea_gsth);
        nmea_ths = view.findViewById(R.id.nmea_ths);
        nmea_rmc = view.findViewById(R.id.nmea_rmc);
        nmea_rmch = view.findViewById(R.id.nmea_rmch);
        nmea_rot = view.findViewById(R.id.nmea_rot);
        nmea_vtg = view.findViewById(R.id.nmea_vtg);
        nmea_vtgh = view.findViewById(R.id.nmea_vtgh);
        nmea_zda = view.findViewById(R.id.nmea_zda);

        return view;
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
                    nmea_dtm.setText(data);
                } else if (data != null && data.startsWith("$GNGBS,")) {
                    nmea_gbs.setText(data);
                } else if (data != null && data.startsWith("$GNGGA,")) {
                    nmea_gga.setText(data);
                } else if (data != null && data.startsWith("$GNGGAH,")) {
                    nmea_ggah.setText(data);
                } else if (data != null && data.startsWith("$GNGLL,")) {
                    nmea_gll.setText(data);
                } else if (data != null && data.startsWith("$GNGLLH,")) {
                    nmea_gllh.setText(data);
                } else if (data != null && data.startsWith("$GNGNS,")) {
                    nmea_gns.setText(data);
                } else if (data != null && data.startsWith("$GNGNSH,")) {
                    nmea_gnsh.setText(data);
                } else if (data != null && data.startsWith("$GNGRS,")) {

                } else if (data != null && data.startsWith("$GNGRSH,")) {

                } else if (data != null && data.startsWith("$GNGSA,")) {

                } else if (data != null && data.startsWith("$GNGSAH,")) {

                } else if (data != null && data.startsWith("$GNGST,")) {
                    nmea_gst.setText(data);
                } else if (data != null && data.startsWith("$GNGSTH,")) {
                    nmea_gsth.setText(data);
                } else if (data != null && data.startsWith("$GNGSV,")) {

                } else if (data != null && data.startsWith("$GNGSVH,")) {

                } else if (data != null && data.startsWith("$GNTHS,")) {
                    nmea_ths.setText(data);
                } else if (data != null && data.startsWith("$GNRMC,")) {
                    nmea_rmc.setText(data);
                } else if (data != null && data.startsWith("$GNRMCH,")) {
                    nmea_rmch.setText(data);
                } else if (data != null && data.startsWith("$GNROT,")) {
                    nmea_rot.setText(data);
                } else if (data != null && data.startsWith("$GNVTG,")) {
                    nmea_vtg.setText(data);
                } else if (data != null && data.startsWith("$GNVTGH,")) {
                    nmea_vtgh.setText(data);
                } else if (data != null && data.startsWith("$GNZDA,")) {
                    nmea_zda.setText(data);
                }
            }
        });
    }
}