package com.example.ql_quancf.View;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ql_quancf.Controller.AdapterMon_GioHang;
import com.example.ql_quancf.Controller.Adapter_LSDonHang;
import com.example.ql_quancf.Controller.OnDonHangClickListener;
import com.example.ql_quancf.Model.Connect;
import com.example.ql_quancf.Model.DatHang;
import com.example.ql_quancf.Model.KhachHangManager;
import com.example.ql_quancf.Model.Mon;
import com.example.ql_quancf.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LS_DonHang_Fragment extends Fragment {
    View view;
   RecyclerView lv_LSDonHang;
   Adapter_LSDonHang adapterLsDonHang;
   ArrayList<DatHang>listDatHang;
   TextView tvDonHang;
    private Handler handler = new Handler();
    private Runnable runnable;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_ls_donhang, container, false);
        addID();
        addEvent();
        adapterLsDonHang = new Adapter_LSDonHang(listDatHang, new OnDonHangClickListener() {
            @Override
            public void onDatHangClick(DatHang datHang) {
                    //Put mã đơn hàng qua trang khác
            }
        });


        lv_LSDonHang.setAdapter(adapterLsDonHang);
        lv_LSDonHang.setLayoutManager(new LinearLayoutManager(view.getContext()));
        new SendPostRequest().execute(KhachHangManager.MaKH);

        return view;
    }
    void addID()
    {
        lv_LSDonHang=(RecyclerView) view.findViewById(R.id.lv_LSDonHang);
        tvDonHang=(TextView)view.findViewById(R.id.tvDonHang);

        //--------
        listDatHang=new ArrayList<>();
    }
    private void startPolling() {
        runnable = new Runnable() {
            @Override
            public void run() {
                new SendPostRequest().execute(KhachHangManager.MaKH);
                handler.postDelayed(this, 5000);
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    void addEvent()
    {

    }
    private class SendPostRequest extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {

            String urlString = "http://" + Connect.ipConnect + "/Ql_QuanCF_User/LoadLichSuDatHang.php";
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setDoOutput(true);

                String urlParameters = "MaKH=" + KhachHangManager.MaKH;

                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = urlConnection.getResponseCode();
                Log.d("DonHang", "Response Code: " + responseCode);

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
                    Log.e("DonHang", "Gửi dữ liệu thất bại.");
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
                Log.e("DonHang", "Không có phản hồi từ server.");
            }
        }
        private void parseJsonResponse(String jsonResponse) {
            Gson gson = new Gson();
            try {

                JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class);
                // Kiểm tra số lượng phần tử trong jsonArray
                if (jsonArray.size() > 0) {
                    tvDonHang.setText("Số đơn hàng của bạn: "+String.valueOf(jsonArray.size()));
                } else {
                    tvDonHang.setText("Bạn chưa có đơn hàng nào"); // Hoặc một thông báo khác khi không có đơn hàng
                }
                for (JsonElement element : jsonArray) {
                    JsonObject khachHang = element.getAsJsonObject();

                    String maDH = khachHang.has("MaDH") && !khachHang.get("MaDH").isJsonNull() ? khachHang.get("MaDH").getAsString() : "";
                    String maKH = khachHang.has("MaKH") && !khachHang.get("MaKH").isJsonNull() ? khachHang.get("MaKH").getAsString() : "";

                    String ngayDatStr = khachHang.has("NgayDat") && !khachHang.get("NgayDat").isJsonNull() ? khachHang.get("NgayDat").getAsString() : "";
                    String ngayDatFormatted = "";
                    if (!ngayDatStr.isEmpty()) {
                        try {
                            Date ngayDat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(ngayDatStr);
                            ngayDatFormatted = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(ngayDat);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    Double tongTien = khachHang.has("TongTien") && !khachHang.get("TongTien").isJsonNull() ? khachHang.get("TongTien").getAsDouble() : 0.0;
                    String trangThai = khachHang.has("TrangThai") && !khachHang.get("TrangThai").isJsonNull() ? khachHang.get("TrangThai").getAsString() : "";
                    String ghiChu = khachHang.has("GhiChu") && !khachHang.get("GhiChu").isJsonNull() ? khachHang.get("GhiChu").getAsString() : "";

                    DatHang datHang = new DatHang(maDH, maKH, ngayDatFormatted, tongTien, trangThai, ghiChu);
                    listDatHang.add(datHang);

                    Log.d("DonHang", "------------------------");
                    Log.d("DonHang", "MaDH: " + maDH);
                    Log.d("DonHang", "MaKH: " + maKH);
                    Log.d("DonHang", "NgayDat: " + ngayDatFormatted);
                    Log.d("DonHang", "TongTien: " + tongTien);
                    Log.d("DonHang", "TrangThai: " + trangThai);
                    Log.d("DonHang", "GhiChu: " + ghiChu);
                    Log.d("DonHang", "------------------------");
                }

                adapterLsDonHang.notifyDataSetChanged();

            } catch (Exception e) {
                Log.e("DonHang", "Lỗi xử lý JSON", e);
            }
        }
    }

}