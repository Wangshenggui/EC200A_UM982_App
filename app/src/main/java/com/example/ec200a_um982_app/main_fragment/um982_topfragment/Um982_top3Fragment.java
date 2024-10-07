package com.example.ec200a_um982_app.main_fragment.um982_topfragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.ec200a_um982_app.MainActivity;
import com.example.ec200a_um982_app.R;
import com.example.ec200a_um982_app.main_fragment.BluetoothFragment;

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


        return view;
    }

    // 统一的 CheckBox 状态变化监听器
    private final CompoundButton.OnCheckedChangeListener CheckBoxCallbackHandler = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int id = buttonView.getId();
            TextView associatedTextView = null;

            if (id == R.id.DTMselectCheck) {
                associatedTextView = DTMselectText;
            } else if (id == R.id.GBSselectCheck) {
                associatedTextView = GBSselectText;
            } else if (id == R.id.GGAselectCheck) {
                associatedTextView = GGAselectText;
            } else if (id == R.id.GGAHselectCheck) {
                associatedTextView = GGAHselectText;
            } else if (id == R.id.GLLselectCheck) {
                associatedTextView = GLLselectText;
            } else if (id == R.id.GLLHselectCheck) {
                associatedTextView = GLLHselectText;
            } else if (id == R.id.GNSselectCheck) {
                associatedTextView = GNSselectText;
            } else if (id == R.id.GNSHselectCheck) {
                associatedTextView = GNSHselectText;
            } else if (id == R.id.GSTselectCheck) {
                associatedTextView = GSTselectText;
            } else if (id == R.id.GSTHselectCheck) {
                associatedTextView = GSTHselectText;
            } else if (id == R.id.THSselectCheck) {
                associatedTextView = THSselectText;
            } else if (id == R.id.RMCselectCheck) {
                associatedTextView = RMCselectText;
            } else if (id == R.id.RMCHselectCheck) {
                associatedTextView = RMCHselectText;
            } else if (id == R.id.ROTselectCheck) {
                associatedTextView = ROTselectText;
            } else if (id == R.id.VTGselectCheck) {
                associatedTextView = VTGselectText;
            } else if (id == R.id.VTGHselectCheck) {
                associatedTextView = VTGHselectText;
            } else if (id == R.id.ZDAselectCheck) {
                associatedTextView = ZDAselectText;
            }

            if (associatedTextView != null) {
                handleBluetoothCommand(associatedTextView, isChecked);
                changeTextColor(associatedTextView, isChecked);
            }
        }

        private void handleBluetoothCommand(TextView textView, boolean isChecked) {
            String input = textView.getText().toString();
            String result = input.split(" ")[0];

            if (isChecked) {
                BluetoothFragment.characteristic.setValue("AT+UM982=" + result + " 1" + "\r\n");
                BluetoothFragment.characteristic.setValue("11111111111111111111111111111111111111111111111111111111111111111111111111111111");
            } else {
                BluetoothFragment.characteristic.setValue("AT+UM982=" + "UNLOG " + result + "\r\n");
                BluetoothFragment.characteristic.setValue("22222222222222222222222222222222222222222222222222222222222222222222222222222222");
            }

            MainActivity.showToast(getActivity(), result);

            BluetoothFragment.bluetoothGatt.writeCharacteristic(BluetoothFragment.characteristic);
        }

        private void changeTextColor(TextView textView, boolean isChecked) {
            if (isChecked) {
                textView.setTextColor(0xFF00BFFF); // 深天蓝
            } else {
                textView.setTextColor(0xFF333333); // 深灰色
            }
        }
    };


}