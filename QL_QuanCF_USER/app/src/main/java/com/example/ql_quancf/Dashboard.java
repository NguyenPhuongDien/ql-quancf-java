package com.example.ql_quancf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.ql_quancf.Model.Connect;
import com.example.ql_quancf.Model.KhachHangManager;
import com.example.ql_quancf.View.donhang_fragment;
import com.example.ql_quancf.View.home_fragment;
import com.example.ql_quancf.View.profile_fragment;
import com.example.ql_quancf.View.xemtatcaMon_fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Dashboard extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FrameLayout fragment_container;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        addID();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.item_Home) {
                    replaceFragment(new xemtatcaMon_fragment());
                    return true;
                }
                if (item.getItemId() == R.id.item_DonHang) {
                    replaceFragment(new donhang_fragment());
                    return true;
                }
                if (item.getItemId() == R.id.item_Profile) {
                    replaceFragment(new profile_fragment());
                    return true;
                }
                return false;
            }
        });
        //Chuyển fragment
        if (savedInstanceState == null) {
            replaceFragment(new xemtatcaMon_fragment());
        }

    }


    void addID()
    {
        bottomNavigationView=(BottomNavigationView) findViewById(R.id.bottom_navigation);
        fragment_container=(FrameLayout) findViewById(R.id.fragment_container);
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
    }
    void addEvent()
    {

    }
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
    private class SendPostRequest extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            String taiKhoan = params[0];
            String urlString = "http://" + Connect.ipConnect + "/Ql_QuanCF_User/GetTTKhachHangTheoEmail.php"; // Thay bằng URL thực tế
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setDoOutput(true);

                String urlParameters = "TaiKhoan=" + taiKhoan;

                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = urlConnection.getResponseCode();
                Log.d("MainActivity", "Response Code: " + responseCode);

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
                    Log.e("MainActivity", "POST request failed.");
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
                Log.e("MainActivity", "No response from server.");
            }
        }

        private void parseJsonResponse(String jsonResponse) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

            if (jsonObject.has("KhachHang") && jsonObject.has("GioHang")) {
                JsonObject khachHang = jsonObject.getAsJsonObject("KhachHang");
                JsonObject gioHang = jsonObject.getAsJsonObject("GioHang");

                String maKH = khachHang.get("MaKH").getAsString();
                String tenKH = khachHang.get("TenKH").getAsString();
                String email = khachHang.get("Email").getAsString();
                String sdt = khachHang.get("SDT").getAsString();

                String maGH = gioHang.get("MaGH").getAsString();

                Log.d("TTKhachHang", "MaKH: " + maKH);
                Log.d("TTKhachHang", "TenKH: " + tenKH);
                Log.d("TTKhachHang", "Email: " + email);
                Log.d("TTKhachHang", "SDT: " + sdt);
                Log.d("TTKhachHang", "MaGH: " + maGH);
                //Gán vào lớp Static
                KhachHangManager.MaKH=maKH;
                KhachHangManager.TenKH=tenKH;
                KhachHangManager.Email=email;
                KhachHangManager.SDT=sdt;
                KhachHangManager.MaGH=maGH;
            } else {
                Log.e("MainActivity", "Invalid JSON response");
            }
        }

    }
}