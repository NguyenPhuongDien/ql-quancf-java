package com.example.ql_quancf.Controller;

import com.example.ql_quancf.Model.LoaiMon;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ql_quancf.Model.LoaiMon;
import com.example.ql_quancf.R;

import java.util.List;
public class AdapterSpiner_LoaiMon extends ArrayAdapter<LoaiMon> {
    private final Context context;
    private final List<LoaiMon> listLoaiMon;

    public AdapterSpiner_LoaiMon(Context context, List<LoaiMon> listLoaiMon) {
        super(context, 0, listLoaiMon);
        this.context = context;
        this.listLoaiMon = listLoaiMon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.custom_spinner_loaimon, parent, false);
        }

        LoaiMon loaiMon = listLoaiMon.get(position);

        TextView tvTenLoai = view.findViewById(R.id.tvTenLoai);
        tvTenLoai.setText(loaiMon.getTenLoai());

        return view;
    }
}
