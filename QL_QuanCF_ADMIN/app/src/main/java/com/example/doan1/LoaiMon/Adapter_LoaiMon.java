package com.example.doan1.LoaiMon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.doan1.R;

import java.util.List;

public class Adapter_LoaiMon extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Class_LoaiMon> LoaiMonList;

    public Adapter_LoaiMon(Context context, int layout, List<Class_LoaiMon> LoaiMonList) {
        this.context = context;
        this.layout = layout;
        this.LoaiMonList = LoaiMonList;
    }

    @Override
    public int getCount() {
        return LoaiMonList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView txtMaLoai, txtTenLoai;

    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            holder.txtMaLoai = (TextView) view.findViewById(R.id.MaLoai);
            holder.txtTenLoai = (TextView) view.findViewById(R.id.TenLoai);


            holder.txtMaLoai.setText(LoaiMonList.get(position).getMaLoai());
            holder.txtTenLoai.setText(LoaiMonList.get(position).getTenLoai());



        }
        return view;
    }
}


