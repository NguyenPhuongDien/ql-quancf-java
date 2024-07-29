package com.example.ql_quancf.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ql_quancf.Model.Mon;
import com.example.ql_quancf.R;
import java.util.ArrayList;

public class Adapter_ChiTietDonHang extends RecyclerView.Adapter<Adapter_ChiTietDonHang.MyViewHolder> {

    private ArrayList<Mon> arrayListMon;


    public interface OnMonClickListener {
        void onMonClick(Mon mon);
    }

    public Adapter_ChiTietDonHang(ArrayList<Mon> arrayListMon) {
        this.arrayListMon = arrayListMon;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_listview_ctdh, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Mon mon = arrayListMon.get(position);
        holder.tvTenMon.setText(mon.getTenMon());
        holder.tvThanhTien.setText(String.valueOf(mon.getGiaBan()*mon.getSoLuong()));
        int idAnh = timIDAnh(holder.itemView.getContext(), mon.getHinhAnh());
        if (idAnh != -1) {
            holder.imgHinhAnh.setImageResource(idAnh);
        } else {
            holder.imgHinhAnh.setImageResource(R.drawable.imgfood_test_layout);
        }
    }

    @Override
    public int getItemCount() {
        return arrayListMon.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTenMon, tvThanhTien,tvSoLuong;
        ImageView imgHinhAnh;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenMon = itemView.findViewById(R.id.tvTenMon);
            tvThanhTien = itemView.findViewById(R.id.tvThanhTien);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            imgHinhAnh = itemView.findViewById(R.id.imgAnhMon);
        }
        @Override
        public void onClick(View v) {

        }
    }

    private static int timIDAnh(Context context, String tenHinh) {
        int resourceId = context.getResources().getIdentifier(tenHinh, "drawable", context.getPackageName());
        return resourceId == 0 ? -1 : resourceId;  // Return -1 if resourceId is 0
    }

}
