package com.example.doan1.Ban;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.doan1.MainActivity;
import com.example.doan1.Mon.Class_Mon;
import com.example.doan1.Mon.SharedCartViewModel;
import com.example.doan1.NhanVien.ThongTinNV;
import com.example.doan1.R;
import com.example.doan1.XuLyDonHang.XuLyDonHang;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class Ban_Fragment extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    private SharedCartViewModel sharedCartViewModel;
    private String maBan, maNV;
    private ArrayList<Class_Mon> chosenDishes = new ArrayList<>();
    private HashMap<String, Integer> soLuongMon = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban_fragment);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            maNV = intent.getStringExtra("maNV");
            maBan = intent.getStringExtra("maBan");
            chosenDishes = getIntent().getParcelableArrayListExtra("chosenDishes");
            soLuongMon = (HashMap<String, Integer>) intent.getSerializableExtra("soLuongMon");
        }

        // Toolbar setup
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Danh Sách Bàn");

        // DrawerLayout setup
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // NavigationView setup
        navigationView = findViewById(R.id.nav_drawerb6);
        Menu menu = navigationView.getMenu();
        MenuItem DKItem = menu.findItem(R.id.DX);
        MenuItem downloadedItem = menu.findItem(R.id.downloaded);
        SwitchCompat switchView = (SwitchCompat) downloadedItem.getActionView().findViewById(R.id.switchb6);
        switchView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                DKItem.setVisible(true);
            } else {
                DKItem.setVisible(false);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Ban:
                        // Khởi tạo intent để chuyển sang BanActivity
                        Intent intent = new Intent(Ban_Fragment.this, Ban_Fragment.class);
                        intent.putExtra("maNV", maNV);
                        startActivity(intent); // Khởi động BanActivity
                        drawerLayout.closeDrawers(); // Đóng drawer
                        return true;
                    case R.id.DX:
                        Intent intent2 = new Intent(Ban_Fragment.this, MainActivity.class);
                        intent2.putExtra("maNV", maNV);
                        startActivity(intent2);
                        return true;
                    case R.id.TT:
                        Intent intent1 = new Intent(Ban_Fragment.this, ThongTinNV.class);
                        intent1.putExtra("maNV", maNV);
                        startActivity(intent1);
                        return true;
                    case R.id.XuLy_DH:
                        Intent intent3 = new Intent(Ban_Fragment.this, XuLyDonHang.class);
                        startActivity(intent3);
                        drawerLayout.closeDrawers();
                        return true;
                    default:
                        return false;
                }
            }
        });

        // Khởi tạo SharedCartViewModel
        sharedCartViewModel = new ViewModelProvider(this).get(SharedCartViewModel.class);

        // Khởi tạo ViewPager và TabLayout
        viewPager = findViewById(R.id.viewPager);
        setUpViewPager(viewPager, maNV);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpViewPager(ViewPager viewPager, String maNV) {
        Ban_Adapter_Fragment adapter = new Ban_Adapter_Fragment(getSupportFragmentManager());
        adapter.addFragment(new TatCaBan_Fragment(sharedCartViewModel, maNV), "Tất Cả");
        adapter.addFragment(new BanSuDung_Fragment(sharedCartViewModel, maNV), "Bàn Sử Dụng");
        adapter.addFragment(new BanTrong_Fragment(sharedCartViewModel, maNV), "Bàn Còn Trống");
        viewPager.setAdapter(adapter);
    }
}