package com.example.ec200a_um982_app.main_fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ec200a_um982_app.MainActivity;
import com.example.ec200a_um982_app.R;
import com.example.ec200a_um982_app.SharedViewModel;
import com.example.ec200a_um982_app.SocketService;
import com.example.ec200a_um982_app.main_fragment.bluetooth_topfragment.Bluetooth_top1Fragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.util.Base64;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NtripFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NtripFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SharedViewModel viewModel1;

    public static byte[] RTCMString;

    EditText CORSip;
    EditText CORSport;
    Spinner CORSmount;
    EditText CORSAccount;
    EditText CORSPassword;
    CheckBox RememberTheServerIP;
    CheckBox RememberTheCORSInformation;

    Button SettingCORSInformationButton;
    Button ConnectCORSserverButton;

    boolean SettingCORSInformationFlag = false;

    String MountPoint=" ";

    // Timer variables
    private Handler handler;
    private Runnable timerRunnable;
    private static final int TIMER_INTERVAL = 100; // 1 seconds

    public NtripFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NtripFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NtripFragment newInstance(String param1, String param2) {
        NtripFragment fragment = new NtripFragment();
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

        // Initialize Handler
        handler = new Handler(Looper.getMainLooper());


        // Define the task to be run periodically
        timerRunnable = new Runnable() {
            int i = 0;
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                // Repeat the task every TIMER_INTERVAL milliseconds
                handler.postDelayed(this, TIMER_INTERVAL*10);


            }
        };
        handler.post(timerRunnable); // 启动更新
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ntrip, container, false);

        CORSip = view.findViewById(R.id.CORSip);
        CORSport = view.findViewById(R.id.CORSport);
        CORSmount = view.findViewById(R.id.CORSmount);
        CORSAccount = view.findViewById(R.id.CORSAccount);
        CORSPassword = view.findViewById(R.id.CORSPassword);
        RememberTheServerIP = view.findViewById(R.id.RememberTheServerIP);
        RememberTheCORSInformation = view.findViewById(R.id.RememberTheCORSInformation);

        SettingCORSInformationButton = view.findViewById(R.id.SettingCORSInformationButton);

        ConnectCORSserverButton = view.findViewById(R.id.ConnectCORSserverButton);


        // 设置发送按钮的点击事件
        SettingCORSInformationButton.setOnClickListener(v -> {
            if (MainActivity.getBluetoothConFlag()) {
                SettingCORSInformationFlag = true;
                SettingCORSInformationButton.setText("请稍等...");
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("ip", CORSip.getText());
                    jsonObject.put("port", CORSport.getText());
                    jsonObject.put("mount", CORSmount.getSelectedItem());

                    String originalString = CORSAccount.getText() + ":" + CORSPassword.getText();
                    String encodedString = Base64.encodeToString(originalString.getBytes(), Base64.NO_WRAP);

                    jsonObject.put("accpas", encodedString); // 假设将账号和密码组合

                    String jsonString = jsonObject.toString();
                    // 使用 jsonString

                    Bluetooth_top1Fragment.characteristic.setValue("AT+" + jsonString + "\r\n"); // 设置要发送的值
                    Bluetooth_top1Fragment.bluetoothGatt.writeCharacteristic(Bluetooth_top1Fragment.characteristic); // 写入特征值
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                MainActivity.showToast(getActivity(), "请连接蓝牙");
            }
        });

        // 文件名
        String fileName = "RememberTheServerIPFile";
        // 获取应用的文件目录
        File file = new File(getActivity().getFilesDir(), fileName);

        if (!file.exists()) {
            // 文件不存在，创建并写入内容
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write("1:1");  // 写入文本内容
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 文件存在，检查是否为空
            if (file.length() == 0) {
                // 文件为空，写入内容
                try {
                    FileWriter writer = new FileWriter(file);
                    writer.write("1:1");  // 写入文本内容
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // 文件不为空，执行其他操作或不操作
//                Toast.makeText(getActivity(), "File already contains content", Toast.LENGTH_SHORT).show();
            }
        }

        // 文件名
        fileName = "RememberTheCORSInformationFile";
        // 获取应用的文件目录
        file = new File(getActivity().getFilesDir(), fileName);

        if (!file.exists()) {
            // 文件不存在，创建并写入内容
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write("1:1");  // 写入文本内容
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 文件存在，检查是否为空
            if (file.length() == 0) {
                // 文件为空，写入内容
                try {
                    FileWriter writer = new FileWriter(file);
                    writer.write("1:1");  // 写入文本内容
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // 文件不为空，执行其他操作或不操作
//                Toast.makeText(getActivity(), "File already contains content", Toast.LENGTH_SHORT).show();
            }
        }



        FileInputStream fis = null;
        try {
            fis = getActivity().openFileInput("RememberFlag1");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            // Read each line from the file
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            // Show the contents in a Toast message
            String fileContents = sb.toString();
            if(fileContents.equals("1")){
                RememberTheServerIP.setChecked(true);
                readFillIPportFile("RememberTheServerIPFile" + ".csv",1);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        fis = null;
        try {
            fis = getActivity().openFileInput("RememberFlag2");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            // Read each line from the file
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            // Show the contents in a Toast message
            String fileContents = sb.toString();
            if(fileContents.equals("1")){
                RememberTheCORSInformation.setChecked(true);
                readFillIPportFile("RememberTheCORSInformationFile" + ".csv",2);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        RememberTheServerIP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean isCheck1 = RememberTheServerIP.isChecked();

                String sIP = CORSip.getText().toString().trim();
                String sPort = CORSport.getText().toString().trim();

                if(isCheck1){
                    if(TextUtils.isEmpty(sIP) || TextUtils.isEmpty(sPort)){

                    }else{
                        StringBuilder Data = new StringBuilder();
                        Data.append("1");
                        FileOutputStream fos = null;
                        try {
                            fos = getActivity().openFileOutput("RememberFlag1", Context.MODE_PRIVATE);
                            fos.write(Data.toString().getBytes());
                            //Toast.makeText(this, "CSV file saved successfully", Toast.LENGTH_SHORT).show();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (fos != null) {
                                    fos.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        writeFileIPport("RememberTheServerIPFile" + ".csv", CORSip.getText().toString(),CORSport.getText().toString());
                    }
                }else {
                    StringBuilder Data = new StringBuilder();
                    Data.append("0");
                    FileOutputStream fos = null;
                    try {
                        fos = getActivity().openFileOutput("RememberFlag1", Context.MODE_PRIVATE);
                        fos.write(Data.toString().getBytes());
                        //Toast.makeText(this, "CSV file saved successfully", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (fos != null) {
                                fos.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        RememberTheCORSInformation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean isCheck = RememberTheCORSInformation.isChecked();
                String sAccount = CORSAccount.getText().toString().trim();
                String sPassword = CORSPassword.getText().toString().trim();

                if(isCheck){
                    if(TextUtils.isEmpty(sAccount) || TextUtils.isEmpty(sPassword)){

                    }else{
                        StringBuilder Data = new StringBuilder();
                        Data.append("1");
                        FileOutputStream fos = null;
                        try {
                            fos = getActivity().openFileOutput("RememberFlag2", Context.MODE_PRIVATE);
                            fos.write(Data.toString().getBytes());
                            //Toast.makeText(this, "CSV file saved successfully", Toast.LENGTH_SHORT).show();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (fos != null) {
                                    fos.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        writeFileAccountPassword("RememberTheCORSInformationFile" + ".csv", CORSAccount.getText().toString(),CORSPassword.getText().toString());
                    }
                }else {
                    StringBuilder Data = new StringBuilder();
                    Data.append("0");
                    FileOutputStream fos = null;
                    try {
                        fos = getActivity().openFileOutput("RememberFlag2", Context.MODE_PRIVATE);
                        fos.write(Data.toString().getBytes());
                        //Toast.makeText(this, "CSV file saved successfully", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (fos != null) {
                                fos.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });


        ConnectCORSserverButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (MainActivity.getBluetoothConFlag()) {
                    // 创建 Intent 对象
                    Intent intent = new Intent(getActivity(), SocketService.class);

                    String expectedMessage = "打开CORS连接";
                    String text = ConnectCORSserverButton.getText().toString();
                    if (text.equals(expectedMessage)) {
                        //进行连接操作
                        ConnectCORSserverButton.setText("关闭Socket连接");
                        String ipAddress = CORSip.getText().toString();
                        String portString = CORSport.getText().toString();
                        if (ipAddress.isEmpty() || portString.isEmpty()) {
                            MainActivity.showToast(getActivity(), "请输入IP地址");
                        } else {
                            int portNumber = Integer.parseInt(portString);
                            if (MainActivity.isBound) {
                                MainActivity.socketService.connectToServer(ipAddress, portNumber);
                            }
                        }

                        String corsAccount = CORSAccount.getText().toString();
                        String corsPassword = CORSPassword.getText().toString();
                        String mountPoint = MountPoint;

                        // 将两个字符串放入 Intent
                        intent.putExtra("CORSAccount", corsAccount);
                        intent.putExtra("CORSPassword", corsPassword);
                        intent.putExtra("MountPoint", mountPoint);

                        // 启动 Service
                        Context context = getActivity(); // 或者 getContext()
                        context.startService(intent);
                    } else {
                        ConnectCORSserverButton.setText("打开CORS连接");

                        intent.putExtra("SocketClose", "SocketClose");

                        // 启动 Service
                        Context context = getActivity(); // 或者 getContext()
                        context.startService(intent);

                        // 停止 Socket 服务
                        Intent socketServiceIntent = new Intent(getActivity(), SocketService.class);
                        getActivity().stopService(socketServiceIntent);
                    }
                } else {
                    MainActivity.showToast(getActivity(), "请连接蓝牙");
                }
            }
        });

        // 创建一个选项列表（数据源）
        List<String> categories = new ArrayList<>();
//        categories.add("选项 1");
//        categories.add("选项 2");
//        categories.add("选项 3");

        // 创建一个适配器（Adapter），用于将数据与 Spinner 关联起来
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // 获取默认视图
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                // 设置字体大小
                textView.setTextSize(16); // 根据需要调整字体大小
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // 获取默认下拉视图
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                // 设置字体大小
                textView.setTextSize(16); // 根据需要调整字体大小
                return view;
            }
        };

        // 设置下拉列表框的样式
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 将适配器设置到 Spinner
        CORSmount.setAdapter(dataAdapter);
        // 设置 Spinner 的选择监听器
        CORSmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 当用户选择某个选项时触发
                String item = parent.getItemAtPosition(position).toString();
                MountPoint = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 当没有选项被选择时触发

            }
        });

        // 根据 Port 的值更新 Spinner 的选项内容
        updateSpinnerOptions();

        return view;
    }
    // 更新 Spinner 的选项内容方法
    private void updateSpinnerOptions() {
        // 根据 Port 的值选择合适的数据源
        List<String> currentCategories = new ArrayList<>();

        currentCategories.add("RTCM33_GRCEpro");//9
        currentCategories.add("RTCM33_GRCEJ");//5
        currentCategories.add("RTCM33_GRCE");//6
        currentCategories.add("RTCM33_GRC");//7
        currentCategories.add("RTCM30_GR");//8
        currentCategories.add("RTCM32_GGB");//10
        currentCategories.add("RTCM30_GG");//11

        // 更新适配器的数据源，并通知适配器数据已改变
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) CORSmount.getAdapter();
        adapter.clear();
        adapter.addAll(currentCategories);
        adapter.notifyDataSetChanged();

        // 选择数据源之后默认选中第一个
        CORSmount.setSelection(0);
    }
    private void readFillIPportFile(String fileName,int num) {
        FileInputStream fis = null;
        try {
            fis = getActivity().openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            // Read each line from the file
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            // Show the contents in a Toast message
            String fileContents = sb.toString();
            String[] strarray=fileContents.split("[:]");
//            MainActivity.showToast(getActivity(),strarray[0] + strarray[1]);

            if(num==1){
                CORSip.setText(strarray[0]);
                CORSport.setText(strarray[1]);
            } else if (num==2) {
                CORSAccount.setText(strarray[0]);
                CORSPassword.setText(strarray[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void writeFileIPport(String filename, String ip,String port) {
        StringBuilder Data = new StringBuilder();

        Data.append(ip).append(":").append(port);

        FileOutputStream fos = null;
        try {
            fos = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(Data.toString().getBytes());
            //Toast.makeText(this, "CSV file saved successfully", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void writeFileAccountPassword(String filename, String Account,String Password) {
        StringBuilder Data = new StringBuilder();

        Data.append(Account).append(":").append(Password);

        FileOutputStream fos = null;
        try {
            fos = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(Data.toString().getBytes());
            //Toast.makeText(this, "CSV file saved successfully", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    int json_num = 0;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel1 = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel1.getDataGroup1().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String data) {
                // 处理接收到的数据
                if (data != null && data.contains("OK")) {
                    SettingCORSInformationButton.setText("设置成功");
                    SettingCORSInformationFlag = false;
                } else if(SettingCORSInformationFlag) {
                    if (MainActivity.getBluetoothConFlag()) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("ip", CORSip.getText());
                            jsonObject.put("port", CORSport.getText());
                            jsonObject.put("mount", CORSmount.getSelectedItem());

                            String originalString = CORSAccount.getText() + ":" + CORSPassword.getText();
                            String encodedString = Base64.encodeToString(originalString.getBytes(), Base64.NO_WRAP);

                            jsonObject.put("accpas", encodedString); // 假设将账号和密码组合

                            String jsonString = jsonObject.toString();
                            // 使用 jsonString

                            json_num ++;
                            if(json_num>5) {
                                json_num = 0;
                                SettingCORSInformationFlag = false;
                            }

                            Bluetooth_top1Fragment.characteristic.setValue("AT+" + jsonString + "\r\n"); // 设置要发送的值
                            Bluetooth_top1Fragment.bluetoothGatt.writeCharacteristic(Bluetooth_top1Fragment.characteristic); // 写入特征值
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        MainActivity.showToast(getActivity(), "请连接蓝牙");
                    }
                }
            }
        });
    }
}