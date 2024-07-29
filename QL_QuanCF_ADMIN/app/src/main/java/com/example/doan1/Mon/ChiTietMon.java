package com.example.doan1.Mon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doan1.R;

import java.util.ArrayList;
import java.util.List;

public class ChiTietMon extends AppCompatActivity {

    private List<Class_Mon> monList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_mon);

        // Lấy các view
        ImageView imageViewHinhAnh = findViewById(R.id.imageViewHinhAnh);
        TextView textViewTenMon = findViewById(R.id.textViewTenMon);
        TextView textViewGiaBan = findViewById(R.id.textViewGiaBan);
        TextView textViewMaMon = findViewById(R.id.textViewMaMon);
        TextView textViewMaLoai = findViewById(R.id.textViewMaLoai);

        // Lấy Bundle từ Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Lấy dữ liệu từ Bundle và gán cho các view
            String maMon = extras.getString("MaMon");
            String maLoai = extras.getString("MaLoai");
            String tenMon = extras.getString("TenMon");
            String hinhAnh = extras.getString("HinhAnh");
            double giaBan = extras.getDouble("GiaBan");

            // Set dữ liệu cho các view
            textViewTenMon.setText(tenMon);
            textViewGiaBan.setText(String.format("%.0f", giaBan));
            textViewMaMon.setText(maMon);
            textViewMaLoai.setText(maLoai);




            String imageName = hinhAnh;

            int imageId = getApplication().getResources().getIdentifier(imageName, "drawable", getApplication().getPackageName());

            // Hiển thị ảnh lên ImageView
            imageViewHinhAnh.setImageResource(imageId);


            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(50f);
            imageViewHinhAnh.setBackground(shape);
        }
    }
}