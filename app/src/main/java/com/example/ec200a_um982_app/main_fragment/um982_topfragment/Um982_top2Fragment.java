package com.example.ec200a_um982_app.main_fragment.um982_topfragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.example.ec200a_um982_app.MainActivity;
import com.example.ec200a_um982_app.R;
import com.example.ec200a_um982_app.SharedViewModel;
import com.example.ec200a_um982_app.SocketService;

import java.util.ArrayList;
import java.util.List;

public class Um982_top2Fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private SharedViewModel viewModel1;
    private static WebView webView;
    private Handler handler;
    private Runnable timerRunnable;
    private static final int TIMER_INTERVAL = 1000; // 1 second

    private String mParam1;
    private String mParam2;

    public Um982_top2Fragment() {
        // Required empty public constructor
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_um982_top2, container, false);
        webView = view.findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);  // Enable JavaScript
        webView.addJavascriptInterface(new WebAppInterface(), "Android");
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/Um982_top2Fragment.html");

        handler = new Handler(Looper.getMainLooper());
        timerRunnable = new Runnable() {
            @Override
            public void run() {

//                MainActivity.showToast(getActivity(),"12");
//                sendDataToWebView("$GNGGA,020523.000,2623.013809,N,10636.512344,E,5,33,1.26,1199.9,M,-26.1,M,1.0,0451*4A");

                // Repeat the task every TIMER_INTERVAL milliseconds
                handler.postDelayed(this, TIMER_INTERVAL);
            }
        };
        handler.post(timerRunnable); // 启动更新

        return view;
    }

    public class WebAppInterface {
        @android.webkit.JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
        }

        @android.webkit.JavascriptInterface
        public void sendDataToAndroid(String data) {
            Log.d("WebAppInterface", "Data received from WebView: " + data);
            MainActivity.showToast(getActivity(), "你干嘛" + data);
        }
    }

    public void sendDataToWebView(String data) {
        webView.evaluateJavascript("javascript:receiveDataFromAndroid('" + data + "')", null);
    }
    public static void sendDataToWebView_1(String data) {
        webView.evaluateJavascript("javascript:receiveDataFromAndroid_1('" + data + "')", null);
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
                            sendDataToWebView(message);
                        } else if (message != null && message.startsWith("$GNGBS,")) {
                            sendDataToWebView(message);
                        } else if (message != null && message.startsWith("$GNGGA,")) {
                            sendDataToWebView(message);
                        } else if (message != null && message.startsWith("$GNGGAH,")) {
                            sendDataToWebView(message);
                        } else if (message != null && message.startsWith("$GNGLL,")) {
                            sendDataToWebView(message);
                        } else if (message != null && message.startsWith("$GNGLLH,")) {
                            sendDataToWebView(message);
                        } else if (message != null && message.startsWith("$GNGNS,")) {
                            sendDataToWebView(message);
                        } else if (message != null && message.startsWith("$GNGNSH,")) {
                            sendDataToWebView(message);
                        } else if (message != null && message.startsWith("$GNGST,")) {
                            sendDataToWebView(message);
                        } else if (message != null && message.startsWith("$GNGSTH,")) {
                            sendDataToWebView(message);
                        } else if (message != null && message.startsWith("$GNTHS,")) {
                            sendDataToWebView(message);
                        } else if (message != null && message.startsWith("$GNRMC,")) {
                            sendDataToWebView(message);
                        } else if (message != null && message.startsWith("$GNRMCH,")) {
                            sendDataToWebView(message);
                        } else if (message != null && message.startsWith("$GNROT,")) {
                            sendDataToWebView(message);
                        } else if (message != null && message.startsWith("$GNVTG,")) {
                            sendDataToWebView(message);
                        } else if (message != null && message.startsWith("$GNVTGH,")) {
                            sendDataToWebView(message);
                        } else if (message != null && message.startsWith("$GNZDA,")) {
                            sendDataToWebView(message);
                        }
                    }
                    dataBuffer.setLength(0);
                }
            }
        });
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
}
