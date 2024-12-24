package com.example.ec200a_um982_app.main_fragment.um982_topfragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ec200a_um982_app.MainActivity;
import com.example.ec200a_um982_app.R;
import com.example.ec200a_um982_app.SharedViewModel;
import com.example.ec200a_um982_app.SocketService;
import com.example.ec200a_um982_app.main_fragment.Um982Fragment;

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

    private SharedViewModel viewModel1;
    private SharedViewModel viewModel2;

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

//        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
//        Button nimaButton = view.findViewById(R.id.nimaButton);
//        nimaButton.setOnClickListener(v -> {
//            // 点击按钮后，通过 Activity 切换到 Um982_top2Fragment
//            Um982Fragment um982Fragment = (Um982Fragment) getParentFragment();
//            if (um982Fragment != null) {
//                um982Fragment.switchToFragment(1); // 切换到第二个 Fragment
//            }
//        });

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

        // 设置每个TextView可点击
        nmea_dtm.setClickable(true);
        nmea_gbs.setClickable(true);
        nmea_gga.setClickable(true);
        nmea_ggah.setClickable(true);
        nmea_gll.setClickable(true);
        nmea_gllh.setClickable(true);
        nmea_gns.setClickable(true);
        nmea_gnsh.setClickable(true);
        nmea_gst.setClickable(true);
        nmea_gsth.setClickable(true);
        nmea_ths.setClickable(true);
        nmea_rmc.setClickable(true);
        nmea_rmch.setClickable(true);
        nmea_rot.setClickable(true);
        nmea_vtg.setClickable(true);
        nmea_vtgh.setClickable(true);
        nmea_zda.setClickable(true);

        // 为每个TextView设置相同的点击监听器
        nmea_dtm.setOnClickListener(clickListenerCallbackHandler);
        nmea_gbs.setOnClickListener(clickListenerCallbackHandler);
        nmea_gga.setOnClickListener(clickListenerCallbackHandler);
        nmea_ggah.setOnClickListener(clickListenerCallbackHandler);
        nmea_gll.setOnClickListener(clickListenerCallbackHandler);
        nmea_gllh.setOnClickListener(clickListenerCallbackHandler);
        nmea_gns.setOnClickListener(clickListenerCallbackHandler);
        nmea_gnsh.setOnClickListener(clickListenerCallbackHandler);
        nmea_gst.setOnClickListener(clickListenerCallbackHandler);
        nmea_gsth.setOnClickListener(clickListenerCallbackHandler);
        nmea_ths.setOnClickListener(clickListenerCallbackHandler);
        nmea_rmc.setOnClickListener(clickListenerCallbackHandler);
        nmea_rmch.setOnClickListener(clickListenerCallbackHandler);
        nmea_rot.setOnClickListener(clickListenerCallbackHandler);
        nmea_vtg.setOnClickListener(clickListenerCallbackHandler);
        nmea_vtgh.setOnClickListener(clickListenerCallbackHandler);
        nmea_zda.setOnClickListener(clickListenerCallbackHandler);

        viewModel2 = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        return view;
    }

    private StringBuilder dataBuffer = new StringBuilder();
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel1 = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel1.getDataGroup1().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String data) {
                viewModel2.setDataGroup2(data);

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
                            nmea_dtm.setText(message);
                        } else if (message != null && message.startsWith("$GNGBS,")) {
                            nmea_gbs.setText(message);
                        } else if (message != null && message.startsWith("$GNGGA,")) {
                            nmea_gga.setText(message);
                            SocketService.CORSSSGString = message;
                        } else if (message != null && message.startsWith("$GNGGAH,")) {
                            nmea_ggah.setText(message);
                        } else if (message != null && message.startsWith("$GNGLL,")) {
                            nmea_gll.setText(message);
                        } else if (message != null && message.startsWith("$GNGLLH,")) {
                            nmea_gllh.setText(message);
                        } else if (message != null && message.startsWith("$GNGNS,")) {
                            nmea_gns.setText(message);
                        } else if (message != null && message.startsWith("$GNGNSH,")) {
                            nmea_gnsh.setText(message);
                        } else if (message != null && message.startsWith("$GNGST,")) {
                            nmea_gst.setText(message);
                        } else if (message != null && message.startsWith("$GNGSTH,")) {
                            nmea_gsth.setText(message);
                        } else if (message != null && message.startsWith("$GNTHS,")) {
                            nmea_ths.setText(message);
                        } else if (message != null && message.startsWith("$GNRMC,")) {
                            nmea_rmc.setText(message);
                        } else if (message != null && message.startsWith("$GNRMCH,")) {
                            nmea_rmch.setText(message);
                        } else if (message != null && message.startsWith("$GNROT,")) {
                            nmea_rot.setText(message);
                        } else if (message != null && message.startsWith("$GNVTG,")) {
                            nmea_vtg.setText(message);
                        } else if (message != null && message.startsWith("$GNVTGH,")) {
                            nmea_vtgh.setText(message);
                        } else if (message != null && message.startsWith("$GNZDA,")) {
                            nmea_zda.setText(message);
                        }
                    }
                    dataBuffer.setLength(0);
                }
            }
        });
    }

    // 创建统一的点击监听器
    View.OnClickListener clickListenerCallbackHandler = new View.OnClickListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {
            TextView clickedTextView = (TextView) v;

            Um982Fragment um982Fragment = (Um982Fragment) getParentFragment();

            String message = "Hello";
            if (v.getId() == R.id.nmea_dtm) {
                // 点击 nmea_dtm 时切换到第 1 个 Fragment (index 1)
                if (um982Fragment != null) {
                    um982Fragment.switchToFragment(1); // 切换到第二个 Fragment
                    message = "dtmContainer";
                    Um982_top2Fragment.sendDataToWebView_1(message);
                }
            } else if (v.getId() == R.id.nmea_gbs) {
                // 点击 nmea_gbs 时切换到第 2 个 Fragment (index 2)
                if (um982Fragment != null) {
                    um982Fragment.switchToFragment(1); // 切换到第三个 Fragment
                    message = "gbsContainer";
                    Um982_top2Fragment.sendDataToWebView_1(message);
                }
            } else if (v.getId() == R.id.nmea_gga) {
                // 点击 nmea_gga 时切换到第 3 个 Fragment (index 3)
                if (um982Fragment != null) {
                    um982Fragment.switchToFragment(1); // 切换到第四个 Fragment
                    message = "ggaContainer";
                    Um982_top2Fragment.sendDataToWebView_1(message);
                }
            } else if (v.getId() == R.id.nmea_ggah) {
                // 点击 nmea_ggah 时切换到第 4 个 Fragment (index 4)
                if (um982Fragment != null) {
                    um982Fragment.switchToFragment(1); // 切换到第五个 Fragment
                    message = "ggahContainer";
                    Um982_top2Fragment.sendDataToWebView_1(message);
                }
            } else if (v.getId() == R.id.nmea_gll) {
                // 点击 nmea_gll 时切换到第 5 个 Fragment (index 5)
                if (um982Fragment != null) {
                    um982Fragment.switchToFragment(1); // 切换到第六个 Fragment
                    message = "gllContainer";
                    Um982_top2Fragment.sendDataToWebView_1(message);
                }
            } else if (v.getId() == R.id.nmea_gllh) {
                // 点击 nmea_gllh 时切换到第 6 个 Fragment (index 6)
                if (um982Fragment != null) {
                    um982Fragment.switchToFragment(1); // 切换到第七个 Fragment
                    message = "gllhContainer";
                    Um982_top2Fragment.sendDataToWebView_1(message);
                }
            } else if (v.getId() == R.id.nmea_gns) {
                // 点击 nmea_gns 时切换到第 7 个 Fragment (index 7)
                if (um982Fragment != null) {
                    um982Fragment.switchToFragment(1); // 切换到第八个 Fragment
                    message = "gnsContainer";
                    Um982_top2Fragment.sendDataToWebView_1(message);
                }
            } else if (v.getId() == R.id.nmea_gnsh) {
                // 点击 nmea_gnsh 时切换到第 8 个 Fragment (index 8)
                if (um982Fragment != null) {
                    um982Fragment.switchToFragment(1); // 切换到第九个 Fragment
                    message = "gnshContainer";
                    Um982_top2Fragment.sendDataToWebView_1(message);
                }
            } else if (v.getId() == R.id.nmea_gst) {
                // 点击 nmea_gst 时切换到第 9 个 Fragment (index 9)
                if (um982Fragment != null) {
                    um982Fragment.switchToFragment(1); // 切换到第十个 Fragment
                    message = "gstContainer";
                    Um982_top2Fragment.sendDataToWebView_1(message);
                }
            } else if (v.getId() == R.id.nmea_gsth) {
                // 点击 nmea_gsth 时切换到第 10 个 Fragment (index 10)
                if (um982Fragment != null) {
                    um982Fragment.switchToFragment(1); // 切换到第十一个 Fragment
                    message = "gsthContainer";
                    Um982_top2Fragment.sendDataToWebView_1(message);
                }
            } else if (v.getId() == R.id.nmea_ths) {
                // 点击 nmea_ths 时切换到第 11 个 Fragment (index 11)
                if (um982Fragment != null) {
                    um982Fragment.switchToFragment(1); // 切换到第十二个 Fragment
                    message = "thsContainer";
                    Um982_top2Fragment.sendDataToWebView_1(message);
                }
            } else if (v.getId() == R.id.nmea_rmc) {
                // 点击 nmea_rmc 时切换到第 12 个 Fragment (index 12)
                if (um982Fragment != null) {
                    um982Fragment.switchToFragment(1); // 切换到第十三个 Fragment
                    message = "rmcContainer";
                    Um982_top2Fragment.sendDataToWebView_1(message);
                }
            } else if (v.getId() == R.id.nmea_rmch) {
                // 点击 nmea_rmch 时切换到第 13 个 Fragment (index 13)
                if (um982Fragment != null) {
                    um982Fragment.switchToFragment(1); // 切换到第十四个 Fragment
                    message = "rmchContainer";
                    Um982_top2Fragment.sendDataToWebView_1(message);
                }
            } else if (v.getId() == R.id.nmea_rot) {
                // 点击 nmea_rot 时切换到第 14 个 Fragment (index 14)
                if (um982Fragment != null) {
                    um982Fragment.switchToFragment(1); // 切换到第十五个 Fragment
                    message = "rotContainer";
                    Um982_top2Fragment.sendDataToWebView_1(message);
                }
            } else if (v.getId() == R.id.nmea_vtg) {
                // 点击 nmea_vtg 时切换到第 15 个 Fragment (index 15)
                if (um982Fragment != null) {
                    um982Fragment.switchToFragment(1); // 切换到第十六个 Fragment
                    message = "vtgContainer";
                    Um982_top2Fragment.sendDataToWebView_1(message);
                }
            } else if (v.getId() == R.id.nmea_vtgh) {
                // 点击 nmea_vtgh 时切换到第 16 个 Fragment (index 16)
                if (um982Fragment != null) {
                    um982Fragment.switchToFragment(1); // 切换到第十七个 Fragment
                    message = "vtghContainer";
                    Um982_top2Fragment.sendDataToWebView_1(message);
                }
            } else if (v.getId() == R.id.nmea_zda) {
                // 点击 nmea_zda 时切换到第 17 个 Fragment (index 17)
                if (um982Fragment != null) {
                    um982Fragment.switchToFragment(1); // 切换到第十八个 Fragment
                    message = "zdaContainer";
                    Um982_top2Fragment.sendDataToWebView_1(message);
                }
            }
        }
    };
}