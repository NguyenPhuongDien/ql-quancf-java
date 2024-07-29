package com.example.ql_quancf.Model;

import java.util.Objects;

public class Mon {
        private String MaMon;
        private String MaLoai;
        private String TenMon;
        private String HinhAnh;
        private int GiaBan;
        private int SoLuong;

        public Mon(String maMon, String maLoai, String tenMon, String hinhAnh, int giaBan,int soLuong) {
            MaMon = maMon;
            MaLoai = maLoai;
            TenMon = tenMon;
            HinhAnh = hinhAnh;
            GiaBan = giaBan;
            SoLuong=soLuong;
        }

        public String getMaMon() {
            return MaMon;
        }

        public void setMaMon(String maMon) {
            MaMon = maMon;
        }

        public String getMaLoai() {
            return MaLoai;
        }

        public void setMaLoai(String maLoai) {
            MaLoai = maLoai;
        }

        public String getTenMon() {
            return TenMon;
        }

        public void setTenMon(String tenMon) {
            TenMon = tenMon;
        }

        public String getHinhAnh() {
            return HinhAnh;
        }

        public void setHinhAnh(String hinhAnh) {
            HinhAnh = hinhAnh;
        }

        public int getGiaBan() {
            return GiaBan;
        }

        public void setGiaBan(int giaBan) {
            GiaBan = giaBan;
        }

        public int getSoLuong() {
            return SoLuong;
        }

        public void setSoLuong(int soLuong) {
            SoLuong = soLuong;
        }
        // Override equals and hashCode
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Mon mon = (Mon) o;
            return Objects.equals(MaMon, mon.MaMon);
        }

        @Override
        public int hashCode() {
            return Objects.hash(MaMon);
        }
    }
