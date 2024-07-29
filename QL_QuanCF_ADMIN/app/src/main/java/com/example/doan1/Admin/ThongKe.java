package com.example.doan1.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;

import com.example.doan1.Ban.Ban_Adapter_Fragment;
import com.example.doan1.R;
import com.google.android.material.tabs.TabLayout;

public class ThongKe extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        viewPager = findViewById(R.id.viewPager);
        setUpViewPager(viewPager);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpViewPager(ViewPager viewPager) {
        Ban_Adapter_Fragment adapter = new Ban_Adapter_Fragment(getSupportFragmentManager());
        adapter.addFragment(new ThongKeThang(), "Thống Kê Theo Tháng");
        adapter.addFragment(new ThongKeNgay(),"Thống Kê Theo Ngày");
        viewPager.setAdapter(adapter);
    }
}
