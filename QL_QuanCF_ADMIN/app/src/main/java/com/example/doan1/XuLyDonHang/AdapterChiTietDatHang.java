package com.example.doan1.XuLyDonHang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan1.R;

import java.util.ArrayList;

public class AdapterChiTietDatHang extends RecyclerView.Adapter<AdapterChiTietDatHang.MyViewHolder> {

    private ArrayList<Mon> listMon;

    public AdapterChiTietDatHang(ArrayList<Mon> listMon) {
        this.listMon = listMon;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_listview_chitietdathang, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Mon mon = listMon.get(position);

        holder.tvTenMon.setText(mon.getTenMon());
        holder.tvGiaBan.setText(String.valueOf(mon.getGiaBan()));
        holder.tvSoLuong.setText(String.valueOf(mon.getSoLuong()));

        double thanhTien = mon.getGiaBan() * mon.getSoLuong();
        holder.tvThanhTien.setText(String.valueOf(thanhTien));

        int idAnh = timIDAnh(holder.itemView.getContext(), mon.getHinhAnh());
        if (idAnh != -1) {
            holder.imagemon.setImageResource(idAnh);
        } else {
            holder.imagemon.setImageResource(R.drawable.cf9);
        }
    }

    @Override
    public int getItemCount() {
        return listMon != null ? listMon.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenMon, tvGiaBan, tvSoLuong, tvThanhTien;
        ImageView imagemon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenMon = itemView.findViewById(R.id.tvTenMon);
            tvGiaBan = itemView.findViewById(R.id.tvGiaBan);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            tvThanhTien = itemView.findViewById(R.id.tvThanhTien);
            imagemon = itemView.findViewById(R.id.imagemon);
        }
    }

    private static int timIDAnh(Context context, String tenHinh) {
        return context.getResources().getIdentifier(tenHinh, "drawable", context.getPackageName());
    }
}
