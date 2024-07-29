package com.example.ql_quancf.View;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ql_quancf.Controller.AdapterMon_GioHang;
import com.example.ql_quancf.Controller.OnMonClickListener;
import com.example.ql_quancf.Model.Connect;
import com.example.ql_quancf.Model.KhachHangManager;
import com.example.ql_quancf.Model.ListMonDatHang;
import com.example.ql_quancf.Model.Mon;
import com.example.ql_quancf.R;
import com.example.ql_quancf.dathang;
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

public class donhang_fragment extends Fragment {
    View view;
    TextView tvTongTien, tvThongBao, tvTextTongTien;
    Button btnDatHang;
    RecyclerView recyclerviewCTGH;
    AdapterMon_GioHang adapterMonGioHang;
    String URL_CTGH;
    ArrayList<Mon> listMon;
    SwipeRefreshLayout swipeRefreshLayout;

    int tongTien = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_donhang, container, false);
        listMon = new ArrayList<>();
        addID();
        addEvent();
        adapterMonGioHang=new AdapterMon_GioHang(listMon, new OnMonClickListener() {
            @Override
            public void onMonClick(Mon mon) {

            }
        });



        recyclerviewCTGH.setAdapter(adapterMonGioHang);
        recyclerviewCTGH.setLayoutManager(new LinearLayoutManager(view.getContext()));
        new SendPostRequest().execute(KhachHangManager.MaGH);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapterMonGioHang.clearData();
                new SendPostRequest().execute(KhachHangManager.MaGH);
                refreshData();
            }
        });
        return view;
    }


    void addID() {
        tvTongTien = (TextView) view.findViewById(R.id.tvTongTien);
        btnDatHang = (Button) view.findViewById(R.id.btnDatHang);
        recyclerviewCTGH = (RecyclerView) view.findViewById(R.id.recyclerviewCTGH);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        tvTextTongTien = (TextView) view.findViewById(R.id.tvTextTongTien);
    }

    void addEvent() {

        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double tongTien = Double.parseDouble(tvTongTien.getText().toString());

                ListMonDatHang.TongTien=tongTien;
                Log.d("Món","Tên móm :"+Double.toString(ListMonDatHang.TongTien));
                if (listMon.size() > 0) {
                    ListMonDatHang.listMonDatHang.clear();
                    for (Mon mon : listMon) {
                        Log.d("Món","Tên móm :"+mon.getTenMon());
                        ListMonDatHang.listMonDatHang.add(mon);
                    }
                    Intent intent=new Intent(view.getContext(), dathang.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(view.getContext(), "Không có sản phẩm trong giỏ hàng", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void refreshData() {
        // Ví dụ: giả lập làm mới dữ liệu
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // Kết thúc làm mới
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },
                2000); // 2 giây giả lập thời gian làm mới dữ liệu
    }

    //Post dữ liệu đơn hàng
    public interface OnMaDHReceivedListener {
        void onMaDHReceived(String maDH);
    }

    void postDonHang(Context context, String MaKH, double TongTien, String maGioHang, String url, OnMaDHReceivedListener listener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("Response", response);
                    if (!response.isEmpty()) {
                        // Trả về MaDH trong phản hồi từ máy chủ
                        String MaDH = response.trim();
                        listener.onMaDHReceived(MaDH);
                    } else {
                        // Gửi mã đơn hàng rỗng nếu không có phản hồi từ máy chủ
                        listener.onMaDHReceived("");
                    }
                },
                error -> {
                    Log.e("VolleyError", error.toString()); // Log the error
                    // Gửi mã đơn hàng rỗng nếu có lỗi
                    listener.onMaDHReceived("");
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("MaKH", MaKH);
                params.put("TongTien", Double.toString(TongTien));
                params.put("MaGH", maGioHang);
                return params;
            }
        };
        requestQueue.add(request);
    }

    void addCHiTietDatHang(Context context, String maDH, String maMon, int soLuong, double thanhTien, String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("Response", response); // Log the response
                    if (!response.isEmpty()) {
                        // Trả về MaDH trong phản hồi từ máy chủ
                        String MaDH = response.trim();

                    } else {
                        // Gửi mã đơn hàng rỗng nếu không có phản hồi từ máy chủ

                    }
                },
                error -> {
                    Log.e("VolleyError", error.toString()); // Log the error
                    // Gửi mã đơn hàng rỗng nếu có lỗi

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("MaDH", maDH);
                params.put("MaMon", maMon);
                params.put("SoLuong", Integer.toString(soLuong));
                params.put("ThanhTien", Double.toString(thanhTien));
                return params;
            }
        };
        requestQueue.add(request);
    }

    //Xoá giỏ hàng sau khi thanh toán
    void xoaGioHang(Context context, String maGH, String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("Response", response); // Log the response
                    if (!response.isEmpty()) {
                        // Hiển thị thông báo cho người dùng

                        Log.d("DonHang", response);
                    } else {

                        Log.e("DonHang", "Không có phản hồi từ máy chủ");
                    }
                },
                error -> {
                    Log.e("VolleyError", error.toString()); // Log the error
                    // Hiển thị thông báo lỗi cho người dùng
                    Toast.makeText(context, "Lỗi khi thực hiện yêu cầu: " + error.toString(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("MaGH", maGH);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private class SendPostRequest extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            String MaGH = params[0];
            String urlString = "http://" + Connect.ipConnect + "/Ql_QuanCF_User/ThongTinGioHang.php";
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setDoOutput(true);

                String urlParameters = "MaGH=" + MaGH;

                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = urlConnection.getResponseCode();
                Log.d("DonHang", "Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    return response.toString();
                } else {
                    Log.e("DonHang", "Gửi dữ liệu thất bại.");
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                parseJsonResponse(result);
            } else {
                Log.e("DonHang", "Không có phản hồi từ server.");
            }
        }

        private void parseJsonResponse(String jsonResponse) {
            Gson gson = new Gson();
            try {
                JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class);

                for (JsonElement element : jsonArray) {
                    JsonObject khachHang = element.getAsJsonObject();

                    String maGH = khachHang.has("MaGH") && !khachHang.get("MaGH").isJsonNull() ? khachHang.get("MaGH").getAsString() : "";
                    String maMon = khachHang.has("MaMon") && !khachHang.get("MaMon").isJsonNull() ? khachHang.get("MaMon").getAsString() : "";
                    String soLuong = khachHang.has("SoLuong") && !khachHang.get("SoLuong").isJsonNull() ? khachHang.get("SoLuong").getAsString() : "0";
                    String thanhTien = khachHang.has("ThanhTien") && !khachHang.get("ThanhTien").isJsonNull() ? khachHang.get("ThanhTien").getAsString() : "0";
                    String maLoai = khachHang.has("MaLoai") && !khachHang.get("MaLoai").isJsonNull() ? khachHang.get("MaLoai").getAsString() : "";
                    String tenMon = khachHang.has("TenMon") && !khachHang.get("TenMon").isJsonNull() ? khachHang.get("TenMon").getAsString() : "";
                    String hinhAnh = khachHang.has("HinhAnh") && !khachHang.get("HinhAnh").isJsonNull() ? khachHang.get("HinhAnh").getAsString() : "";
                    String giaBan = khachHang.has("GiaBan") && !khachHang.get("GiaBan").isJsonNull() ? khachHang.get("GiaBan").getAsString() : "0";

                    Mon mon = new Mon(maMon, maLoai, tenMon, hinhAnh, Integer.parseInt(giaBan), Integer.parseInt(soLuong));
                    if (!listMon.contains(mon)) {
                        listMon.add(mon);
                    } else {
                        // Có thể thông báo rằng món ăn đã tồn tại trong danh sách nếu cần
                        System.out.println("Món ăn đã tồn tại trong danh sách.");
                    }
                    Log.d("GioHang", "MaGH: " + maGH);
                    Log.d("GioHang", "MaMon: " + maMon);
                    Log.d("GioHang", "SoLuong: " + soLuong);
                    Log.d("GioHang", "ThanhTien: " + thanhTien);
                    Log.d("GioHang", "TenMon: " + tenMon);
                    Log.d("GioHang", "MaLoai: " + maLoai);
                    Log.d("GioHang", "GiaBan: " + giaBan);
                    Log.d("GioHang", "HinhAnh: " + hinhAnh);
                    tongTien += Integer.parseInt(thanhTien);
                }
                tvTongTien.setText(Integer.toString(tongTien));
                adapterMonGioHang.notifyDataSetChanged();
                tongTien = 0;
            } catch (Exception e) {
                Log.e("DonHang", "Lỗi xử lý JSON", e);
            }
        }
    }

    private class AdapterMon_GioHang extends RecyclerView.Adapter<AdapterMon_GioHang.MyViewHolder> {

        private ArrayList<Mon> arrayListMon;
        private OnMonClickListener listener;


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

            int tongTien = 0;

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
                        Log.d("Món", "Mã giỏ hàng:" + MaGH + "\n Mã Món: " + MaMon + "\n Số lượng:" + soLuong + "\n Thành tiền:" + ThanhTien);
                        postData(itemView.getContext(), MaGH, MaMon, Integer.parseInt(soLuong), ThanhTien, urlAddCTGH);
//                        adapterMonGioHang.clearData();
//                        new SendPostRequest().execute(KhachHangManager.MaGH);
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

                                // Cập nhật tổng tiền và refresh RecyclerView sau khi cập nhật thành công
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Update the relevant item in the adapter's data source
                                        int position = getAdapterPosition();
                                        if (position != RecyclerView.NO_POSITION) {
                                            Mon updatedMon = arrayListMon.get(position);
                                            updatedMon.setSoLuong(SoLuong); // Update quantity
                                            notifyItemChanged(position); // Refresh the item

                                            // Cập nhật tổng tiền
                                            tongTien = 0; // Reset tổng tiền
                                            for (Mon mon : arrayListMon) {
                                                tongTien += mon.getSoLuong() * mon.getGiaBan();
                                            }
                                            tvTongTien.setText(String.valueOf(tongTien)); // Cập nhật TextView tổng tiền
                                        }
                                    }
                                }, 500); // Delay slightly for the update to complete
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

        private int timIDAnh(Context context, String tenHinh) {
            int resourceId = context.getResources().getIdentifier(tenHinh, "drawable", context.getPackageName());
            return resourceId == 0 ? -1 : resourceId;  // Return -1 if resourceId is 0
        }
    }
}