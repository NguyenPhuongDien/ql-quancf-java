package com.example.doan1.NhanVien;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.doan1.LoaiMon.Adapter_LoaiMon;
import com.example.doan1.LoaiMon.Class_LoaiMon;
import com.example.doan1.R;

import java.util.List;

public class Adapter_NhanVien extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Class_NhanVien> NVList;

    public Adapter_NhanVien(Context context, int layout, List<Class_NhanVien> NVList) {
        this.context = context;
        this.layout = layout;
        this.NVList = NVList;
    }

    @Override
    public int getCount() {
        return NVList.size();
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
        TextView MaNV,TenNV,SDT,DiaChi;

    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        Adapter_NhanVien.ViewHolder holder;
        if (view == null) {
            holder = new Adapter_NhanVien.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            holder.MaNV = (TextView) view.findViewById(R.id.MaNV);
            holder.TenNV = (TextView) view.findViewById(R.id.TenNV);
            holder.SDT = (TextView) view.findViewById(R.id.SDT);
            holder.DiaChi = (TextView) view.findViewById(R.id.DiaChi);


            holder.MaNV.setText(NVList.get(position).getMaNV());
            holder.TenNV.setText(NVList.get(position).getTenNV());
            holder.SDT.setText(NVList.get(position).getSDT());
            holder.DiaChi.setText(NVList.get(position).getDiaChi());



        }
        return view;
    }
}


