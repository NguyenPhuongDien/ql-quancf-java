package com.example.doan1.LoaiMon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doan1.Ban.Ban_Activity;
import com.example.doan1.Connect.Connect;
import com.example.doan1.R;

import java.util.HashMap;
import java.util.Map;

public class ThemLoai extends AppCompatActivity {

    private EditText edtMaLoai;
    private EditText edtTenLoai;
    String url = "http://" + Connect.connectip + "/DoAn_Android/Them_Loai.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_loai);

        edtMaLoai=findViewById(R.id.edtmaloai);
        edtTenLoai=findViewById(R.id.edttenloai);

        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postData(
                        edtMaLoai.getText().toString(),
                        edtTenLoai.getText().toString()

                );
            }
        });
    }

    public void postData(String maloai, String tenloai) {
        RequestQueue requestQueue = Volley.newRequestQueue(ThemLoai.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    Toast.makeText(ThemLoai.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(ThemLoai.this, LoaiMon_Activity.class);
                    intent.putExtra("REFRESH_DATA", true);
                    startActivity(intent);
                } else if (response.trim().equals("exist")) {
                    Toast.makeText(ThemLoai.this, "Mã Bàn đã tồn tại", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ThemLoai.this, "Có lỗi xảy ra: " + response.trim(), Toast.LENGTH_SHORT).show();
                    Log.e("Lỗi phản hồi", "Có lỗi xảy ra: " + response.trim());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ThemLoai.this, "Xảy ra lỗi, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                Log.e("Thêm bàn", "Xảy ra lỗi khi thêm bàn", error); // Sửa lỗi tại đây
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("MaLoai", maloai);
                params.put("TenLoai", tenloai);
                return params;
            }
        };
        requestQueue.add(request);
    }
}