package com.example.doan1.Ban;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doan1.Connect.Connect;
import com.example.doan1.HoaDon.CartHelper;
import com.example.doan1.Mon.Class_Mon;
import com.example.doan1.Mon.LoaiMon_Fragment;
import com.example.doan1.Mon.SharedCartViewModel;
import com.example.doan1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BanTrong_Fragment extends Fragment {
    private View rootView;
    String ip= Connect.connectip;
    String url = "http://"+ip+"//DoAn_Android/QL_BanTrong.php";
    private RecyclerView recyBan;
    private Ban_Adapter adapterBan;
    private List<Class_Ban> banList = new ArrayList<>();
    private List<Class_Mon> banList1 = new ArrayList<>();
    private ArrayList<Class_Mon> chosenDishes = new ArrayList<>();
    private String maNV;

    // Khởi tạo sharedCartViewModel trong onCreate()
    private SharedCartViewModel sharedCartViewModel;
    private String maBanTamTinh;
    HashMap<String, Integer> soLuongMon;
    private CartHelper cartHelper;
    public BanTrong_Fragment(SharedCartViewModel sharedCartViewModel,String maNV) {
        this.sharedCartViewModel = sharedCartViewModel;
        this.maNV=maNV;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        maBanTamTinh = getActivity().getIntent().getStringExtra("maBanTamTinh");
        // Khởi tạo sharedCartViewModel từ Activity cha
        sharedCartViewModel = new ViewModelProvider(requireActivity()).get(SharedCartViewModel.class);
        cartHelper = new CartHelper(getContext());
        //chosenDishes = sharedCartViewModel.getChosenDishes();
        //soLuongMon = sharedCartViewModel.getSoLuongMon();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tatcaban, container, false);

        recyBan = rootView.findViewById(R.id.recyBan);
        recyBan.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapterBan = new Ban_Adapter(banList);
        recyBan.setAdapter(adapterBan);
        getActivity().setTitle("Danh Sách Bàn");
        getData(url);


        return rootView;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (sharedCartViewModel != null) {
            adapterBan.setOnItemClickListener((view1, ban) -> {

                // Chuyển đến LoaiMon_Fragment với dữ liệu cần thiết
                Intent intent = new Intent(getContext(), LoaiMon_Fragment.class);
                Bundle bundle = new Bundle();
                bundle.putString("maBan", ban.getMaBan());
                bundle.putString("tenBan", ban.getTenBan());
                bundle.putString("maNV", maNV);
                if (chosenDishes != null) {
                    bundle.putParcelableArrayList("chosenDishes", chosenDishes);
                }
                bundle.putSerializable("soLuongMon", sharedCartViewModel.getSoLuongMon());
                intent.putExtras(bundle);
                startActivity(intent);
            });
        } else {
            Toast.makeText(getContext(), "SharedCartViewModel is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void getData(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    banList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        banList.add(new Class_Ban(
                                jsonObject.getString("MaBan"),
                                jsonObject.getString("TenBan"),
                                jsonObject.getInt("TrangThai")

                        ));
                    }

                    adapterBan.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("NetworkError", "Xảy ra lỗi: " + error.getMessage());
                Toast.makeText(getContext(), "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}