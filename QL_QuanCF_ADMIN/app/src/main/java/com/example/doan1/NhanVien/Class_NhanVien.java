package com.example.doan1.NhanVien;

public class Class_NhanVien {
    private String MaNV;
    private String TenNV;
    private String SDT;
    private String DiaChi;

    public Class_NhanVien(String maNV, String tenNV, String SDT, String diaChi) {
        MaNV = maNV;
        TenNV = tenNV;
        this.SDT = SDT;
        DiaChi = diaChi;
    }

    public String getMaNV() {
        return MaNV;
    }

    public String getTenNV() {
        return TenNV;
    }

    public String getSDT() {
        return SDT;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setMaNV(String maNV) {
        MaNV = maNV;
    }

    public void setTenNV(String tenNV) {
        TenNV = tenNV;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }
}
