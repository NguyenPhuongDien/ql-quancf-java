package com.example.doan1.NhanVien;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.example.doan1.Connect.Connect;
import com.example.doan1.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ThongTinNV extends AppCompatActivity {
    private TextView tvMaNV, tvTenNV, tvSDT, tvDiaChi, tvGmail;
    private EditText edtTenNV, edtSDT, edtDiaChi;
    private Button btnCapNhat;
    private String maNV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_nv);

        tvMaNV = findViewById(R.id.tvMaNV);
        tvTenNV = findViewById(R.id.tvTenNV);
        tvSDT = findViewById(R.id.tvSDT);
        tvDiaChi = findViewById(R.id.tvDiaChi);
        tvGmail = findViewById(R.id.tvGmail);
        edtTenNV = findViewById(R.id.edtTenNV);
        edtSDT = findViewById(R.id.edtSDT);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        btnCapNhat = findViewById(R.id.btnCapNhat);

        maNV = getIntent().getStringExtra("maNV");
        tvMaNV.setText("Mã NV: " + maNV);
        if (maNV != null) {
            loadThongTinNhanVien(maNV);
        }

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capNhatThongTinNhanVien();
            }
        });
    }

    private void loadThongTinNhanVien(String maNV) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    URL url = new URL("http://" + Connect.connectip + "/DoAn_Android/TTNV.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                    writer.write("maNV=" + URLEncoder.encode(maNV, "UTF-8"));
                    writer.flush();
                    writer.close();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();


                    JSONObject jsonResponse = new JSONObject(response.toString());
                    if (jsonResponse.getString("status").equals("success")) {
                        JSONObject data = jsonResponse.getJSONObject("data");

                        String maNV = data.getString("MaNV");
                        String tenNV = data.getString("TenNV");
                        String sdt = data.getString("SDT");
                        String diachi = data.getString("DiaChi");
                        String gmail = data.getString("Gmail");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                tvTenNV.setText("Tên NV: " + tenNV);
                                tvSDT.setText("Số điện thoại: " + sdt);
                                tvDiaChi.setText("Địa chỉ: " + diachi);
                                tvGmail.setText("Gmail: " + gmail);

                                // Thiết lập giá trị mặc định cho EditText
                                edtTenNV.setText(tenNV);
                                edtSDT.setText(sdt);
                                edtDiaChi.setText(diachi);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ThongTinNV.this, "message", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ThongTinNV.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void capNhatThongTinNhanVien() {
        String tenNV = edtTenNV.getText().toString();
        String sdt = edtSDT.getText().toString();
        String diachi = edtDiaChi.getText().toString();

        if (tenNV.isEmpty() || sdt.isEmpty() || diachi.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    URL url = new URL("http://" + Connect.connectip + "/DoAn_Android/UpdateNV.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                    writer.write("maNV=" + URLEncoder.encode(maNV, "UTF-8") +
                            "&tenNV=" + URLEncoder.encode(tenNV, "UTF-8") +
                            "&sdt=" + URLEncoder.encode(sdt, "UTF-8") +
                            "&diachi=" + URLEncoder.encode(diachi, "UTF-8"));
                    writer.flush();
                    writer.close();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ThongTinNV.this, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ThongTinNV.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}