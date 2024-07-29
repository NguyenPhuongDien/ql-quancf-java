package com.example.doan1.XuLyDonHang;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.doan1.Connect.Connect;
import com.example.doan1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DaXacNhanDon_Fragment extends Fragment {


    View view;
    RecyclerView recycler_DH_ChuaXacNhan;
    String URL;
    ArrayList<DatHang> listDonHang;
    AdapterDaXacNhan adapterDonDatHang;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tvDonHang;
    private Handler handler = new Handler();
    private Runnable runnable;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.layout_xacnhandon, container, false);
        addID();
        getData(URL);
        adapterDonDatHang=new AdapterDaXacNhan(listDonHang, new AdapterDaXacNhan.OnMonClickListener() {
            @Override
            public void onMonClick(DatHang datHang) {
                // Handle the item click here
//                String maDH = datHang.getMaDH();
//                Toast.makeText(view.getContext(), "Mã đơn hàng: " + maDH, Toast.LENGTH_SHORT).show();

                // Optionally start a new activity

            }
        });
        recycler_DH_ChuaXacNhan.setAdapter(adapterDonDatHang);
        recycler_DH_ChuaXacNhan.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapterDonDatHang.clearData();
                getData(URL);
                refreshData();
            }
        });
        startPolling();
        return view;
    }
    void addID()
    {
        recycler_DH_ChuaXacNhan=(RecyclerView) view.findViewById(R.id.recycler_DH_DaXacNhan);
        tvDonHang=(TextView)view.findViewById(R.id.tvDonHang);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        //---------------
        listDonHang=new ArrayList<>();
        URL="http://" + Connect.connectip + "/DoAn_Android/List_DH_DaXacNhan.php";
    }

    private void refreshData() {

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // Kết thúc làm mới
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },
                2000); // 2 giây giả lập thời gian làm mới dữ liệu
    }
    private void startPolling() {
        runnable = new Runnable() {
            @Override
            public void run() {
                getData(URL);
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
    //Load dữ liệu từ API
    private void getData(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> parseCafeResponse(response.toString()),
                error -> handleError(error));
        requestQueue.add(jsonArrayRequest);
    }

    private void parseCafeResponse(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            listDonHang.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //Thêm đối tượng Món vào list
                listDonHang.add(new DatHang(
                        jsonObject.getString("MaDH"),
                        jsonObject.getString("MaKH"),
                        jsonObject.getString("NgayDat"),
                        jsonObject.getDouble("TongTien"),
                        jsonObject.getString("TrangThai"),
                        jsonObject.getString("GhiChu")
                ));

            }
            if(jsonArray.length()!=0)
            {
                tvDonHang.setText("DANH SÁCH ĐƠN HÀNG");
            }
            else
            {
                tvDonHang.setText("KHÔNG CÓ ĐƠN HÀNG NÀO");
            }
            adapterDonDatHang.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.e("JSONError", "Error parsing JSON: " + e.getMessage());
        }
    }

    private void handleError(VolleyError error) {
        Log.e("NetworkError", "Xảy ra lỗi: " + error.getMessage());
        Toast.makeText(getContext(), "Lỗi mạng: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }
}