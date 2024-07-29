package com.example.doan1.Mon;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;

public class SharedCartViewModel extends ViewModel {
    private HashMap<String, Integer> soLuongMon = new HashMap<>();
    private ArrayList<Class_Mon> chosenDishes = new ArrayList<>();
    private HashMap<String, HashMap<String, Integer>> temporarySoLuongMon = new HashMap<>();

    private String maBan;

    public void setSoLuongMon(HashMap<String, Integer> soLuongMon) {
        this.soLuongMon = soLuongMon;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public String getMaBan() {
        return maBan;
    }

    public HashMap<String, Integer> getSoLuongMon() {
        return soLuongMon;
    }

    public void setChosenDishes(ArrayList<Class_Mon> chosenDishes) {
        this.chosenDishes = chosenDishes;
    }


    public ArrayList<Class_Mon> getChosenDishes() {
        return chosenDishes;
    }

    public void setSoLuong(String maMon, int soLuong) {
        soLuongMon.put(maMon, soLuong);
    }

    public int getSoLuong(String maMon) {
        return soLuongMon.getOrDefault(maMon, 0);
    }
    public void removeChosenDish(Class_Mon mon) {
        chosenDishes.remove(mon);
        decrementSoLuong(mon.getMaMon());
    }

    public void incrementSoLuong(String maMon) {
        int currentSoLuong = soLuongMon.getOrDefault(maMon, 0);
        soLuongMon.put(maMon, currentSoLuong + 1);
    }

    public void decrementSoLuong(String maMon) {
        int currentSoLuong = soLuongMon.getOrDefault(maMon, 0);
        if (currentSoLuong > 0) {
            soLuongMon.put(maMon, currentSoLuong - 1);
        }
    }

    public void addOrUpdateChosenDish(Class_Mon mon) {
        if (!chosenDishes.contains(mon)) {
            chosenDishes.add(mon);
        }
        incrementSoLuong(mon.getMaMon());
    }


}
