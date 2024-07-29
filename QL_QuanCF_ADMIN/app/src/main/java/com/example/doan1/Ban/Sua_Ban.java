package com.example.doan1.Ban;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doan1.Connect.Connect;
import com.example.doan1.R;

import java.util.HashMap;
import java.util.Map;

public class Sua_Ban extends AppCompatActivity {
    private EditText edtMaBan;
    private EditText edtTenBan;
    String url = "http://" + Connect.connectip + "/DoAn_Android/Sua_Ban.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_ban);
        edtMaBan=findViewById(R.id.edtmaban);
        edtTenBan=findViewById(R.id.edttenban);

        Button btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postData(
                        edtMaBan.getText().toString(),
                        edtTenBan.getText().toString()

                );
            }
        });
    }

    public void postData(String maban, String tenban) {
        RequestQueue requestQueue = Volley.newRequestQueue(Sua_Ban.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    Toast.makeText(Sua_Ban.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(Sua_Ban.this, Ban_Activity.class);
                    intent.putExtra("REFRESH_DATA", true);
                    startActivity(intent);
                } else if (response.trim().equals("exist")) {
                    Toast.makeText(Sua_Ban.this, "Mã Bàn không tồn tại", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Sua_Ban.this, "Có lỗi xảy ra: " + response.trim(), Toast.LENGTH_SHORT).show();
                    Log.e("Lỗi phản hồi", "Có lỗi xảy ra: " + response.trim());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Sua_Ban.this, "Xảy ra lỗi, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                Log.e("Thêm bàn", "Xảy ra lỗi khi thêm bàn", error); // Sửa lỗi tại đây
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("MaBan", maban);
                params.put("TenBan", tenban);
                return params;
            }
        };
        requestQueue.add(request);
    }
}