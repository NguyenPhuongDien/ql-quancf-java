package com.example.doan1.Mon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.doan1.Connect.Connect;
import com.example.doan1.HoaDon.CartHelper;
import com.example.doan1.HoaDon.HoaDon;
import com.example.doan1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tra_Fragment extends Fragment implements Mon_Adapter.OnMonImageClickListener {

    private static final String URL_GET_CAFE = "http:/"+ Connect.connectip+"/DoAn_Android/QL_Tra.php";
    private ListView listCafe;
    private Mon_Adapter adapterMon;
    private List<Class_Mon> monList = new ArrayList<>();
    private Button btnThemVaoHoaDon, btnChonLai;
    private String maBan;
    private String maNV;
    private CartHelper cartHelper;
    private SharedCartViewModel sharedCartViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_loai_mon_fragment, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            maBan = bundle.getString("maBan");
            maNV = bundle.getString("maNV");
        }

        listCafe = view.findViewById(R.id.cafe);
        btnThemVaoHoaDon = view.findViewById(R.id.btnThemVaoHoaDon);
        btnChonLai = view.findViewById(R.id.btnChonLai);

        sharedCartViewModel = new ViewModelProvider(requireActivity()).get(SharedCartViewModel.class);
        adapterMon = new Mon_Adapter(getContext(), monList, this, sharedCartViewModel.getChosenDishes(), sharedCartViewModel);
        listCafe.setAdapter(adapterMon);

        getData(URL_GET_CAFE);

        btnChonLai.setOnClickListener(view1 -> {
            sharedCartViewModel.getSoLuongMon().clear();
            sharedCartViewModel.getChosenDishes().clear();
            adapterMon.notifyDataSetChanged();
            updateButtonWithSoLuong();
        });

        btnThemVaoHoaDon.setOnClickListener(v -> {
            if (!sharedCartViewModel.getChosenDishes().isEmpty()) {
                Intent intent = new Intent(getContext(), HoaDon.class);
                intent.putParcelableArrayListExtra("chosenDishes", sharedCartViewModel.getChosenDishes());
                intent.putExtra("maBan", maBan);
                intent.putExtra("soLuongMon", sharedCartViewModel.getSoLuongMon());
                intent.putExtra("maNV", maNV);

                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Vui lòng chọn món ăn", Toast.LENGTH_SHORT).show();
            }
        });

        listCafe.setOnItemClickListener((parent, view12, position, id) -> {
            Class_Mon selectedMon = monList.get(position);
            Intent intent = new Intent(getContext(), ChiTietMon.class);
            intent.putExtra("MaMon", selectedMon.getMaMon());
            intent.putExtra("MaLoai", selectedMon.getMaLoai());
            intent.putExtra("TenMon", selectedMon.getTenMon());
            intent.putExtra("HinhAnh", selectedMon.getHinhAnh());
            intent.putExtra("GiaBan", selectedMon.getGiaBan());
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateButtonWithSoLuong();
    }

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
            monList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                monList.add(new Class_Mon(
                        jsonObject.getString("MaMon"),
                        jsonObject.getString("MaLoai"),
                        jsonObject.getString("TenMon"),
                        jsonObject.getString("HinhAnh"),
                        jsonObject.getInt("GiaBan"),
                        jsonObject.getInt("SoLuong")

                ));
            }
            adapterMon.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.e("JSONError", "Error parsing JSON: " + e.getMessage());
        }
    }

    private void handleError(VolleyError error) {
        Log.e("NetworkError", "Xảy ra lỗi: " + error.getMessage());
        Toast.makeText(getContext(), "Lỗi mạng: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMonImageClick(String maMon, Class_Mon mon, boolean isIncrease) {
        // Xử lý hành động tương ứng với giảm hoặc tăng số lượng
        if (isIncrease) {
            // Tăng số lượng
            sharedCartViewModel.addOrUpdateChosenDish(mon);
        } else {
            // Giảm số lượng
            sharedCartViewModel.removeChosenDish(mon);
        }
        adapterMon.notifyDataSetChanged();

        updateButtonWithSoLuong();
    }


    private void updateButtonWithSoLuong() {
        int totalQuantity = 0;
        for (int quantity : sharedCartViewModel.getSoLuongMon().values()) {
            totalQuantity += quantity;
        }
        btnThemVaoHoaDon.setText("Thêm vào hóa đơn (" + totalQuantity + ")");
    }
}

