package com.example.doan1.Ban;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doan1.Connect.Connect;
import com.example.doan1.R;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ThemBan extends AppCompatActivity {

    private EditText edtMaBan;
    private EditText edtTenBan;
    private Socket socket; // Biến socket cho kết nối WebSocket

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_ban);

        edtMaBan = findViewById(R.id.edtmaban);
        edtTenBan = findViewById(R.id.edttenban);

        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maBan = edtMaBan.getText().toString();
                String tenBan = edtTenBan.getText().toString();

                // Kiểm tra dữ liệu
                if (maBan.isEmpty() || tenBan.isEmpty()) {
                    Toast.makeText(ThemBan.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Gọi web service PHP để thêm bàn vào cơ sở dữ liệu
                addTableToDatabase(maBan, tenBan);
            }
        });

        // Kết nối Socket Server
        connectToServer();
    }

    private void connectToServer() {
        try {
            // Thay thế "your_server_ip" bằng IP của server WebSocket
            socket = IO.socket("http://" + Connect.connectip + "8080");
            socket.connect();

            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("SocketIO", "Connected to server");
                }
            });

            socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e("SocketIO", "Error connecting to server: " + args[0]);
                }
            });

            socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("SocketIO", "Disconnected from server");
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e("SocketIO", "Error connecting to server: " + e.getMessage());
        }
    }

    private void addTableToDatabase(String maBan, String tenBan) {
        String url = "http://" + Connect.connectip + "/DoAn_Android/Them_Ban.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {
                                // Gửi sự kiện thêm bàn qua Socket.IO
                                sendAddTableEvent(maBan, tenBan);

                                Toast.makeText(ThemBan.this, "Thêm bàn thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ThemBan.this, Ban_Activity.class);
                                intent.putExtra("REFRESH_DATA", true);
                                startActivity(intent);
                                // Reset EditText
                                edtMaBan.setText("");
                                edtTenBan.setText("");
                            } else if (status.equals("exist")) {
                                Toast.makeText(ThemBan.this, "Bàn đã tồn tại", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ThemBan.this, "Lỗi thêm bàn", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ThemBan.this, "Lỗi phản hồi từ máy chủ", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(ThemBan.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("MaBan", maBan);
                params.put("TenBan", tenBan);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void sendAddTableEvent(String maBan, String tenBan) {
        try {
            JSONObject data = new JSONObject();
            data.put("MaBan", maBan);
            data.put("TenBan", tenBan);
            socket.emit("addTable", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socket != null) {
            socket.disconnect();
            socket.off();
        }
    }
}
