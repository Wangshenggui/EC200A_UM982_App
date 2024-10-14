package com.example.ec200a_um982_app.main_fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ec200a_um982_app.R;

import java.util.List;

public class BluetoothListCustomAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> values;

    // 构造函数的名称应与类名一致
    public BluetoothListCustomAdapter(Context context, List<String> values) {
        super(context, R.layout.bluetooth_list_item, values);
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.bluetooth_list_item, parent, false);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView imageView = rowView.findViewById(R.id.item_image);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textView = rowView.findViewById(R.id.item_text);

        textView.setText(values.get(position));
        imageView.setImageResource(R.drawable.bluetooth); // 替换为实际图像

        return rowView;
    }
}
