package com.example.ql_quancf.Controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.ql_quancf.Model.KhachHangManager;
import com.example.ql_quancf.Model.Mon;
import com.example.ql_quancf.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterMon_GioHang extends RecyclerView.Adapter<AdapterMon_GioHang.MyViewHolder> {

    private ArrayList<Mon> arrayListMon;
    private OnMonClickListener listener;

    public interface OnMonClickListener {
        void onMonClick(Mon mon);
    }
    private SwipeRefreshLayout swipeRefreshLayout;

    public AdapterMon_GioHang(ArrayList<Mon> arrayListMon, OnMonClickListener listener) {
        this.arrayListMon = arrayListMon;
        this.listener = listener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.giohang, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Mon mon = arrayListMon.get(position);
        holder.tvTenMon.setText(mon.getTenMon());
        holder.tvGiaBan.setText(String.valueOf(mon.getGiaBan()));
        holder.edtSoLuong.setText(Integer.toString(mon.getSoLuong()));
        int idAnh = timIDAnh(holder.itemView.getContext(), mon.getHinhAnh());
        int thanhTien = mon.getSoLuong() * mon.getGiaBan();
        holder.tvThanhTien.setText(Integer.toString(thanhTien));


        if (idAnh != -1) {
            holder.imagemon.setImageResource(idAnh);
        } else {
            holder.imagemon.setImageResource(R.drawable.imgfood_test_layout);
        }
    }

    @Override
    public int getItemCount() {
        return arrayListMon.size();
    }
    public void clearData() {
        if (arrayListMon != null) {
            arrayListMon.clear();
            notifyDataSetChanged();
        }
    }
    public ArrayList<Mon> getCurrentList() {
        return new ArrayList<>(arrayListMon); // Trả về một bản sao của danh sách để tránh thay đổi dữ liệu gốc
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTenMon, tvGiaBan, tvThanhTien;
        EditText edtSoLuong;
        ImageView imagemon;
        ImageView iconGiam, iconTang;
        Button btnUpdate;

        int tongTien=0;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenMon = itemView.findViewById(R.id.tvTenMon);
            tvGiaBan = itemView.findViewById(R.id.tvGiaBan);
            imagemon = itemView.findViewById(R.id.imagemon);
            edtSoLuong = itemView.findViewById(R.id.edtSoLuong);
            tvThanhTien = itemView.findViewById(R.id.tvThanhTien);
            iconGiam = itemView.findViewById(R.id.iconGiam);
            iconTang = itemView.findViewById(R.id.iconTang);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);


            // Giảm số lượng
            iconGiam.setOnClickListener(v -> updateQuantity(false));

            // Tăng số lượng
            iconTang.setOnClickListener(v -> updateQuantity(true));

            // Thêm vào giỏ hàng
            btnUpdate.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Mon selectedMon = arrayListMon.get(position);
                    String soLuong = edtSoLuong.getText().toString().trim();
                    if (soLuong.isEmpty()) {
                        soLuong = "1";
                    }
                    String MaGH = KhachHangManager.MaGH; // Lấy ra mã giỏ hàng của khách hàng đang đăng nhập
                    String MaMon = selectedMon.getMaMon(); // Mã món đang chọn
                    // Tính tiền
                    double ThanhTien = Double.parseDouble(soLuong) * selectedMon.getGiaBan();
                    // Link API
                    String urlAddCTGH = "http://" + Connect.ipConnect + "/Ql_QuanCF_User/UpdateGioHang.php";
                    // Thêm vào CSDL
                    Log.d("Món","Mã giỏ hàng:"+MaGH+"\n Mã Món: "+MaMon+"\n Số lượng:"+soLuong+"\n Thành tiền:"+ThanhTien);
                    postData(itemView.getContext(), MaGH, MaMon, Integer.parseInt(soLuong), ThanhTien, urlAddCTGH);
                }
            });

            itemView.setOnClickListener(this);
        }

        private void updateQuantity(boolean increase) {
            String sl = edtSoLuong.getText().toString().trim();
            if (sl.isEmpty()) {
                sl = "0";
            }
            int quantity = Integer.parseInt(sl);
            if (increase) {
                quantity++;
            } else if (quantity > 1) {
                quantity--;
            }
            edtSoLuong.setText(String.valueOf(quantity));
        }

        void postData(Context context, String MaGH, String MaMon, int SoLuong, double ThanhTien, String url) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    response -> {
                        Log.d("Response", response); // Log the response
                        if (response.trim().equals("success")) {
                            Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        } else if (response.trim().equals("exist")) {
                            Toast.makeText(context, "Mã Món đã tồn tại", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Có lỗi xảy ra: " + response, Toast.LENGTH_SHORT).show(); // Show detailed error
                        }
                    },
                    error -> {
                        Log.e("VolleyError", error.toString()); // Log the error
                        Toast.makeText(context, "Xảy ra lỗi, vui lòng thử lại: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("MaGH", MaGH);
                    params.put("MaMon", MaMon);
                    params.put("SoLuong", Integer.toString(SoLuong));
                    params.put("ThanhTien", Double.toString(ThanhTien));
                    return params;
                }
            };
            requestQueue.add(request);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                listener.onMonClick(arrayListMon.get(position));
            }
        }
    }

    private static int timIDAnh(Context context, String tenHinh) {
        int resourceId = context.getResources().getIdentifier(tenHinh, "drawable", context.getPackageName());
        return resourceId == 0 ? -1 : resourceId;  // Return -1 if resourceId is 0
    }
}
