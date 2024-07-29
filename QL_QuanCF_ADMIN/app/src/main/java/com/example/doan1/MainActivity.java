package com.example.doan1;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doan1.Admin.Admin;
import com.example.doan1.Connect.Connect;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

public class  MainActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button loginButton;
    String ip = Connect.connectip;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login");

        // Đăng nhập bằng Google
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this, resultCode, 9000).show();
            } else {
                Log.e(TAG, "Thiết bị này không hỗ trợ Google Play Services.");
                finish();
            }
        }

        SignInButton googleSignInButton = findViewById(R.id.googleBtn);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taikhoan = username.getText().toString();
                String matkhau = password.getText().toString();

                if (taikhoan.equals("admin") && matkhau.equals("123")) {
                    Intent intent = new Intent(MainActivity.this, Admin.class);
                    startActivity(intent);
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL("http://" + Connect.connectip + "/DoAn_Android/Login.php");
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("POST");
                                conn.setDoOutput(true);
                                conn.setDoInput(true);

                                OutputStream os = conn.getOutputStream();
                                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                                writer.write("taikhoan=" + URLEncoder.encode(taikhoan, "UTF-8") + "&matkhau=" + URLEncoder.encode(matkhau, "UTF-8"));
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

                                JSONObject jsonResponse = new JSONObject(response.toString());
                                String status = jsonResponse.getString("status");
                                String message = jsonResponse.getString("message");

                                String gmail = jsonResponse.getString("gmail");
                                String tenNV = jsonResponse.getString("tenNV");
                                String maNV = jsonResponse.getString("maNV");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                        if (status.equals("success")) {
                                            Intent intent = new Intent(MainActivity.this, TrangChu.class);
                                            intent.putExtra("gmail", gmail);
                                            intent.putExtra("tenNV", tenNV);
                                            intent.putExtra("maNV", maNV);
                                            startActivity(intent);

                                            username.setText("");
                                            password.setText("");
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });
    }

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
                            URL url = new URL("http://" + Connect.connectip + "/DoAn_Android/Login.php");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setDoOutput(true);
                            conn.setDoInput(true);

                            OutputStream os = conn.getOutputStream();
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                            writer.write("email=" + URLEncoder.encode(email, "UTF-8")
                                    + "&tenNV=" + URLEncoder.encode(displayName, "UTF-8")
                                    + "&maNV=" + URLEncoder.encode(uid, "UTF-8"));
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

                            JSONObject jsonResponse = new JSONObject(response.toString());
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");

                            String gmail = jsonResponse.getString("gmail");
                            String tenNV = jsonResponse.getString("tenNV");
                            String maNV = jsonResponse.getString("maNV");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                    if (status.equals("success")) {
                                        Intent intent = new Intent(MainActivity.this, TrangChu.class);
                                        intent.putExtra("gmail", gmail);
                                        intent.putExtra("tenNV", tenNV);
                                        intent.putExtra("maNV", maNV);
                                        startActivity(intent);
                                    }
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        } else {
            if (response == null) {
                Toast.makeText(this, "Đăng nhập đã bị hủy bởi người dùng", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Đăng nhập đã bị hủy bởi người dùng");
            } else {
                // Hiển thị thông báo lỗi chi tiết từ response
                Toast.makeText(this, "Đăng nhập thất bại: " + response.getError().getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Đăng nhập thất bại với lỗi không xác định. " + response.getError().getMessage());
            }
        }
    }
}
