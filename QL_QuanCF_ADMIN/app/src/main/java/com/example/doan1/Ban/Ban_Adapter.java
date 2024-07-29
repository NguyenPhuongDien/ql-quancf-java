package com.example.doan1.Ban;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan1.R;

import java.util.List;

public class Ban_Adapter extends RecyclerView.Adapter<Ban_Adapter.ViewHolder> {

    private List<Class_Ban> banList;
    private OnItemClickListener onItemClickListener;
    private OnButtonClickListener onButtonClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Class_Ban ban);
    }

    public interface OnButtonClickListener {
        void onButtonClick(Class_Ban ban);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    public Ban_Adapter(List<Class_Ban> banList) {
        this.banList = banList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customrecycler_ban, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Class_Ban ban = banList.get(position);
        holder.txtTenBan.setText(ban.getTenBan());

        if (ban.getMaBan().equals("MB000")) {
            holder.icon_ban.setVisibility(View.VISIBLE);
        } else {
            holder.icon_ban.setVisibility(View.GONE);
        }

        if (ban.getTrangThai() == 1) {
            holder.cardView.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.purple));
            holder.btnTraBan.setVisibility(View.VISIBLE);
        } else {
            holder.cardView.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.white));
            holder.btnTraBan.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, banList.get(holder.getAdapterPosition()));
                }
            }
        });

        holder.btnTraBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onButtonClick(banList.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return banList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTenBan;
        public ImageView icon_ban;
        public CardView cardView;
        public Button btnTraBan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenBan = itemView.findViewById(R.id.txt_tenban);
            cardView = itemView.findViewById(R.id.cardview);
            btnTraBan = itemView.findViewById(R.id.btnTraBan);
            icon_ban=itemView.findViewById(R.id.icon_ban);
        }
    }
}
