package com.example.ql_quancf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ql_quancf.Model.Connect;

import java.util.HashMap;
import java.util.Map;

public class DangKy extends AppCompatActivity {
    TextView tvLogin;
    Button btnDangKy;
    EditText edtEmail,edtFullName,edtPassword,edtSDT;
    String urlAPI_AddKH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        //------------------
        //Thêm id của control
        addID();
        //Thêm sự kiên
        addEvent();

    }
    void addID()
    {
        //Add control
        tvLogin=(TextView) findViewById(R.id.tvLogin);
        btnDangKy=(Button) findViewById(R.id.btnDangKy);
        edtEmail=(EditText) findViewById(R.id.edtEmail);
        edtFullName=(EditText) findViewById(R.id.edtFullNam);
        edtPassword=(EditText) findViewById(R.id.edtPassword);
        edtSDT=(EditText) findViewById(R.id.edtSDT);

        //Khai báo biến
        urlAPI_AddKH="http://" + Connect.ipConnect + "/Ql_QuanCF_User/DK_AcountKhachHang.php";
    }
    void addEvent()
    {
        //Đóng activity hiện hành để chuyển qua activity Login
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Sự kiện click button Đăng ký -->Tạo khách hàng và tài khoản khách hàng, giỏ hàng
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    String email = edtEmail.getText().toString().trim();
                    String fullName = edtFullName.getText().toString().trim();
                    String password = edtPassword.getText().toString().trim();
                    String sdt = edtSDT.getText().toString().trim();
                    //Gọi hàm thực hiện gửi dữ liệu lên server
                    postData(email, fullName, sdt, password, urlAPI_AddKH);
                }
            }
        });
    }
    boolean checkEmpty() {
        //Kiểm tra dữ liệu rổng
        return edtEmail.getText().toString().trim().isEmpty() ||
                edtFullName.getText().toString().trim().isEmpty() ||
                edtPassword.getText().toString().trim().isEmpty() ||
                edtSDT.getText().toString().trim().isEmpty();
    }
    public void postData(String Email, String TenKH, String SDT, String MatKhau, String url) {
        // Tạo một RequestQueue mới
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Tạo một StringRequest mới với phương thức POST
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Kiểm tra phản hồi từ server
                if(response.trim().equals("success")) {
                    Toast.makeText(getApplicationContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    edtEmail.setText("");
                    edtFullName.setText("");
                    edtPassword.setText("");
                    edtSDT.setText("");
                } else if(response.trim().equals("exist")) {
                    Toast.makeText(getApplicationContext(), "Khách hàng đã tồn tại", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Xảy ra lỗi, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Thêm các tham số gửi lên server
                Map<String, String> params = new HashMap<>();
                params.put("Email", Email);
                params.put("TenKH", TenKH);
                params.put("SDT", SDT);
                params.put("MatKhau", MatKhau);
                return params;
            }
        };

        // Thêm request vào RequestQueue để thực thi
        requestQueue.add(request);
    }

}