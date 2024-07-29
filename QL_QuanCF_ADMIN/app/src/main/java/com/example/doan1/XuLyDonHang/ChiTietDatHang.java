package com.example.doan1.XuLyDonHang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.doan1.Connect.Connect;
import com.example.doan1.Mon.Class_Mon;
import com.example.doan1.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ChiTietDatHang extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView tvMaDH,tvDiaChi,tvSDT,tvTongTien;
    ArrayList<Mon>listMon;
    DatHang datHang;
    AdapterChiTietDatHang adapterChiTietDatHang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_dat_hang);
        addID();
        Intent intent = getIntent();
        String maDH = intent.getStringExtra("MaDH");
        new SendPostRequest().execute(maDH);
        adapterChiTietDatHang=new AdapterChiTietDatHang(listMon);
        recyclerView.setAdapter(adapterChiTietDatHang);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DonHangCallback callback = new DonHangCallback() {
            @Override
            public void onDonHangLoaded(DatHang datHang) {
                if (datHang != null) {
                    // Cập nhật TextView với dữ liệu từ DatHang
                    tvMaDH.setText( datHang.getMaDH());
                    tvDiaChi.setText(datHang.getDiaChi());
                    tvSDT.setText(datHang.getSDT());
                    tvTongTien.setText(String.valueOf(datHang.getTongTien()));
                }
            }

            @Override
            public void onError(String errorMessage) {
                // Xử lý lỗi ở đây
            }
        };

        new chiTietDH(callback).execute(maDH);

    }
    void addID()
    {
        recyclerView=findViewById(R.id.recycler_ChiTietDH);
        tvMaDH=findViewById(R.id.tvMaDH);
        tvDiaChi=findViewById(R.id.tvDiaChi);
        tvSDT=findViewById(R.id.tvSDT);
        tvTongTien=findViewById(R.id.tvTongTien);
        listMon=new ArrayList<>();
    }
    public interface DonHangCallback {
        void onDonHangLoaded(DatHang datHang);
        void onError(String errorMessage);
    }

    private class chiTietDH extends AsyncTask<String, Void, DatHang> {
        private DonHangCallback callback;

        public chiTietDH(DonHangCallback callback) {
            this.callback = callback;
        }

        @Override
        protected DatHang doInBackground(String... params) {
            String MaGH = params[0];
            String urlString = "http://" + Connect.connectip + "/DoAn_Android/ThongTinDonHang.php";
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setDoOutput(true);

                String urlParameters = "MaDH=" + URLEncoder.encode(MaGH, "UTF-8");

                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = urlConnection.getResponseCode();
                Log.d("DonHang", "Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    return parseJsonResponse(response.toString());
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
        protected void onPostExecute(DatHang result) {
            if (result != null) {
                callback.onDonHangLoaded(result);
            } else {
                callback.onError("Không có phản hồi từ server.");
            }
        }

        private DatHang parseJsonResponse(String jsonResponse) {
            try {
                JSONArray jsonArray = new JSONArray(jsonResponse);
                if (jsonArray.length() > 0) {
                    JSONObject dathang = jsonArray.getJSONObject(0);
                    String maDH = dathang.optString("MaDH", "");
                    String maKH = dathang.optString("MaKH", "");
                    String diaChi = dathang.optString("DiaChi", "");
                    String SDT = dathang.optString("SDT", "");
                    Double tongTien = dathang.optDouble("TongTien", 0);
                    String ghiChu = dathang.optString("GhiChu", "");
                    String trangThai = dathang.optString("TrangThai", "");
                    String ngayDat = dathang.optString("NgayDat", "");

                    return new DatHang(maDH, maKH, ngayDat, tongTien, diaChi, SDT, trangThai, ghiChu);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private class SendPostRequest extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            String MaGH = params[0];
            String urlString = "http://" + Connect.connectip + "/DoAn_Android/ChiTietDonHang.php";
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setDoOutput(true);

                String urlParameters = "MaDH=" + MaGH;

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
                JSONArray jsonArray = new JSONArray(jsonResponse);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject monan = jsonArray.getJSONObject(i);

                    String maGH = monan.optString("MaDH", "");
                    String maMon = monan.optString("MaMon", "");
                    int soLuong = monan.optInt("SoLuong", 0);
                    int thanhTien = monan.optInt("ThanhTien", 0);
                    String maLoai = monan.optString("MaLoai", "");
                    String tenMon = monan.optString("TenMon", "");
                    String hinhAnh = monan.optString("HinhAnh", "");
                    int giaBan = monan.optInt("GiaBan", 0);

                    // Thực hiện các xử lý với dữ liệu đã lấy được từ JSON
                    // Ví dụ: tạo đối tượng Class_Mon và thêm vào listMon
                    Mon mon = new Mon(maMon, maLoai, tenMon, hinhAnh, giaBan, soLuong);
                    listMon.add(mon);
                }

                // Sau khi lấy dữ liệu, thông báo cho adapter cập nhật
                adapterChiTietDatHang.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                // Xử lý lỗi khi parse JSON
            }

        }
    }
}