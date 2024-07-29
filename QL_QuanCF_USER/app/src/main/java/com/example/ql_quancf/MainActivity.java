package com.example.ql_quancf;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ql_quancf.Model.Connect;
import com.example.ql_quancf.Model.KhachHangManager;
import com.example.ql_quancf.Model.TaiKhoanKH;
import com.example.ql_quancf.Model.TaiKhoanKHManeger;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    SignInButton btnLoginGoogle;
    private TextView tvDangKy;
    private static final String API_URL = "http://" + Connect.ipConnect + "/Ql_QuanCF_User/GetTaiKhoan.php";
    private  String URL_GetThongTin;
    private List<TaiKhoanKH> listTK; // Biến toàn cục để lưu trữ dữ liệu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addID();
        addEvent();
        String taiKhoan = "dien123@gmail.com"; // Thay bằng TaiKhoan thực tế

    }

    private void addID() {
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvDangKy=(TextView)findViewById(R.id.tvDangKy);
        btnLoginGoogle=(SignInButton) findViewById(R.id.btnLoginGoogle);
        //Khởi tạo đối tượng
        listTK = new ArrayList<>(); // Khởi tạo danh sách rỗng
        URL_GetThongTin="http://" + Connect.ipConnect + "/Ql_QuanCF_User/GetTTKhachHangTheoEmail.php";
    }
    private void addEvent() {
        //Login khi đăng ký
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                if (!username.isEmpty() && !password.isEmpty()) {
                    // Gọi API để lấy dữ liệu và xác thực tài khoản
                    getTaiKhoanKHList(new Callback<List<TaiKhoanKH>>() {
                        @Override
                        public void onSuccess(List<TaiKhoanKH> result) {
                            // Kiểm tra từng cặp tài khoản và mật khẩu
                            boolean isAuthenticated = false;
                            for (TaiKhoanKH item : result) {
                                if (username.equals(item.getTaiKhoan()) && password.equals(item.getMatKhau())) {
                                    TaiKhoanKHManeger.UserName=username;
                                    TaiKhoanKHManeger.PassWord=password;

                                    isAuthenticated = true;
                                    break;
                                }
                            }
                            // Hiển thị Toast trên luồng giao diện người dùng
                            final boolean finalIsAuthenticated = isAuthenticated;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (finalIsAuthenticated) {
                                        Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                        // Chuyển sang activity Dashboard
                                        //Get thông tin khách hàng theo Email
                                        new SendPostRequest().execute(edtUsername.getText().toString());
                                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);

                                        startActivity(intent);
                                        //Login xong xoá thông tin đăng nhập hiện tại
                                        edtUsername.setText("");
                                        edtPassword.setText("");
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        @Override
                        public void onError(Exception e) {
                            // Xử lý lỗi khi gọi API
                            Log.e("NetworkError", "Xảy ra lỗi: " + e.getMessage());
                        }
                    });
                } else {
                    // Hiển thị thông báo nếu tên đăng nhập hoặc mật khẩu trống
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Login google
        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });
        //Chuyển qua trang đăng ký
        tvDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),DangKy.class);
                startActivity(intent);
            }
        });

    }

    public void getTaiKhoanKHList(Callback<List<TaiKhoanKH>> callback) {
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
    //Xử lý đăng nhập google
    private void startSignIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String email = user.getEmail();
                String displayName = user.getDisplayName();
                String uid = user.getUid();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://" + Connect.ipConnect + "/QL_QuanCF_USER/Login.php");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setDoOutput(true);
                            conn.setDoInput(true);

                            OutputStream os = conn.getOutputStream();
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                            writer.write("email=" + URLEncoder.encode(email, "UTF-8")
                                    + "&tenKH=" + URLEncoder.encode(displayName, "UTF-8")
                                    + "&maKH=" + URLEncoder.encode(uid, "UTF-8"));
                            writer.flush();
                            writer.close();
                            os.close();

                            InputStream is = conn.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                            StringBuilder response = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                            reader.close();
                            is.close();

                            try {
                                JSONObject jsonResponse = new JSONObject(response.toString());
                                String status = jsonResponse.getString("status");
                                String message = jsonResponse.getString("message");

                                String gmail = jsonResponse.getString("gmail");
                                String tenKH = jsonResponse.getString("tenKH");
                                String maKH = jsonResponse.getString("maKH");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                        if (status.equals("success")) {
                                            new SendPostRequest().execute(gmail);
                                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);

                                            startActivity(intent);
                                        }
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e("Google", "Phản hồi không phải JSON hợp lệ: " + response.toString());
                                        Toast.makeText(MainActivity.this, "Lỗi phản hồi: Không thể phân tích JSON", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("Google", "Lỗi kết nối: " + e.getMessage());
                                    Toast.makeText(MainActivity.this, "Lỗi kết nối: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        } else {
            if (response == null) {
                Toast.makeText(this, "Đăng nhập đã bị hủy bởi người dùng", Toast.LENGTH_SHORT).show();
                Log.e("Google", "Đăng nhập đã bị hủy bởi người dùng");
            } else {
                // Hiển thị thông báo lỗi chi tiết từ response
                Toast.makeText(this, "Đăng nhập thất bại: " + response.getError().getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Google", "Đăng nhập thất bại với lỗi không xác định. " + response.getError().getMessage());
            }
        }
    }

    private class SendPostRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String taiKhoan = params[0];
            String urlString = "http://" + Connect.ipConnect + "/Ql_QuanCF_User/GetTTKhachHangTheoEmail.php";
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setDoOutput(true);

                // Gửi dữ liệu POST
                String urlParameters = "TaiKhoan=" + URLEncoder.encode(taiKhoan, "UTF-8");
                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = urlConnection.getResponseCode();
                Log.d("SendPostRequest", "Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    return response.toString();
                } else {
                    Log.e("SendPostRequest", "POST request failed with response code: " + responseCode);
                    return null;
                }
            } catch (Exception e) {
                Log.e("SendPostRequest", "Error during POST request", e);
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
                Log.e("SendPostRequest", "No response from server.");
            }
        }

        private void parseJsonResponse(String jsonResponse) {
            try {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

                if (jsonObject != null && jsonObject.has("KhachHang") && jsonObject.has("GioHang")) {
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

                    // Gán vào lớp Static
                    KhachHangManager.MaKH = maKH;
                    KhachHangManager.TenKH = tenKH;
                    KhachHangManager.Email = email;
                    KhachHangManager.SDT = sdt;
                    KhachHangManager.MaGH = maGH;
                } else {
                    Log.e("SendPostRequest", "Invalid JSON response: " + jsonResponse);
                }
            } catch (JsonSyntaxException e) {
                Log.e("SendPostRequest", "JSON parsing error: " + e.getMessage());
            }
        }
    }

}
