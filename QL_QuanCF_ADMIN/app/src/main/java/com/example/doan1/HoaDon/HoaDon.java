package com.example.doan1.HoaDon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doan1.Ban.Ban_Fragment;
import com.example.doan1.Ban.SharedViewModel;
import com.example.doan1.Ban.TatCaBan_Fragment;
import com.example.doan1.Connect.Connect;
import com.example.doan1.Mon.Class_Mon;
import com.example.doan1.Mon.LoaiMon_Fragment;
import com.example.doan1.Mon.SharedCartViewModel;
import com.example.doan1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class HoaDon extends AppCompatActivity {

    private ListView lvHoaDon;
    private ArrayAdapter<Class_Mon> adapter;
    private ArrayList<Class_Mon> chosenDishes = new ArrayList<>();
    private String maBan;
    private int SoLuong;
    private int tongTien = 0;
    private TextView tvTongTien;
    HashMap<String, Integer> soLuongMon;
    private NavController navController;
    private String maNV;
    private SharedCartViewModel sharedCartViewModel;

    private SharedViewModel sharedViewModel;
    private EditText edtGhiChu;
    private String ghiChu;

    private  PhieuThanhToan_Adapter adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don);


        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        lvHoaDon = findViewById(R.id.lvHoaDon);
        Button btnThanhToan = findViewById(R.id.btnThanhToan);

        tvTongTien = findViewById(R.id.tvTongTien);

        // Lấy danh sách chosenDishes, maBan, soLuongMon và maNV từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            chosenDishes = intent.getParcelableArrayListExtra("chosenDishes");
            maBan = intent.getStringExtra("maBan");
            soLuongMon = (HashMap<String, Integer>) intent.getSerializableExtra("soLuongMon");
            maNV = intent.getStringExtra("maNV"); // Lấy mã nhân viên từ Intent
        }

        // Kiểm tra xem danh sách món ăn đã chọn có rỗng hay không
        if (chosenDishes != null && !chosenDishes.isEmpty()) {
            // Tính tổng tiền
            for (Class_Mon mon : chosenDishes) {
                int thanhTien = soLuongMon.getOrDefault(mon.getMaMon(), 0) * mon.getGiaBan();
                mon.setThanhTien(thanhTien);
                tongTien += thanhTien;
            }

            // Cập nhật tổng tiền lên TextView
            String text = "Tổng tiền: " + tongTien;
            SpannableString spannableString = new SpannableString(text);

            // Tăng kích thước toàn bộ văn bản
            spannableString.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Màu đỏ cho phần tổng tiền
            spannableString.setSpan(new ForegroundColorSpan(Color.RED), text.indexOf(String.valueOf(tongTien)), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            tvTongTien.setText(spannableString);

            // Tạo adapter cho ListView
            adapter = new ArrayAdapter<Class_Mon>(this, R.layout.customlistview_hoadon, chosenDishes) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.customlistview_hoadon, parent, false);
                    }

                    // Lấy TextView từ view
                    TextView tvMaMon = convertView.findViewById(R.id.tvMaMon);
                    TextView tvTenMon = convertView.findViewById(R.id.tvTenMon);
                    TextView tvGiaBan = convertView.findViewById(R.id.tvGiaBan);
                    TextView tvSoLuong = convertView.findViewById(R.id.tvSoLuong);
                    ImageView hinhAnh=convertView.findViewById(R.id.ivHinhAnh);
                    ImageView icon=convertView.findViewById(R.id.ivDelete);
                    edtGhiChu=convertView.findViewById(R.id.edtGhiChu);
                    edtGhiChu.setTag(position);
                    edtGhiChu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) { // Khi EditText mất focus
                                int position = (int) v.getTag();
                                String ghiChu = edtGhiChu.getText().toString();
                                chosenDishes.get(position).setGhiChu(ghiChu); // Lưu ghi chú vào đối tượng Class_Mon
                            }
                        }
                    });

                    // Xử lý sự kiện click iconDelete
                    sharedCartViewModel = new ViewModelProvider(HoaDon.this).get(SharedCartViewModel.class);
                    icon.setTag(position);
                    icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = (int) v.getTag();
                            Class_Mon mon = chosenDishes.get(position);


                            chosenDishes.remove(position);
                            soLuongMon.remove(mon.getMaMon());

                            tongTien -= mon.getThanhTien();

                            adapter.notifyDataSetChanged();
                            String text = "Tổng tiền: " + tongTien;
                            SpannableString spannableString = new SpannableString(text);

                            spannableString.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spannableString.setSpan(new ForegroundColorSpan(Color.RED), text.indexOf(String.valueOf(tongTien)), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                            tvTongTien.setText(spannableString);

                            sharedCartViewModel.removeChosenDish(mon);
                        }
                    });

                    // Thiết lập dữ liệu cho TextView
                    Class_Mon mon = getItem(position);
                    if (mon != null) {
                        tvMaMon.setText("Mã Món: " + mon.getMaMon());
                        tvTenMon.setText("Tên Món: " + mon.getTenMon());
                        tvGiaBan.setText("Giá Bán: " + mon.getGiaBan());
                        int soLuong = soLuongMon.getOrDefault(mon.getMaMon(), 0);
                        tvSoLuong.setText("Số Lượng: " + soLuong);
                        String imgName = mon.getHinhAnh().toLowerCase();
                        int imgId = getResources().getIdentifier(imgName, "drawable", getPackageName());
                        hinhAnh.setImageResource(imgId);

                    }

                    return convertView;
                }
            };

            // Gán adapter cho ListView
            lvHoaDon.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Chưa có món nào được chọn", Toast.LENGTH_SHORT).show();
        }

        // Xử lý sự kiện click nút Thanh Toán
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gửi yêu cầu đến PHP web service
                thanhToan();
            }
        });



    }

    private void thanhToan() {
        String url = "http://" + Connect.connectip + "/DoAn_Android/ThemHD.php"; // Thay thế với URL thực tế của server
        //ghiChu = edtGhiChu.getText().toString();
        JSONObject params = new JSONObject();
        try {
            params.put("maBan", maBan);
            params.put("tongTien", tongTien);
            params.put("maNV", maNV);
           // params.put("ghiChu", ghiChu);

            JSONArray chosenDishesArray = new JSONArray();
            for (int i = 0; i < chosenDishes.size(); i++) {
                Class_Mon mon = chosenDishes.get(i);


                View itemView = lvHoaDon.getChildAt(i);
                EditText edtGhiChu = itemView.findViewById(R.id.edtGhiChu);

                // Cập nhật ghi chú cho đối tượng Class_Mon
                String ghiChu = edtGhiChu.getText().toString();
                mon.setGhiChu(ghiChu);

                JSONObject dishObject = new JSONObject();
                dishObject.put("maMon", mon.getMaMon());
                dishObject.put("soLuong", soLuongMon.getOrDefault(mon.getMaMon(), 0)); // Lấy số lượng từ HashMap
                dishObject.put("thanhTien", mon.getThanhTien());
                dishObject.put("ghiChu", mon.getGhiChu());
                chosenDishesArray.put(dishObject);
                Log.d("HoaDon", "Dữ liệu gửi đi: " + params.toString());
                Log.d("HoaDon", "Thông tin món gửi đi: " + dishObject.toString());
            }
            params.put("chiTietHoaDon", chosenDishesArray);

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String status = response.getString("status");
                                if (status.equals("success")) {
                                    Toast.makeText(HoaDon.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                                    updateBanTrangThai(maBan, 1);

                                   inHoaDon();


                            } else {
                                    Toast.makeText(HoaDon.this, "Lỗi: " + response.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(HoaDon.this, "Lỗi phản hồi JSON", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(HoaDon.this, "Lỗi mạng: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(HoaDon.this, "Lỗi tạo JSON", Toast.LENGTH_SHORT).show();
        }
    }

    private void inHoaDon()
    {
        // Tạo AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(HoaDon.this);
        View view = LayoutInflater.from(HoaDon.this).inflate(R.layout.phieuthanhtoan, null);
        builder.setView(view);

        // Lấy ngày và giờ hiện tại
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        String dateTime = dateFormat.format(calendar.getTime());

        TextView tvMaBan = view.findViewById(R.id.tvMaBan);
        TextView tvTongTien = view.findViewById(R.id.tvTongTien);
        TextView tvMaNV = view.findViewById(R.id.tvMaNV);
        TextView tvsdt = view.findViewById(R.id.tvsdt);
        TextView tvNgayGioThanhToan = view.findViewById(R.id.tvNgayGioThanhToan); // Thêm dòng này
        RecyclerView rvChiTietHoaDon = view.findViewById(R.id.rvChiTietHoaDon);
        Button btnDong = view.findViewById(R.id.btnDong);

        tvMaBan.setText("Mã bàn: " + maBan);
        tvTongTien.setText("Tổng tiền: " + tongTien);
        tvMaNV.setText("Mã nhân viên: " + maNV);
        tvNgayGioThanhToan.setText("Ngày giờ thanh toán: " + dateTime);


        PhieuThanhToan_Adapter adapter = new PhieuThanhToan_Adapter(HoaDon.this, chosenDishes, sharedViewModel, soLuongMon);
        rvChiTietHoaDon.setAdapter(adapter);
        rvChiTietHoaDon.setLayoutManager(new LinearLayoutManager(HoaDon.this));


        AlertDialog alertDialog = builder.create();
        btnDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng AlertDialog
                alertDialog.dismiss();

                Intent intent=new Intent(HoaDon.this,Ban_Fragment.class);
                intent.putExtra("maNV", maNV);
                startActivity(intent);
            }
        });
        alertDialog.show();
    }
    private void updateBanTrangThai(String maBan, int trangThai) {
        // Kiểm tra nếu mã bàn là "MB000" thì không thực hiện cập nhật trạng thái
        if (maBan.equals("MB000")) {
           inHoaDon();
            return;
        }

        String updateUrl = "http://" + Connect.connectip + "/DoAn_Android/QL_TrangThaiBan.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("success")) {
                                Toast.makeText(getApplicationContext(), "Cập nhật trạng thái bàn thành công", Toast.LENGTH_SHORT).show();

                                sharedViewModel.setTableStatusUpdated(true);
                            } else {
                                Toast.makeText(getApplicationContext(), "Lỗi cập nhật trạng thái bàn", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Lỗi phản hồi từ máy chủ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("maBan", maBan);
                params.put("trangThai", String.valueOf(trangThai));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}