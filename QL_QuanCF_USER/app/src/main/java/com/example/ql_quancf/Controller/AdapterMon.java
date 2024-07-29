package com.example.ql_quancf.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ql_quancf.Model.Mon;
import com.example.ql_quancf.R;
import java.util.ArrayList;

public class AdapterMon extends RecyclerView.Adapter<AdapterMon.MyViewHolder> {

    private ArrayList<Mon> arrayListMon;
    private OnMonClickListener listener;

    public interface OnMonClickListener {
        void onMonClick(Mon mon);
    }

    public AdapterMon(ArrayList<Mon> arrayListMon, OnMonClickListener listener) {
        this.arrayListMon = arrayListMon;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_rycleview_mon, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Mon mon = arrayListMon.get(position);
        holder.tvTenMon.setText(mon.getTenMon());
        holder.tvGiaBan.setText(String.valueOf(mon.getGiaBan()));
        int idAnh = timIDAnh(holder.itemView.getContext(), mon.getHinhAnh());

        if (idAnh != -1) {
            holder.imgHinhAnh.setImageResource(idAnh);
        } else {
            holder.imgHinhAnh.setImageResource(R.drawable.imgfood_test_layout);
        }
    }

    @Override
    public int getItemCount() {
        return arrayListMon.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTenMon, tvGiaBan;
        ImageView imgHinhAnh;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenMon = itemView.findViewById(R.id.tvTenMon);
            tvGiaBan = itemView.findViewById(R.id.tvGiaBan);
            imgHinhAnh = itemView.findViewById(R.id.imgAnhMon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                listener.onMonClick(arrayListMon.get(position));
            }
        }
    }

    private static int timIDAnh(Context context, String tenHinh) {
        int resourceId = context.getResources().getIdentifier(tenHinh, "drawable", context.getPackageName());
        return resourceId == 0 ? -1 : resourceId;  // Return -1 if resourceId is 0
    }

}
