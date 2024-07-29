package com.example.ql_quancf.Controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ql_quancf.Model.Connect;
import com.example.ql_quancf.Model.DatHang;
import com.example.ql_quancf.Model.KhachHangManager;
import com.example.ql_quancf.Model.Mon;
import com.example.ql_quancf.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Adapter_LSDonHang extends RecyclerView.Adapter<Adapter_LSDonHang.MyViewHolder> {

    private ArrayList<DatHang> arrayListDatHang;
    private OnDonHangClickListener listener;

    public interface OnMonClickListener {
        void onDatHangClick(DatHang datHang);
    }


    public Adapter_LSDonHang(ArrayList<DatHang> arrayListDatHang, OnDonHangClickListener listener) {
        this.arrayListDatHang = arrayListDatHang;
        this.listener = listener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_lsdathang, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DatHang datHang = arrayListDatHang.get(position);
        holder.tvMaDH.setText(datHang.getMaDH());
        //Set ngày đặt hàng
        holder.tvNgayDH.setText(datHang.getNgayDat());
        //Tổng tiền
        holder.tvTongTien.setText(Double.toString(datHang.getTongTien()));
        //Trạng thái đã được quán xác nhận hay chưa
        holder.tvTrangThai.setText(datHang.getTrangThai());

    }

    @Override
    public int getItemCount() {
        return arrayListDatHang.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNgayDH, tvTongTien,tvTrangThai,tvMaDH;
        Button btnDatLai;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNgayDH =(TextView) itemView.findViewById(R.id.tvNgayDH);
            tvTongTien=(TextView)itemView.findViewById(R.id.tvTongTien);
            btnDatLai=(Button) itemView.findViewById(R.id.btnDatLai);
            tvTrangThai=(TextView) itemView.findViewById(R.id.tvTrangThai);
            tvMaDH=(TextView) itemView.findViewById(R.id.tvMaDH);
            //Đặt lại
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                listener.onDatHangClick(arrayListDatHang.get(position));
            }
        }
    }
    private static int timIDAnh(Context context, String tenHinh) {
        int resourceId = context.getResources().getIdentifier(tenHinh, "drawable", context.getPackageName());
        return resourceId == 0 ? -1 : resourceId;
    }


}
