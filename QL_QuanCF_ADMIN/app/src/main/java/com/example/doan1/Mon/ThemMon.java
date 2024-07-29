package com.example.doan1.Mon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doan1.Connect.Connect;
import com.example.doan1.LoaiMon.LoaiMon_Activity;
import com.example.doan1.LoaiMon.ThemLoai;
import com.example.doan1.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ThemMon extends AppCompatActivity {

    EditText edtMaMon, edtTenMon, edtHinhAnh, edtGiaBan;
    Spinner spnMaLoai;
    Button btnAdd;
    String url = "http://" + Connect.connectip + "/DoAn_Android/Them_Mon.php";
    String urlMaLoai = "http://" + Connect.connectip + "/DoAn_Android/LayMaLoai.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_mon);

        edtMaMon = findViewById(R.id.edtmamon);
        edtTenMon = findViewById(R.id.edttenmon);
        edtGiaBan = findViewById(R.id.edtgiaban);
        spnMaLoai = findViewById(R.id.spnMaLoai);
        edtHinhAnh = findViewById(R.id.edthinhanh);
        btnAdd = findViewById(R.id.btnAdd);

        loadDataMaLoai();


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themMon();
            }
        });
    }

    private void loadDataMaLoai() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlMaLoai, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    ArrayList<String> maLoaiList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        maLoaiList.add(jsonArray.getString(i));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ThemMon.this, android.R.layout.simple_spinner_item, maLoaiList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnMaLoai.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("loadDataMaLoai", "Lỗi khi phân tích JSON", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("NetworkError", "Xảy ra lỗi: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }


    private void themMon() {
        final String maLoai = spnMaLoai.getSelectedItem().toString();
        final String maMon = edtMaMon.getText().toString().trim();
        final String tenMon = edtTenMon.getText().toString().trim();
        final String hinhAnh = edtHinhAnh.getText().toString().trim(); // Tên ảnh đã lưu
        final String giaBan = edtGiaBan.getText().toString().trim();
        //final String soLuong = edtSoLuong.getText().toString().trim();

        if (maMon.isEmpty() || tenMon.isEmpty() || hinhAnh.isEmpty() || giaBan.isEmpty()) {
            Toast.makeText(ThemMon.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int giaBanInt = Integer.parseInt(giaBan);

        RequestQueue requestQueue = Volley.newRequestQueue(ThemMon.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    Toast.makeText(ThemMon.this, "Thêm món thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ThemMon.this, Mon_Activity.class);
                    intent.putExtra("REFRESH_DATA", true);
                    startActivity(intent);
                } else {
                    Toast.makeText(ThemMon.this, "Thêm món thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ThemMon.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("MaLoai", maLoai);
                params.put("MaMon", maMon);
                params.put("TenMon", tenMon);
                params.put("HinhAnh", hinhAnh); // Gửi tên ảnh lên server
                params.put("GiaBan", String.valueOf(giaBanInt));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}