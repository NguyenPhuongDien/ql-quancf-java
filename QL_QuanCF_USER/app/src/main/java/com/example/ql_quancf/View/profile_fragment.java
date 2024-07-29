package com.example.ql_quancf.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ql_quancf.MainActivity;
import com.example.ql_quancf.Model.KhachHangManager;
import com.example.ql_quancf.Model.TaiKhoanKHManeger;
import com.example.ql_quancf.R;

public class profile_fragment extends Fragment {
    View view;
    TextView tvTenKH,tvDangXuat,tvLSDonHang;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_profile, container, false);
        addID();
        addEvent();
        tvTenKH.setText(KhachHangManager.TenKH);

        return view;
    }
    void addID()
    {
        tvTenKH=(TextView) view.findViewById(R.id.tvTenKH);

        tvDangXuat=(TextView) view.findViewById(R.id.tvDangXuat);
        tvLSDonHang=(TextView) view.findViewById(R.id.tvLSDonHang);
    }
    void addEvent()
    {
        //Đăng xuất
        tvDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        try {
            tvLSDonHang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getFragmentManager();
                    if (fragmentManager != null) {
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.fragment_container, new LS_DonHang_Fragment());
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

}
