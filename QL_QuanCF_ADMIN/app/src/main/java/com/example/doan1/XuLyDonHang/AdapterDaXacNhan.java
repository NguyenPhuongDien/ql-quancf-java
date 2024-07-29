package com.example.doan1.XuLyDonHang;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan1.R;

import java.util.ArrayList;

public class AdapterDaXacNhan extends RecyclerView.Adapter<AdapterDaXacNhan.MyViewHolder> {

    private ArrayList<DatHang> arrayListDatHang;
    private OnMonClickListener listener;

    public interface OnMonClickListener {
        void onMonClick(DatHang datHang);
    }


    public AdapterDaXacNhan(ArrayList<DatHang> arrayListDatHang,OnMonClickListener onMonClickListener) {
        this.arrayListDatHang = arrayListDatHang;
        this.listener = onMonClickListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_donhang_daxacnhan, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DatHang datHang = arrayListDatHang.get(position);

        // Đặt mã đơn hàng
        holder.tvMaDH.setText(datHang.getMaDH());

        // Đặt ngày đặt hàng
        holder.tvNgayDH.setText(datHang.getNgayDat());

        // Đặt tổng tiền
        holder.tvTongTien.setText(Double.toString(datHang.getTongTien()));

        // Đặt trạng thái đơn hàng và màu sắc
        String trangThai = datHang.getTrangThai();
        holder.tvTrangThai.setText(trangThai);

        // Đặt màu sắc dựa trên trạng thái
        if (trangThai.equals("Đã xác nhận")) {
            holder.tvTrangThai.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
        }
    }

    public void clearData() {
        if (arrayListDatHang != null) {
            arrayListDatHang.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return arrayListDatHang.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNgayDH, tvTongTien, tvTrangThai, tvMaDH;
        Button btnChiTiet;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNgayDH = itemView.findViewById(R.id.tvNgayDH);
            tvTongTien = itemView.findViewById(R.id.tvTongTien);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            tvMaDH = itemView.findViewById(R.id.tvMaDH);
            btnChiTiet = itemView.findViewById(R.id.btnChiTiet);

            btnChiTiet.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    DatHang datHang = arrayListDatHang.get(position); // Get the correct DatHang
                    String maDH = datHang.getMaDH();
                    Log.d("ViewHolder", "Item clicked: " + maDH);
                    Toast.makeText(itemView.getContext(), "Item clicked: " + maDH, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(itemView.getContext(), ChiTietDatHang.class);
                    intent.putExtra("MaDH", maDH);
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && listener != null) {
                listener.onMonClick(arrayListDatHang.get(position));
            }
        }
    }

    private static int timIDAnh(Context context, String tenHinh) {
        int resourceId = context.getResources().getIdentifier(tenHinh, "drawable", context.getPackageName());
        return resourceId == 0 ? -1 : resourceId;
    }
}
