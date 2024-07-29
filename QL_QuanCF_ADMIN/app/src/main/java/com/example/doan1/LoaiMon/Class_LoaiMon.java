package com.example.doan1.LoaiMon;

public class Class_LoaiMon {
    private String MaLoai;
    private  String TenLoai;

    public Class_LoaiMon(String maLoai, String tenLoai) {
        MaLoai = maLoai;
        TenLoai = tenLoai;
    }

    public String getMaLoai() {
        return MaLoai;
    }

    public String getTenLoai() {
        return TenLoai;
    }

    public void setMaLoai(String maLoai) {
        MaLoai = maLoai;
    }

    public void setTenLoai(String tenLoai) {
        TenLoai = tenLoai;
    }
}
