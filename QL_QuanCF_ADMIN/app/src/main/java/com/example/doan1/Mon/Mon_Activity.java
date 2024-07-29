package com.example.doan1.Mon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

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
import com.example.doan1.Ban.Ban_Adapter;
import com.example.doan1.Ban.Ban_Adapter_Fragment;
import com.example.doan1.Connect.Connect;
import com.example.doan1.LoaiMon.Adapter_LoaiMon;
import com.example.doan1.LoaiMon.Class_LoaiMon;
import com.example.doan1.LoaiMon.LoaiMon_Activity;
import com.example.doan1.LoaiMon.SuaLoai;
import com.example.doan1.LoaiMon.ThemLoai;
import com.example.doan1.LoaiMon.XoaLoai;
import com.example.doan1.R;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Mon_Activity extends AppCompatActivity {


    String url = "http://" + Connect.connectip + "/DoAn_Android/QL_Mon.php";
    ListView listView;
    ArrayList<Class_Mon> arrayMon;
    Mon_Activity_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon);
        setTitle("Danh Sách Món");

        Toolbar myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        listView = (ListView) findViewById(R.id.lvmon);
        arrayMon = new ArrayList<>();
        adapter = new Mon_Activity_Adapter(Mon_Activity.this, R.layout.customlisview_cafe, arrayMon);
        listView.setAdapter(adapter);
        boolean refreshData = getIntent().getBooleanExtra("REFRESH_DATA", false);
        if (refreshData) {
            arrayMon.clear();
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
                Intent intent = new Intent(getApplicationContext(), ThemMon.class);
                startActivity(intent);
                return true;

            case R.id.menuitem_Delete:
                Intent intent1= new Intent(getApplicationContext(), XoaMon.class);
                startActivity(intent1);
                return true;

            case R.id.menuitem_Update:
                Intent intent2 = new Intent(getApplicationContext(), SuaMon.class);
                startActivity(intent2);
                return true;
            case R.id.menuitem_Exit:
                Toast.makeText(this, "Thoát", Toast.LENGTH_SHORT).show();
                finishAffinity();
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
                    arrayMon.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        arrayMon.add(new Class_Mon(
                                jsonObject.getString("MaMon"),
                                jsonObject.getString("MaLoai"),
                                jsonObject.getString("TenMon"),
                                jsonObject.getString("HinhAnh"),
                                jsonObject.getInt("GiaBan"),
                                jsonObject.getInt("SoLuong")

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