package com.example.doan1.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doan1.Connect.Connect;
import com.example.doan1.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class DK_TaiKhoanNV extends AppCompatActivity {

    private EditText username, gmail, password;
    private Button signinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dk_tai_khoan_nv);

        username = findViewById(R.id.username);
        gmail = findViewById(R.id.gmail);
        password = findViewById(R.id.password);
        signinButton = findViewById(R.id.signinButton);

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAccount();
            }
        });
    }

    private void registerAccount() {
        final String usernameText = username.getText().toString().trim();
        final String gmailText = gmail.getText().toString().trim();
        final String passwordText = password.getText().toString().trim();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // URL của PHP script xử lý đăng ký
                    URL url = new URL("http://" + Connect.connectip + "/DoAn_Android/DK_TaiKhoanNV.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    // Chuẩn bị dữ liệu gửi lên server
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write("username=" + usernameText + "&gmail=" + gmailText + "&password=" + passwordText);
                    writer.flush();
                    writer.close();
                    os.close();

                    // Đọc phản hồi từ server
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Ghi log phản hồi để kiểm tra nội dung
                    Log.d("ServerResponse", response.toString());

                    // Xử lý phản hồi
                    final JSONObject jsonResponse = new JSONObject(response.toString());
                    final String status = jsonResponse.getString("status");
                    final String message = jsonResponse.getString("message");

                    // Cập nhật giao diện
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DK_TaiKhoanNV.this, message, Toast.LENGTH_SHORT).show();
                            if (status.equals("success")) {
                                Intent intent=new Intent(DK_TaiKhoanNV.this,Admin.class);
                                startActivity(intent);
                                //finish();
                                username.setText("");
                                password.setText("");
                                gmail.setText("");
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    // Hiển thị lỗi kết nối
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DK_TaiKhoanNV.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                            Log.e("ConnectionError", "Lỗi kết nối: " + e.getMessage()); // Thêm dòng này để ghi lỗi vào logcat
                        }
                    });
                }
            }
        }).start();
    }

}