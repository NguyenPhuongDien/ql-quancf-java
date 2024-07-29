package com.example.ql_quancf.View;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ql_quancf.Controller.AdapterListViewMon;
import com.example.ql_quancf.Controller.AdapterMon;
import com.example.ql_quancf.Controller.AdapterSpiner_LoaiMon;
import com.example.ql_quancf.Model.Connect;
import com.example.ql_quancf.Model.LoaiMon;
import com.example.ql_quancf.Model.Mon;
import com.example.ql_quancf.Model.TaiKhoanKHManeger;
import com.example.ql_quancf.R;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class xemtatcaMon_fragment extends Fragment {
    View view;
    EditText edtTimKiem;
    Button btnTimKiem;
    RecyclerView recyclerView;
    Spinner spinnerLoaiMon;
    AdapterListViewMon adapterListViewMon;
    AdapterSpiner_LoaiMon adapterSpinerLoaiMon;
    ArrayList<Mon>listMon;
    ArrayList<LoaiMon>listLoaiMon;
    String urlMon,urlLoaiMon,urlMonTheoLoai,urlTimKiem;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.xem_all_mon, container, false);
        addID();
        getDataMonAn(urlMon);
        getDataLoaiMon(urlLoaiMon);
        LoaiMon loaiMon=new LoaiMon("0","Tất cả");
        listLoaiMon.add(loaiMon);
        addEvent();
         adapterListViewMon = new AdapterListViewMon(listMon, new AdapterListViewMon.OnMonClickListener() {
            @Override
            public void onMonClick(Mon mon) {
                // Xử lý sự kiện click trên mục Mon
                Toast.makeText(getContext(), "Clicked on " + mon.getTenMon(), Toast.LENGTH_SHORT).show();
                // Bạn có thể bắt đầu một Activity hoặc Fragment mới dựa trên mục được click
            }
        });

        recyclerView.setAdapter(adapterListViewMon);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext())); // Sử dụng LinearLayoutManager hoặc bất kỳ LayoutManager nào bạn muốn
        //Spinner
        adapterSpinerLoaiMon = new AdapterSpiner_LoaiMon(view.getContext(), listLoaiMon);
        spinnerLoaiMon.setAdapter(adapterSpinerLoaiMon);
        return view;
    }
    void addID() {
        //Khai báo mảng
        listMon=new ArrayList<>();
        listLoaiMon=new ArrayList<>();
        //Add id view
        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerviewAllMon);
        spinnerLoaiMon=(Spinner)view.findViewById(R.id.spinnerLoaiMon);
        edtTimKiem=(EditText)view.findViewById(R.id.edtTimKiem);
        btnTimKiem=(Button)view.findViewById(R.id.btnTimKiem);
        //Khai báo link API
        urlLoaiMon="http://"+ Connect.ipConnect+ "/Ql_QuanCF_User/Select_LoaiMon.php";
        urlMon="http://"+ Connect.ipConnect+ "/Ql_QuanCF_User/QL_MonAn.php";
        urlMonTheoLoai="http://"+ Connect.ipConnect+ "/Ql_QuanCF_User/SelectMonTheoLoai.php";
        urlTimKiem="http://"+ Connect.ipConnect+ "/Ql_QuanCF_User/TimKiemMonTheoTen.php";
    }
    void addEvent()
    {
        spinnerLoaiMon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lấy đối tượng LoaiMon từ adapter của Spinner
                LoaiMon selectedLoaiMon = (LoaiMon) parent.getItemAtPosition(position);

                if (selectedLoaiMon != null) {
                    if (selectedLoaiMon.getMaLoai().equals("0")) {
                        adapterListViewMon.clearData();
                        getDataMonAn(urlMon);
                        Log.d("Loại món", "MaLoai: " + selectedLoaiMon.getMaLoai() + ", TenLoai: " + selectedLoaiMon.getTenLoai());
                    } else {
                        Log.d("Loại món", "MaLoai: " + selectedLoaiMon.getMaLoai() + ", TenLoai: " + selectedLoaiMon.getTenLoai());

                        adapterListViewMon.clearData();

                        sendPostRequest("MaLoai", selectedLoaiMon.getMaLoai(),urlMonTheoLoai );
                    }
                } else {
                    // Xử lý trường hợp không có dòng được chọn
                    Log.e("Spinner", "No item selected");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý trường hợp không có dòng được chọn
                Log.e("Spinner", "No item selected");
            }
        });

        btnTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenMon=edtTimKiem.getText().toString().trim();
                Log.d("Món tìn kiếm",tenMon);
                if(tenMon.isEmpty())
                {
                    adapterListViewMon.clearData();
                    getDataMonAn(urlMon);
                }
                else
                {
                    adapterListViewMon.clearData();
                    sendPostRequest("TenMon",tenMon,urlTimKiem);
                }
            }
        });
    }
    private void getDataMonAn(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    listMon.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            listMon.add(new Mon(
                                    jsonObject.getString("MaMon"),
                                    jsonObject.getString("MaLoai"),
                                    jsonObject.getString("TenMon"),
                                    jsonObject.getString("HinhAnh"),
                                    jsonObject.getInt("GiaBan"),
                                    0
                            ));
                        }
                        adapterListViewMon.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e("JSONError", "Error parsing JSON: " + e.getMessage());
                    }
                },
                error -> {
                    Log.e("NetworkError", "Xảy ra lỗi: " + error.getMessage());
                    Toast.makeText(getContext(), "Lỗi mạng: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(jsonArrayRequest);
    }
    //Get date loại món
    private void getDataLoaiMon(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {

                    try {
                        listMon.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            listLoaiMon.add(new LoaiMon(
                                    jsonObject.getString("MaLoai"),
                                    jsonObject.getString("TenLoai")

                            ));
                        }
                        adapterSpinerLoaiMon.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e("JSONError", "Error parsing JSON: " + e.getMessage());
                    }
                },
                error -> {
                    Log.e("NetworkError", "Xảy ra lỗi: " + error.getMessage());
                    Toast.makeText(getContext(), "Lỗi mạng: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(jsonArrayRequest);
    }

    private void selectMonTheoLoai(String maLoai, String url) {
        // Tạo một RequestQueue để quản lý các yêu cầu HTTP
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Tạo một JsonObjectRequest để thực hiện yêu cầu GET
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, // Phương thức yêu cầu GET
                url, // URL của API với tham số mã loại được thêm vào URL
                null, // Dữ liệu gửi đi, trong trường hợp này là null vì yêu cầu GET không cần dữ liệu gửi đi
                response -> {
                    // Xử lý phản hồi từ máy chủ
                    try {
                        listMon.clear();
                        JSONArray jsonArray = response.getJSONArray("Mon"); // Lấy mảng JSON dữ liệu từ phản hồi
                        // Lặp qua từng phần tử trong mảng và thêm vào danh sách listMon
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            listMon.add(new Mon(
                                    jsonObject.getString("MaMon"),
                                    jsonObject.getString("MaLoai"),
                                    jsonObject.getString("TenMon"),
                                    jsonObject.getString("HinhAnh"),
                                    jsonObject.getInt("GiaBan"),
                                    0
                            ));
                        }
                        // Cập nhật adapterListViewMon sau khi nhận được dữ liệu mới
                        adapterListViewMon.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Xử lý lỗi trong quá trình gửi yêu cầu hoặc nhận phản hồi
                    Log.e("NetworkError", "Xảy ra lỗi: " + error.getMessage());
                    Toast.makeText(getContext(), "Lỗi mạng: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Thêm JsonObjectRequest vào hàng đợi yêu cầu để gửi yêu cầu đến máy chủ
        requestQueue.add(jsonObjectRequest);
    }

    private void timKiemTheoTen(String tenMon, String url) {
        // Tạo một đối tượng JSON để chứa mã loại
        JSONObject postData = new JSONObject();
        try {
            postData.put("TenMon", tenMon); // Đặt giá trị mã loại vào đối tượng JSON
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Tạo một RequestQueue để quản lý các yêu cầu HTTP
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Tạo một JsonObjectRequest để thực hiện yêu cầu POST
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, // Phương thức yêu cầu POST
                url, // URL của API
                postData, // Dữ liệu gửi đi, trong trường hợp này là đối tượng JSON chứa mã loại
                response -> {
                    // Xử lý phản hồi từ máy chủ
                    try {
                        listMon.clear();
                        JSONArray jsonArray = response.getJSONArray("data"); // Lấy mảng JSON dữ liệu từ phản hồi
                        // Lặp qua từng phần tử trong mảng và thêm vào danh sách listMon
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            listMon.add(new Mon(
                                    jsonObject.getString("MaMon"),
                                    jsonObject.getString("MaLoai"),
                                    jsonObject.getString("TenMon"),
                                    jsonObject.getString("HinhAnh"),
                                    jsonObject.getInt("GiaBan"),
                                    0
                            ));
                        }
                        // Cập nhật adapterListViewMon sau khi nhận được dữ liệu mới
                        adapterListViewMon.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Xử lý lỗi trong quá trình gửi yêu cầu hoặc nhận phản hồi
                    Log.e("NetworkError", "Xảy ra lỗi: " + error.getMessage());
                    Toast.makeText(getContext(), "Lỗi mạng: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Thêm JsonObjectRequest vào hàng đợi yêu cầu để gửi yêu cầu đến máy chủ
        requestQueue.add(jsonObjectRequest);
    }

    private void sendPostRequest(String namePost,String MaGH,String urlString) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String MaGH = params[0];

                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    urlConnection.setDoOutput(true);

                    String urlParameters = namePost+"=" + MaGH;

                    DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                    wr.writeBytes(urlParameters);
                    wr.flush();
                    wr.close();

                    int responseCode = urlConnection.getResponseCode();
                    Log.d("Mon", "Response Code: " + responseCode);

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
                        Log.e("Mon", "Gửi dữ liệu thất bại.");
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
                    Log.e("Mon", "Không có phản hồi từ server.");
                }
            }

            private void parseJsonResponse(String jsonResponse) {
                Gson gson = new Gson();
                try {
                    JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class);

                    for (JsonElement element : jsonArray) {
                        JsonObject mon = element.getAsJsonObject();


                        String maMon = mon.has("MaMon") && !mon.get("MaMon").isJsonNull() ? mon.get("MaMon").getAsString() : "";
                        String soLuong = mon.has("SoLuong") && !mon.get("SoLuong").isJsonNull() ? mon.get("SoLuong").getAsString() : "0";

                        String maLoai = mon.has("MaLoai") && !mon.get("MaLoai").isJsonNull() ? mon.get("MaLoai").getAsString() : "";
                        String tenMon = mon.has("TenMon") && !mon.get("TenMon").isJsonNull() ? mon.get("TenMon").getAsString() : "";
                        String hinhAnh = mon.has("HinhAnh") && !mon.get("HinhAnh").isJsonNull() ? mon.get("HinhAnh").getAsString() : "";
                        String giaBan = mon.has("GiaBan") && !mon.get("GiaBan").isJsonNull() ? mon.get("GiaBan").getAsString() : "0";

                        Mon monan = new Mon(maMon, maLoai, tenMon, hinhAnh, Integer.parseInt(giaBan), Integer.parseInt(soLuong));
                        if (!listMon.contains(mon)) {
                            listMon.add(monan);
                        } else {
                            // Có thể thông báo rằng món ăn đã tồn tại trong danh sách nếu cần
                            System.out.println("Món ăn đã tồn tại trong danh sách.");
                        }

                        Log.d("Món", "Tên món: " + tenMon);
                    }

                    adapterListViewMon.notifyDataSetChanged();

                } catch (Exception e) {
                    Log.e("Món", "Lỗi xử lý JSON", e);
                }
            }
        }.execute(MaGH);
    }


}
