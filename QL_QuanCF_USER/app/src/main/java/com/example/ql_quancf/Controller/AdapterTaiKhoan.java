package com.example.ql_quancf.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ql_quancf.Model.TaiKhoanKH;
import com.example.ql_quancf.R;

import java.util.ArrayList;

public class AdapterTaiKhoan extends RecyclerView.Adapter<AdapterTaiKhoan.MyViewHolder> {

    ArrayList<TaiKhoanKH> arrayListSong = new ArrayList<>();

    public AdapterTaiKhoan(ArrayList<TaiKhoanKH> arrayListSong) {
        this.arrayListSong = arrayListSong;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_rycleview_mon,
                parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TaiKhoanKH taiKhoanKH = arrayListSong.get(position);
        holder.tvTaiKhoan.setText(taiKhoanKH.getTaiKhoan());
        holder.tvMatKhau.setText(taiKhoanKH.getMatKhau());
    }

    @Override
    public int getItemCount() {
        return arrayListSong.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaiKhoan, tvMatKhau; // Thay đổi tên TextView
        ImageView imgHinhAnh;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaiKhoan = itemView.findViewById(R.id.tvTenMon); // Thay đổi ID nếu cần
            tvMatKhau = (TextView) itemView.findViewById(R.id.tvGiaBan); // Thay đổi ID nếu cần
            imgHinhAnh = itemView.findViewById(R.id.imgAnhMon);
        }
    }

    public static int timIDAnh(Context context, String tenHinh) {
        int resourceId = context.getResources().getIdentifier(tenHinh, "drawable", context.getPackageName());
        if (resourceId == 0) {
            return -1;
        } else {
            return resourceId;
        }
    }
}