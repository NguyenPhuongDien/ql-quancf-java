package com.example.doan1.XuLyDonHang;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doan1.Connect.Connect;
import com.example.doan1.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterDonDatHang extends RecyclerView.Adapter<AdapterDonDatHang.MyViewHolder> {

    private ArrayList<DatHang> arrayListDatHang;
    private OnClickDonhang listener;
    public interface OnClickDonhang {
        void onDatHangClick(DatHang datHang);
    }
    public AdapterDonDatHang(ArrayList<DatHang> arrayListDatHang, OnClickDonhang listener) {
        this.arrayListDatHang = arrayListDatHang;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_donhang_chuaxacnhan, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DatHang datHang = arrayListDatHang.get(position);
        holder.tvMaDH.setText(datHang.getMaDH());
        holder.tvNgayDH.setText(datHang.getNgayDat());
        holder.tvTongTien.setText(Double.toString(datHang.getTongTien()));
        holder.tvTrangThai.setText(datHang.getTrangThai());
    }

    @Override
    public int getItemCount() {
        return arrayListDatHang.size();
    }

    public void clearData() {
        if (arrayListDatHang != null) {
            arrayListDatHang.clear();
            notifyDataSetChanged();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNgayDH, tvTongTien, tvTrangThai, tvMaDH;
        Button btnXacNhan, btnHuy;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNgayDH = itemView.findViewById(R.id.tvNgayDH);
            tvTongTien = itemView.findViewById(R.id.tvTongTien);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            tvMaDH = itemView.findViewById(R.id.tvMaDH);
            btnXacNhan = itemView.findViewById(R.id.btnXacNhan);
            btnHuy = itemView.findViewById(R.id.btnHuy);

            // Set item click listener
            itemView.setOnClickListener(this);

            // Handle Xác nhận and Hủy button clicks
            btnXacNhan.setOnClickListener(v -> handleAction(v, "Đã xác nhận"));
            btnHuy.setOnClickListener(v -> handleAction(v, "Đã huỷ"));
        }

        private void handleAction(View v, String trangThai) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                DatHang datHang = arrayListDatHang.get(position);
                String maDH = datHang.getMaDH();
                String urlXacNhanDonHang = "http://" + Connect.connectip + "/DoAn_Android/XacNhanDonHang.php";
                postData(itemView.getContext(), maDH, trangThai, urlXacNhanDonHang);
            }
        }

        private void postData(Context context, String maDH, String trangThai, String url) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    response -> {
                        Log.d("Response", response);
                        if (response.trim().equals("success")) {
                            Toast.makeText(context, "Xác nhận thành công", Toast.LENGTH_SHORT).show();
                        } else if (response.trim().equals("exist")) {
                            Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Có lỗi xảy ra: " + response, Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        Log.e("VolleyError", error.toString());
                        Toast.makeText(context, "Xảy ra lỗi, vui lòng thử lại: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("MaDH", maDH);
                    params.put("TrangThai", trangThai);
                    return params;
                }
            };
            requestQueue.add(request);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                listener.onDatHangClick(arrayListDatHang.get(position));
            }
        }
    }
}
