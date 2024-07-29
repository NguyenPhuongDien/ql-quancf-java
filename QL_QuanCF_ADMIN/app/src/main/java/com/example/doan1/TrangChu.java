package com.example.doan1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.doan1.Admin.Admin;
import com.example.doan1.Admin.ThongKe;
import com.example.doan1.Ban.Ban_Fragment;
import com.example.doan1.NhanVien.ThongTinNV;
import com.example.doan1.XuLyDonHang.XuLyDonHang;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.example.doan1.R;

public class TrangChu extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ViewFlipper viewFlipper;


    // TrangChu.java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);


        Intent intent = getIntent();
        String gmail = intent.getStringExtra("gmail");
        String ten = intent.getStringExtra("tenNV");
        String ma = intent.getStringExtra("maNV");

        navigationView = findViewById(R.id.nav_drawerb6);
        View headerView = navigationView.getHeaderView(0);
        TextView gmailNV = headerView.findViewById(R.id.GmailNV);
        TextView tenNV = headerView.findViewById(R.id.TenNV);
        TextView maNV = headerView.findViewById(R.id.MaNV);

        gmailNV.setText(gmail);
        tenNV.setText(ten);
        maNV.setText(ma);
        // Toolbar setup
        toolbar = findViewById(R.id.toolbarb6);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Trang Chủ");

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

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
                        Intent intent = new Intent(TrangChu.this, Ban_Fragment.class);
                        intent.putExtra("maNV", ma);
                        startActivity(intent); // Khởi động BanActivity
                        drawerLayout.closeDrawers(); // Đóng drawer
                        return true;
                    case R.id.DX:
                        Intent intent2 = new Intent(TrangChu.this, MainActivity.class);
                        intent2.putExtra("maNV", ma);
                        startActivity(intent2);
                        return true;
                    case R.id.TT:
                        Intent intent1 = new Intent(TrangChu.this, ThongTinNV.class);
                        intent1.putExtra("maNV", ma);
                        startActivity(intent1);
                        return true;
                    case R.id.XuLy_DH:
                        Intent intent3 = new Intent(TrangChu.this, XuLyDonHang.class);
                        startActivity(intent3);
                        drawerLayout.closeDrawers();
                        return true;
                    default:
                        return false;
                }
            }
        });



        // Khởi tạo ViewFlipper
        viewFlipper = findViewById(R.id.viewFlipper);

        // Tạo mảng các hình ảnh banner
        int[] imageResources = {
                R.drawable.banner1,
                R.drawable.banner2,
                R.drawable.banner3
        };

        // Thêm các ImageView vào ViewFlipper
        for (int imageResource : imageResources) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imageResource);
            viewFlipper.addView(imageView);
        }

        // Thiết lập hiệu ứng chuyển đổi
        viewFlipper.setFlipInterval(2000); // Thời gian chuyển đổi (mili giây)
        viewFlipper.setAutoStart(true); // Bắt đầu tự động chuyển đổi
        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
    }
}