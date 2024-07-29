package com.example.ql_quancf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ql_quancf.Model.Connect;
import com.example.ql_quancf.Model.KhachHangManager;
import com.example.ql_quancf.Model.ListMonDatHang;
import com.example.ql_quancf.Model.Mon;
import com.example.ql_quancf.View.donhang_fragment;

import java.util.HashMap;
import java.util.Map;

public class dathang extends AppCompatActivity {
    Button btnDatHang;
    EditText edtDiaChi,edtSDT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dathang);
        addID();
        addEvent();
    }
    void addID()
    {
        btnDatHang=(Button) findViewById(R.id.btnDatHang);
        edtDiaChi=(EditText) findViewById(R.id.edtDiaChi);
        edtSDT=(EditText) findViewById(R.id.edtSDT);
    }
    void addEvent()
    {
        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String diaChi=edtDiaChi.getText().toString().trim();
                String sdt=edtSDT.getText().toString().trim();
                if(!diaChi.isEmpty() && !sdt.isEmpty())
                {
                    String url_PostDonHang = "http://" + Connect.ipConnect + "/Ql_QuanCF_User/TaoDonDatHang.php";
                    String url_PostChiTietDH = "http://" + Connect.ipConnect + "/Ql_QuanCF_User/TaoChiTietDatHang.php";
                    String url_XoaGioHang = "http://" + Connect.ipConnect + "/Ql_QuanCF_User/XoaGioHang.php";
                    //Tạo đơn đặt hàng + chi tiết đặt hàng

                    double tongTien =ListMonDatHang.TongTien;
                    String maKH = KhachHangManager.MaKH;
                    String maDH = "";
                    String maGH = KhachHangManager.MaGH;
                    if (ListMonDatHang.listMonDatHang.size() > 0) {
                        postDonHang(getApplicationContext(), maKH, tongTien, maGH,diaChi,sdt, url_PostDonHang, new OnMaDHReceivedListener() {
                            @Override
                            public void  onMaDHReceived(String maDH) {
                                if (!maDH.isEmpty()) {
                                    //Thục hiện thêm vào chi tiết đơn đặt hàng
                                    for (Mon mon : ListMonDatHang.listMonDatHang) {
                                        double thanhtien = mon.getSoLuong() * mon.getGiaBan();
                                        addCHiTietDatHang(getApplicationContext(), maDH, mon.getMaMon(), mon.getSoLuong(), thanhtien, url_PostChiTietDH);
                                        Log.d("GioHang", "Mã Món: " + mon.getMaMon() + ", Tên Món: " + mon.getTenMon() + ", Giá Bán: " + mon.getGiaBan() + ", Số Lượng: " + mon.getSoLuong() + ", Thành tiền: " + Double.toString(thanhtien));

                                    }
                                    //Xoá giỏ hàng sau khi đặc
                                    xoaGioHang(getApplicationContext(), KhachHangManager.MaGH, url_XoaGioHang);
                                    Toast.makeText(getApplicationContext(), "Đặc hàng thành công", Toast.LENGTH_SHORT).show();
                                   //Trở về trang trước
                                    finish();

                                } else {
                                    // Xử lý trường hợp không có mã đơn hàng
                                    Log.d("MaDH", "Không có mã đơn hàng");
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Không có sản phẩm trong giỏ hàng", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Vui lòng nhập đủ thông tin",Toast.LENGTH_SHORT).show();
                }

                //Post đơn hàng

            }
        });
    }
    public interface OnMaDHReceivedListener {
        void onMaDHReceived(String maDH);
    }
    void postDonHang(Context context, String MaKH, double TongTien, String maGioHang,String diaChi,String sdt, String url, OnMaDHReceivedListener listener) {
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
                params.put("DiaChi", diaChi);
                params.put("SDT", sdt);
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
}