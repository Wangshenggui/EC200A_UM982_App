package com.example.ec200a_um982_app.main_fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ec200a_um982_app.R;

import java.util.ArrayList;
import java.util.List;

public class BluetoothListCustomAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> values;
    private final ArrayList<Boolean> connectionStatus; // 改为 ArrayList<Boolean>

    // 构造函数
    public BluetoothListCustomAdapter(Context context, List<String> values, ArrayList<Boolean> connectionStatus) {
        super(context, R.layout.bluetooth_list_item, values);
        this.context = context;
        this.values = values;
        this.connectionStatus = connectionStatus;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.bluetooth_list_item, parent, false);

            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.item_image);
            holder.textView = convertView.findViewById(R.id.item_text);
            holder.Con_Dis_image = convertView.findViewById(R.id.Con_Dis_image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 设置文本和图像
        holder.textView.setText(values.get(position));
        holder.imageView.setImageResource(R.drawable.bluetooth); // 替换为实际图像

        // 根据连接状态动态设置 Con_Dis_image 的图像
        if (connectionStatus.get(position)) { // 修改为 get 方法
            holder.Con_Dis_image.setImageResource(R.drawable.circle_sub); // 连接状态图标
        } else {
            holder.Con_Dis_image.setImageResource(R.drawable.circle_add); // 断开状态图标
        }

        return convertView;
    }

    // ViewHolder 内部类
    static class ViewHolder {
        ImageView imageView;
        TextView textView;
        ImageView Con_Dis_image;
    }
}
