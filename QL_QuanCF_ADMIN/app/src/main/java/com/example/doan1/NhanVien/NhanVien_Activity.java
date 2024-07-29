package com.example.doan1.NhanVien;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doan1.Admin.DK_TaiKhoanNV;
import com.example.doan1.Connect.Connect;
import com.example.doan1.LoaiMon.Adapter_LoaiMon;
import com.example.doan1.LoaiMon.Class_LoaiMon;
import com.example.doan1.LoaiMon.LoaiMon_Activity;
import com.example.doan1.LoaiMon.SuaLoai;
import com.example.doan1.LoaiMon.ThemLoai;
import com.example.doan1.LoaiMon.XoaLoai;
import com.example.doan1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NhanVien_Activity extends AppCompatActivity {

    String url = "http://" + Connect.connectip + "/DoAn_Android/QL_NhanVien.php";
    ListView listView;
    ArrayList<Class_NhanVien> arrayNhanVien;
    Adapter_NhanVien adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhan_vien);

        setTitle("Danh Sách Nhân Viên");

        Toolbar myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        listView = (ListView) findViewById(R.id.listview);
        arrayNhanVien = new ArrayList<>();
        adapter = new Adapter_NhanVien(NhanVien_Activity.this, R.layout.customlistview_nhanvien, arrayNhanVien);
        listView.setAdapter(adapter);
        boolean refreshData = getIntent().getBooleanExtra("REFRESH_DATA", false);
        if (refreshData) {
            arrayNhanVien.clear();
        }
        getData(url);
    }
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_danhmuc, menu);
        if(menu instanceof MenuBuilder)
        {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem_Insert:
                Intent intent = new Intent(getApplicationContext(), DK_TaiKhoanNV.class);
                startActivity(intent);
                return true;

            case R.id.menuitem_Delete:
                Intent intent1= new Intent(getApplicationContext(), XoaNhanVien.class);
                startActivity(intent1);
                return true;

            case R.id.menuitem_Update:
                Intent intent2 = new Intent(getApplicationContext(), ThongTinNV.class);
                startActivity(intent2);
                return true;
            case R.id.menuitem_Exit:
                Toast.makeText(this, "Thoát", Toast.LENGTH_SHORT).show();
                finishAffinity(); // Đóng tất cả các Activity và thoát ứng dụng
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void getData(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    arrayNhanVien.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        arrayNhanVien.add(new Class_NhanVien(
                                jsonObject.getString("MaNV"),
                                jsonObject.getString("TenNV"),
                                jsonObject.getString("SDT"),
                                jsonObject.getString("DiaChi")

                        ));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("getData", "Lỗi khi phân tích JSON", e);
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
}