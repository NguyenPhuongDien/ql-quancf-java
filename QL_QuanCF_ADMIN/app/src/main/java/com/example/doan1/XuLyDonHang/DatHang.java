package com.example.doan1.XuLyDonHang;

public class DatHang {
    String MaDH;
    String MaKH;
    String NgayDat;
    double TongTien;
    String DiaChi;
    String SDT;
    String TrangThai;
    String GhiChu;

    public DatHang(String maDH, String maKH, String ngayDat, double tongTien, String diaChi, String SDT, String trangThai, String ghiChu) {
        MaDH = maDH;
        MaKH = maKH;
        NgayDat = ngayDat;
        TongTien = tongTien;
        DiaChi = diaChi;
        this.SDT = SDT;
        TrangThai = trangThai;
        GhiChu = ghiChu;
    }

    public DatHang(String maDH, String maKH, String ngayDat, double tongTien, String trangThai, String ghiChu) {
        MaDH = maDH;
        MaKH = maKH;
        NgayDat = ngayDat;
        TongTien = tongTien;
        TrangThai = trangThai;
        GhiChu = ghiChu;
    }
    public DatHang() {}

    public String getMaDH() {
        return MaDH;
    }

    public void setMaDH(String maDH) {
        MaDH = maDH;
    }

    public String getMaKH() {
        return MaKH;
    }

    public void setMaKH(String maKH) {
        MaKH = maKH;
    }

    public String getNgayDat() {
        return NgayDat;
    }

    public void setNgayDat(String ngayDat) {
        NgayDat = ngayDat;
    }

    public double getTongTien() {
        return TongTien;
    }

    public void setTongTien(double tongTien) {
        TongTien = tongTien;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        TrangThai = trangThai;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String ghiChu) {
        GhiChu = ghiChu;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }
}