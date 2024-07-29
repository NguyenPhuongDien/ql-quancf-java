package com.example.ql_quancf.Model;

import java.util.ArrayList;
import java.util.List;

public class TaiKhoanKHManeger {
    public static String UserName,PassWord;
    private static TaiKhoanKHManeger instance;
    private List<TaiKhoanKH> listTK;

    private TaiKhoanKHManeger() {
        listTK = new ArrayList<>();
    }

    public static synchronized TaiKhoanKHManeger getInstance() {
        if (instance == null) {
            instance = new TaiKhoanKHManeger();
        }
        return instance;
    }

    public List<TaiKhoanKH> getListTK() {
        return listTK;
    }

    public void setListTK(List<TaiKhoanKH> listTK) {
        this.listTK = listTK;
    }
}
