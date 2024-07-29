package com.example.ql_quancf.Controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ql_quancf.Model.Connect;
import com.example.ql_quancf.Model.KhachHangManager;
import com.example.ql_quancf.Model.Mon;
import com.example.ql_quancf.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterListViewMon extends RecyclerView.Adapter<AdapterListViewMon.MyViewHolder> {

    private ArrayList<Mon> arrayListMon;
    private OnMonClickListener listener;

    public interface OnMonClickListener {
        void onMonClick(Mon mon);
    }


    public AdapterListViewMon(ArrayList<Mon> arrayListMon, OnMonClickListener listener) {
        this.arrayListMon = arrayListMon;
        this.listener = listener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_listview_giohang, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Mon mon = arrayListMon.get(position);
        holder.tvTenMon.setText(mon.getTenMon());
        holder.tvGiaBan.setText(String.valueOf(mon.getGiaBan()));

        int idAnh = timIDAnh(holder.itemView.getContext(), mon.getHinhAnh());

        if (idAnh != -1) {
            holder.imagemon.setImageResource(idAnh);
        } else {
            holder.imagemon.setImageResource(R.drawable.imgfood_test_layout);
        }
    }

    @Override
    public int getItemCount() {
        return arrayListMon.size();
    }
    public void clearData() {
        if (arrayListMon != null) {
            arrayListMon.clear();
            notifyDataSetChanged();
        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTenMon, tvGiaBan;
        EditText edtSoLuong;
        ImageView imagemon;
        ImageView iconGiam,iconTang;
        Button btnAddMon;
        LinearLayout layoutMon;
        SwipeRefreshLayout swipeRefreshLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenMon = itemView.findViewById(R.id.tvTenMon);
            tvGiaBan = itemView.findViewById(R.id.tvGiaBan);

            imagemon = itemView.findViewById(R.id.imagemon);
            edtSoLuong=itemView.findViewById(R.id.edtSoLuong);
            iconGiam=(ImageView)itemView.findViewById(R.id.iconGiam);
            iconTang=(ImageView)itemView.findViewById(R.id.iconTang);
            btnAddMon=(Button)itemView.findViewById(R.id.btnAddMon);

            layoutMon=(LinearLayout)itemView.findViewById(R.id.layoutMua);
            //-------------------Add sự kiện------------------

            //--------------------------------------
            //Giảm số lượng
            iconGiam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sl=edtSoLuong.getText().toString().trim();
                    if(!sl.isEmpty() &&Integer.parseInt(sl)>=2)
                    {
                        sl=Integer.toString(Integer.parseInt(sl)-1);
                        edtSoLuong.setText(sl);
                    }
                }
            });
            //Tăng số lượng
            iconTang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sl=edtSoLuong.getText().toString().trim();
                    if(sl.isEmpty())
                    {
                        sl=Integer.toString(1);
                        edtSoLuong.setText(sl);
                    }
                    else
                    {
                        sl=Integer.toString(Integer.parseInt(sl)+1);
                        edtSoLuong.setText(sl);
                    }
                }
            });
            //Thêm vào giỏ hàng
            // Thêm vào giỏ hàng
            btnAddMon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Mon selectedMon = arrayListMon.get(position);
                        String soLuong = edtSoLuong.getText().toString().trim();
                        if (soLuong.isEmpty()) {
                            soLuong = "1";
                        }
                        String MaGH=KhachHangManager.MaGH; //Lấy ra mã giỏ hàng của khách hàng đang đăng nhập
                        String MaMon=selectedMon.getMaMon(); //Mã món đang chọn
                        //Tính tiền
                        Double ThanhTien=Double.parseDouble(soLuong)*selectedMon.getGiaBan();
                        //Link API
                        String urlAddCTGH="http://"+ Connect.ipConnect+ "/Ql_QuanCF_User/ThemMonGioHang.php";
                        //Thêm vào CSDL
                        postData(itemView.getContext(),MaGH,MaMon,selectedMon.getGiaBan(),Integer.parseInt(soLuong),ThanhTien,urlAddCTGH);

                    }
                }
            });
            //------------------------------------------------
            itemView.setOnClickListener(this);
        }
        //Thêm vào giỏ hàng
        void postData(Context context, String MaGH, String MaMon,int GiaBan, int SoLuong, double ThanhTien, String url) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Response", response); // Log the response
                    if (response.trim().equals("success")) {
                        Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    } else if (response.trim().equals("exist")) {
                        Toast.makeText(context, "Mã Món đã tồn tại", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Có lỗi xảy ra: " + response, Toast.LENGTH_SHORT).show(); // Show detailed error
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VolleyError", error.toString()); // Log the error
                    Toast.makeText(context, "Xảy ra lỗi, vui lòng thử lại: " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("MaGH", MaGH);
                    params.put("MaMon", MaMon);
                    params.put("GiaBan", Integer.toString(GiaBan));
                    params.put("SoLuong", Integer.toString(SoLuong));
                    params.put("ThanhTien", Double.toString(ThanhTien));
                    return params;
                }
            };
            requestQueue.add(request);
        }
        @Override
        public void onClick(View v) {
        }
    }

    private static int timIDAnh(Context context, String tenHinh) {
        int resourceId = context.getResources().getIdentifier(tenHinh, "drawable", context.getPackageName());
        return resourceId == 0 ? -1 : resourceId;
    }


}
