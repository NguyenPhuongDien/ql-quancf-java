package com.example.ql_quancf.View;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ql_quancf.Controller.AdapterMon;
import com.example.ql_quancf.Controller.AdapterTaiKhoan;
import com.example.ql_quancf.Model.Connect;
import com.example.ql_quancf.Model.Mon;
import com.example.ql_quancf.Model.TaiKhoanKH;
import com.example.ql_quancf.R;
import com.example.ql_quancf.Controller.OnMonClickListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class home_fragment extends Fragment {
    RecyclerView recyclerviewMon;
    TextView tvXemAllMon;
    AdapterMon adapterMon;
    ArrayList<Mon> listMon;

    String url;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_home, container, false);
        //Gọi hàm khởi tạo các đối tượng trong fragment

        addID();
        getData(url);

        // Initialize the AdapterMon with a listener for click events
        adapterMon = new AdapterMon(listMon, new AdapterMon.OnMonClickListener() {
            @Override
            public void onMonClick(Mon mon) {
                // Handle the click event, for example:
                Toast.makeText(getContext(), "Clicked on " + mon.getTenMon(), Toast.LENGTH_SHORT).show();
                // You can also start a new activity or fragment based on the clicked item
            }
        });
        recyclerviewMon.setAdapter(adapterMon);

        recyclerviewMon.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        addEvent();
        return view;
    }
    void addID()
    {
        //Add control
        recyclerviewMon=(RecyclerView) view.findViewById(R.id.recyclerviewMon);
        tvXemAllMon=(TextView) view.findViewById(R.id.tvXemAllMon);
        //Khởi tạo đối tượng
        listMon=new ArrayList<>();

        //-------------
        url="http://"+ Connect.ipConnect+ "/Ql_QuanCF_User/QL_MonAn.php";
    }
    void addEvent()
    {
        try {
            tvXemAllMon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getFragmentManager();
                    if (fragmentManager != null) {
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.fragment_container, new xemtatcaMon_fragment());
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {
                        Log.e("Fragment Error", "FragmentManager is null");
                    }
                }
            });
        } catch (Exception e) {
            Log.e("Fragment Transaction", "Error in creating fragment", e);
        }

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
            listMon.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //Thêm đối tượng Món vào list
                listMon.add(new Mon(
                        jsonObject.getString("MaMon"),
                        jsonObject.getString("MaLoai"),
                        jsonObject.getString("TenMon"),
                        jsonObject.getString("HinhAnh"),
                        jsonObject.getInt("GiaBan"),
                        0
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
}
