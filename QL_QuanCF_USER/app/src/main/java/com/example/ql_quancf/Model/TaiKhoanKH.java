package com.example.ql_quancf.Model;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanKH {
    String TaiKhoan;
    String MaKH;
    String MatKhau;

    public TaiKhoanKH(String taiKhoan, String maKH, String matKhau) {
        TaiKhoan = taiKhoan;
        MaKH = maKH;
        MatKhau = matKhau;
    }
    public TaiKhoanKH() {
    }

    public String getTaiKhoan() {
        return TaiKhoan;
    }

    public void setTaiKhoan(String taiKhoan) {
        TaiKhoan = taiKhoan;
    }

    public String getMaKH() {
        return MaKH;
    }

    public void setMaKH(String maKH) {
        MaKH = maKH;
    }

    public String getMatKhau() {
        return MatKhau;
    }

    public void setMatKhau(String matKhau) {
        MatKhau = matKhau;
    }
    private static final String API_URL = "http://"+Connect.ipConnect+"/Ql_QuanCF_User/GetTaiKhoan.php";
    public static void getTaiKhoanKHList(Callback<List<TaiKhoanKH>> callback) {
        new Thread(() -> {
            List<TaiKhoanKH> taiKhoanKHList = new ArrayList<>();
            try {
                URL url = new URL(API_URL);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONArray jsonArray = new JSONArray(response.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String taiKhoan = jsonObject.getString("TaiKhoan");
                    String maKH = jsonObject.getString("MaKH");
                    String matKhau = jsonObject.getString("MatKhau");

                    TaiKhoanKH taiKhoanKH = new TaiKhoanKH(taiKhoan, maKH, matKhau);
                    taiKhoanKHList.add(taiKhoanKH);
                }
                // Trả về danh sách thông qua callback
                callback.onSuccess(taiKhoanKHList);
            } catch (Exception e) {
                // Trả về lỗi thông qua callback
                callback.onError(e);
            }
        }).start();
    }

    public interface Callback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }

}