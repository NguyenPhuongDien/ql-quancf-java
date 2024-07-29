package com.example.doan1.XuLyDonHang;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doan1.Connect.Connect;
import com.example.doan1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChuaXacNhanDon_Fragment extends Fragment {
    View view;
    RecyclerView recycler_DH_ChuaXacNhan;
    String URL;
    ArrayList<DatHang> listDonHang;
    AdapterDonDatHang adapterDonDatHang;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tvDonHang;
    private Handler handler = new Handler();
    private Runnable runnable;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_chua_xacnhandon, container, false);
        addID();
        getData(URL);
        adapterDonDatHang = new AdapterDonDatHang(listDonHang, datHang -> {
            String maDH = datHang.getMaDH();
            Log.d("Fragment", "Item clicked: " + maDH);
            Toast.makeText(view.getContext(), "Item clicked: " + maDH, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(view.getContext(), ChiTietDatHang.class);
            intent.putExtra("MaDH", maDH);
            startActivity(intent);
        });
        recycler_DH_ChuaXacNhan.setAdapter(adapterDonDatHang);
        recycler_DH_ChuaXacNhan.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            adapterDonDatHang.clearData();
            getData(URL);
            refreshData();
        });
        startPolling();
        return view;
    }

    void addID() {
        recycler_DH_ChuaXacNhan = view.findViewById(R.id.recycler_DH_ChuaXacNhan);
        tvDonHang = view.findViewById(R.id.tvDonHang);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        listDonHang = new ArrayList<>();
        URL = "http://" + Connect.connectip + "/DoAn_Android/List_DH_ChuaXacNhan.php";
    }

    private void refreshData() {
        new Handler().postDelayed(
                () -> swipeRefreshLayout.setRefreshing(false),
                2000); // 2 giây giả lập thời gian làm mới dữ liệu
    }

    // Load dữ liệu từ API
    private void getData(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> parseCafeResponse(response.toString()),
                error -> handleError(error));
        requestQueue.add(jsonArrayRequest);
    }

    private void parseCafeResponse(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            listDonHang.clear();
            if (jsonArray.length() != 0) {
                tvDonHang.setText("DANH SÁCH ĐƠN HÀNG");
            } else {
                tvDonHang.setText("KHÔNG CÓ ĐƠN HÀNG NÀO");
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // Thêm đối tượng Đơn hàng vào list
                listDonHang.add(new DatHang(
                        jsonObject.getString("MaDH"),
                        jsonObject.getString("MaKH"),
                        jsonObject.getString("NgayDat"),
                        jsonObject.getDouble("TongTien"),
                        jsonObject.getString("TrangThai"),
                        jsonObject.getString("GhiChu")
                ));
            }
            adapterDonDatHang.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.e("JSONError", "Error parsing JSON: " + e.getMessage());
        }
    }

    private void handleError(VolleyError error) {
        Log.e("NetworkError", "Xảy ra lỗi: " + error.getMessage());
        Toast.makeText(getContext(), "Lỗi mạng: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    // Inner class Adapter
    public interface OnClickDonhang {
        void onDatHangClick(DatHang datHang);
    }
    private void startPolling() {
        runnable = new Runnable() {
            @Override
            public void run() {
                getData(URL);
                handler.postDelayed(this, 5000);
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    public class AdapterDonDatHang extends RecyclerView.Adapter<AdapterDonDatHang.MyViewHolder> {

        private ArrayList<DatHang> arrayListDatHang;
        private OnClickDonhang listener;

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

                // Handle Xác nhận and Hủy button clicks using anonymous inner classes
                btnXacNhan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleAction(v, "Đã xác nhận");
                    }
                });

                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleAction(v, "Đã huỷ");
                    }
                });
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
                                // Refresh data on success
                                adapterDonDatHang.clearData();
                                getData(URL);
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
}
