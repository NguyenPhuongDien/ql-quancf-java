package com.example.doan1.HoaDon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan1.Ban.SharedViewModel;
import com.example.doan1.Mon.Class_Mon;
import com.example.doan1.R;

import java.util.HashMap;
import java.util.List;

public class PhieuThanhToan_Adapter extends RecyclerView.Adapter<PhieuThanhToan_Adapter.ViewHolder> {

    private Context context;
    private List<Class_Mon> listMonAn; // Danh sách các món ăn
    private HashMap<String, Integer> soLuongMon; // HashMap lưu số lượng mỗi món
    private SharedViewModel sharedViewModel;

    public PhieuThanhToan_Adapter(Context context, List<Class_Mon> listMonAn, SharedViewModel sharedViewModel, HashMap<String, Integer> soLuongMon) {
        this.context = context;
        this.listMonAn = listMonAn;
        this.sharedViewModel = sharedViewModel;
        this.soLuongMon = soLuongMon; // Nhận soLuongMon trong constructor
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.phieuthanhtoan_customlistview, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Class_Mon monAn = listMonAn.get(position);
        String imgName = monAn.getHinhAnh().toLowerCase();
        int imgId = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());
        holder.hinhanh.setImageResource(imgId);
        holder.tvTenMon.setText(monAn.getTenMon());
        holder.tvSoLuong.setText("Số lượng: " + soLuongMon.getOrDefault(monAn.getMaMon(), 0)); // Lấy số lượng từ soLuongMon
        holder.tvThanhTien.setText("Thành tiền: " + monAn.getThanhTien());
        holder.tvGhiChu.setText("Ghi chú: " + monAn.getGhiChu());
    }

    @Override
    public int getItemCount() {
        return listMonAn.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTenMon, tvSoLuong, tvThanhTien, tvGhiChu;
        ImageView hinhanh;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTenMon = itemView.findViewById(R.id.tvTenMon);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            tvThanhTien = itemView.findViewById(R.id.tvThanhTien);
            tvGhiChu = itemView.findViewById(R.id.tvGhiChu);
            hinhanh = itemView.findViewById(R.id.ivHinhMonAn);
        }
    }
}