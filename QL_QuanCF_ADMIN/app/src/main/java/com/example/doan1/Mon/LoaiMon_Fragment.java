package com.example.doan1.Mon;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.example.doan1.Ban.Ban_Adapter_Fragment;
import com.example.doan1.R;
import com.google.android.material.tabs.TabLayout;

public class LoaiMon_Fragment extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String tenBan;
    private String maBan;
    private  String maNV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban_fragment);

        // Lấy dữ liệu được truyền từ Ban_Fragment
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tenBan = bundle.getString("tenBan");
            maBan = bundle.getString("maBan");
            maNV = bundle.getString("maNV");
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager);
        setUpViewPager(viewPager);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);

        if (tenBan != null) {
            getSupportActionBar().setTitle(tenBan);
        } else {
            getSupportActionBar().setTitle("Danh sách món");
        }
    }

    private void setUpViewPager(ViewPager viewPager) {
        Bundle bundle = new Bundle();
        bundle.putString("maBan", maBan);
        bundle.putString("maNV", maNV);
        Cafe_Fragment cafeFragment = new Cafe_Fragment();
        cafeFragment.setArguments(bundle);

        SinhTo_Fragment sinhToFragment = new SinhTo_Fragment();
        sinhToFragment.setArguments(bundle);

        NuocEp_Fragment nuocEpFragment = new NuocEp_Fragment();
        nuocEpFragment.setArguments(bundle);

        Tra_Fragment traFragment = new Tra_Fragment();
        traFragment.setArguments(bundle);

        Banh_Fragment banhFragment = new Banh_Fragment();
        banhFragment.setArguments(bundle);

        NuocNgot_Fragment nuocNgotFragment = new NuocNgot_Fragment();
        nuocNgotFragment.setArguments(bundle);

        Ban_Adapter_Fragment adapter = new Ban_Adapter_Fragment(getSupportFragmentManager()); // Use getSupportFragmentManager for activity
        adapter.addFragment(cafeFragment, "CÀ PHÊ");
        adapter.addFragment(sinhToFragment, "SINH TỐ");
        adapter.addFragment(nuocEpFragment, "NƯỚC ÉP");
        adapter.addFragment(traFragment, "TRÀ");
        adapter.addFragment(banhFragment, "BÁNH");
        adapter.addFragment(nuocNgotFragment, "NƯỚC NGỌT");
        viewPager.setAdapter(adapter);
    }
}
