package com.example.doan1.Mon;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doan1.R;

import java.util.List;

public class Mon_Activity_Adapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Class_Mon> MonList;

    public Mon_Activity_Adapter(Context context, int layout, List<Class_Mon> MonList) {
        this.context = context;
        this.layout = layout;
        this.MonList = MonList;
    }

    @Override
    public int getCount() {
        return MonList.size();
    }

    @Override
    public Class_Mon getItem(int position) {
        return MonList.get(position); // Get the item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position; // Use position as the ID
    }

    private class ViewHolder {
        TextView tenMon, giaBan;
        ImageView imageMon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            holder.imageMon = convertView.findViewById(R.id.imagemon);
            holder.tenMon = convertView.findViewById(R.id.TenMon);
            holder.giaBan = convertView.findViewById(R.id.GiaBan);

            convertView.setTag(holder); // Store the ViewHolder in the view's tag
        } else {
            holder = (ViewHolder) convertView.getTag(); // Retrieve the ViewHolder from the view's tag
        }

        // Get the current Class_Mon object for the current position
        Class_Mon mon = getItem(position);

        if (mon != null) {
            String imageName = mon.getHinhAnh().toLowerCase();

            // Retrieve the image resource ID from the name
            int imageId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

            // Set the image resource
            holder.imageMon.setImageResource(imageId);

            // Apply rounded corners to the image
            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(50f);
            holder.imageMon.setBackground(shape);

            // Set the text for the dish name and price
            holder.tenMon.setText(mon.getTenMon());
            holder.giaBan.setText(String.format("Giá: %s", mon.getGiaBan()));
        }

        return convertView;
    }
}