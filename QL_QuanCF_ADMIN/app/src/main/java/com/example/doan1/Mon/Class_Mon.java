package com.example.doan1.Mon;

import android.os.Parcel;
import android.os.Parcelable;

public class Class_Mon implements Parcelable {
    private String MaMon;
    private String MaLoai;
    private String TenMon;
    private String HinhAnh;
    private int GiaBan;
    private int SoLuong;
    private int thanhTien;
    private String ghiChu;

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public int getThanhTien() {
        return thanhTien;
    }
    public void setThanhTien(int thanhTien) {
        this.thanhTien = thanhTien;
    }
    // Constructor
    public Class_Mon(String maMon, String maLoai, String tenMon, String hinhAnh, int giaBan, int soLuong) {
        MaMon = maMon;
        TenMon = tenMon;
        MaLoai = maLoai;
        HinhAnh = hinhAnh;
        GiaBan = giaBan;
        SoLuong = soLuong;

    }

    // Phương thức tạo đối tượng từ Parcel
    protected Class_Mon(Parcel in) {
        MaMon = in.readString();
        MaLoai = in.readString();
        TenMon = in.readString();
        HinhAnh = in.readString();
        GiaBan = in.readInt();
        SoLuong = in.readInt();
    }

    // Phương thức tạo đối tượng Creator
    public static final Parcelable.Creator<Class_Mon> CREATOR = new Parcelable.Creator<Class_Mon>() {
        public Class_Mon createFromParcel(Parcel in) {
            return new Class_Mon(in);
        }

        public Class_Mon[] newArray(int size) {
            return new Class_Mon[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(MaMon);
        dest.writeString(MaLoai);
        dest.writeString(TenMon);
        dest.writeString(HinhAnh);
        dest.writeInt(GiaBan);
        dest.writeInt(SoLuong);
    }

    // Các getter và setter (bạn đã có sẵn)
    public int getSoLuong() {
        return SoLuong;
    }

    public String getMaMon() {
        return MaMon;
    }

    public String getTenMon() {
        return TenMon;
    }

    public String getMaLoai() {
        return MaLoai;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public int getGiaBan() {
        return GiaBan;
    }

    public void setMaMon(String maMon) {
        MaMon = maMon;
    }

    public void setTenMon(String tenMon) {
        TenMon = tenMon;
    }

    public void setMaLoai(String maLoai) {
        MaLoai = maLoai;
    }

    public void setHinhAnh(String hinhAnh) {
        HinhAnh = hinhAnh;
    }

    public void setGiaBan(int giaBan) {
        GiaBan = giaBan;
    }

    public void setSoLuong(int soLuong) {
        SoLuong = soLuong;
    }
}
