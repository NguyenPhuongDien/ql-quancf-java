package com.example.doan1.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.doan1.Ban.Ban_Activity;
import com.example.doan1.Ban.Ban_Fragment;
import com.example.doan1.LoaiMon.LoaiMon_Activity;
import com.example.doan1.Mon.Mon_Activity;
import com.example.doan1.NhanVien.NhanVien_Activity;
import com.example.doan1.R;
import com.example.doan1.TrangChu;
import com.example.doan1.XuLyDonHang.XuLyDonHang;
import com.google.android.material.navigation.NavigationView;

public class Admin extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    Button btnThongKe,btnBan,btnMon,btnNV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin);
        toolbar = findViewById(R.id.toolbarb6);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Trang Chủ");


        btnThongKe=findViewById(R.id.btnImage1);
        btnBan=findViewById(R.id.btnImage2);
        btnMon=findViewById(R.id.btnImage3);
        btnNV=findViewById(R.id.btnImage4);

        btnThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5 = new Intent(Admin.this, ThongKe.class);
                startActivity(intent5);
                drawerLayout.closeDrawers();
            }
        });

        btnMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5 = new Intent(Admin.this, Mon_Activity.class);
                startActivity(intent5);
                drawerLayout.closeDrawers();
            }
        });

        btnNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5 = new Intent(Admin.this, NhanVien_Activity.class);
                startActivity(intent5);
                drawerLayout.closeDrawers();
            }
        });

        btnBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5 = new Intent(Admin.this, Ban_Activity.class);
                startActivity(intent5);
                drawerLayout.closeDrawers();
            }
        });
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_drawerb6);
        Menu menu = navigationView.getMenu();
        MenuItem DKItem = menu.findItem(R.id.DK);
        MenuItem DXItem = menu.findItem(R.id.DX);
        MenuItem downloadedItem = menu.findItem(R.id.downloaded);
        SwitchCompat switchView = (SwitchCompat) downloadedItem.getActionView().findViewById(R.id.switchb6);
        switchView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                DKItem.setVisible(true);
                DXItem.setVisible(true);
            } else {
                DKItem.setVisible(false);
                DXItem.setVisible(false);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.DK:
                        Intent intent = new Intent(Admin.this, DK_TaiKhoanNV.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.Ban:
                        Intent intent3= new Intent(Admin.this, Ban_Activity.class);
                        startActivity(intent3);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.LoaiMon:
                        Intent intent4 = new Intent(Admin.this, LoaiMon_Activity.class);
                        startActivity(intent4);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.Mon:
                        Intent intent5 = new Intent(Admin.this, Mon_Activity.class);
                        startActivity(intent5);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.ThongKe:
                        Intent intent1 = new Intent(Admin.this, ThongKe.class);
                        startActivity(intent1);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.XuLy_DH:
                        Intent intent2 = new Intent(Admin.this, XuLyDonHang.class);
                        startActivity(intent2);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.DX:
                        finish();
                    default:
                        return false;
                }
            }
        });
    }

}