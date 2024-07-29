package com.example.doan1.Ban;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doan1.Connect.Connect;
import com.example.doan1.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Ban_Activity extends AppCompatActivity {
    private Ban_Adapter adapterBan;
    private List<Class_Ban> banList = new ArrayList<>();
    private RecyclerView recyBan;
    String url = "http://" + Connect.connectip + "/DoAn_Android/QL_Ban.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban);
        setTitle("Danh Sách Bàn");

        Toolbar myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        recyBan = findViewById(R.id.recyBan);
        recyBan.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        adapterBan = new Ban_Adapter(banList);
        recyBan.setAdapter(adapterBan);

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
                Intent intent = new Intent(getApplicationContext(), ThemBan.class);
                startActivity(intent);
                return true;

            case R.id.menuitem_Delete:
                Intent intent1= new Intent(getApplicationContext(), XoaBan.class);
                startActivity(intent1);
                return true;

            case R.id.menuitem_Update:
                Intent intent2 = new Intent(getApplicationContext(), Sua_Ban.class);
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
                    banList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        banList.add(new Class_Ban(
                                jsonObject.getString("MaBan"),
                                jsonObject.getString("TenBan"),
                                jsonObject.getInt("TrangThai")
                        ));
                    }
                    adapterBan.notifyDataSetChanged();
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