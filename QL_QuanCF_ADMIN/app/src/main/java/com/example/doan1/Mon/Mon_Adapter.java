package com.example.doan1.Mon;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doan1.LoaiMon.Class_LoaiMon;
import com.example.doan1.R;

import java.io.File;
import java.util.List;

public class Mon_Adapter extends ArrayAdapter<Class_Mon> {

    private List<Class_Mon> monList;
    private OnMonImageClickListener listener;
    private List<Class_Mon> chosenDishes;
    private SharedCartViewModel sharedCartViewModel;

    public interface OnMonImageClickListener {
        void onMonImageClick(String maMon, Class_Mon mon, boolean isIncrease);
    }

    public Mon_Adapter(Context context, List<Class_Mon> monList, OnMonImageClickListener listener, List<Class_Mon> chosenDishes, SharedCartViewModel sharedCartViewModel) {
        super(context, 0, monList);
        this.monList = monList;
        this.listener = listener;
        this.chosenDishes = chosenDishes;
        this.sharedCartViewModel = sharedCartViewModel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.customlisview_cafe, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageMon = convertView.findViewById(R.id.imagemon);
            viewHolder.tenMon = convertView.findViewById(R.id.TenMon);
            viewHolder.giaBan = convertView.findViewById(R.id.GiaBan);
            viewHolder.soLuong = convertView.findViewById(R.id.soLuong);
            viewHolder.giam = convertView.findViewById(R.id.icon1);
            viewHolder.tang = convertView.findViewById(R.id.icon2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Class_Mon mon = getItem(position);

        if (mon != null) {
            String imageName = mon.getHinhAnh().toLowerCase();
            Context context = parent.getContext();
            int imageId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

           // Hiển thị ảnh lên ImageView
            viewHolder.imageMon.setImageResource(imageId);


            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(50f);
            viewHolder.imageMon.setBackground(shape);

            viewHolder.tenMon.setText(mon.getTenMon());
            viewHolder.giaBan.setText(String.format("Giá: %s", mon.getGiaBan()));

            int soLuong = (sharedCartViewModel != null) ? sharedCartViewModel.getSoLuong(mon.getMaMon()) : 0;

            // Bắt sự kiện click trên ImageView để hiển thị số lượng
            viewHolder.imageMon.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMonImageClick(mon.getMaMon(), mon,true);
                    // Hiển thị TextView soLuong khi click vào hình ảnh
                    viewHolder.soLuong.setVisibility(View.VISIBLE);
                }
            });

            // Hiển thị số lượng chỉ khi được chọn
            if (soLuong > 0) {
                viewHolder.soLuong.setText(String.valueOf(soLuong));
                viewHolder.soLuong.setVisibility(View.VISIBLE);
                viewHolder.giam.setVisibility(View.VISIBLE);
                viewHolder.tang.setVisibility(View.VISIBLE);
            } else {
                // Nếu không có số lượng, ẩn đi TextView soLuong
                viewHolder.soLuong.setVisibility(View.GONE);
                viewHolder.giam.setVisibility(View.GONE);
                viewHolder.tang.setVisibility(View.GONE);
            }

            // Bắt sự kiện click giảm số lượng
            viewHolder.giam.setOnClickListener(v -> {
                if (listener != null && soLuong > 0) {
                    listener.onMonImageClick(mon.getMaMon(), mon, false);
                    notifyDataSetChanged();
                }
            });

// Bắt sự kiện click tăng số lượng
            viewHolder.tang.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMonImageClick(mon.getMaMon(), mon, true);
                    notifyDataSetChanged();
                }
            });

        }

        return convertView;
    }


    private static class ViewHolder {
        ImageView imageMon;
        ImageView giam;
        ImageView tang;
        TextView tenMon;
        TextView giaBan;
        TextView soLuong;
    }
}
