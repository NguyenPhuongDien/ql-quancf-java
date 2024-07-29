package com.example.doan1.Ban;

public class Class_Ban {

    private String MaBan;
    private  String TenBan;
    private  int TrangThai;

    public Class_Ban(String maBan, String tenBan,int trangThai) {
        MaBan = maBan;
        TenBan = tenBan;
        TrangThai=trangThai;
    }

    public int getTrangThai() {
        return TrangThai;
    }

    public String getMaBan() {
        return MaBan;
    }

    public String getTenBan() {
        return TenBan;
    }

    public void setTrangThai(int trangThai) {
        TrangThai = trangThai;
    }

    public void setMaBan(String maBan) {
        MaBan = maBan;
    }

    public void setTenBan(String tenBan) {
        TenBan = tenBan;
    }
}
