package com.example.doan1.HoaDon;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.doan1.Mon.Class_Mon;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class CartHelper {

    private static final String PREFS_NAME = "cart_prefs";
    private static final String KEY_CHOSEN_DISHES = "chosen_dishes";
    private static final String KEY_SO_LUONG_MON = "so_luong_mon";

    private SharedPreferences prefs;
    private Gson gson;

    public CartHelper(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveCart(String maBan, ArrayList<Class_Mon> chosenDishes, HashMap<String, Integer> soLuongMon) {
        SharedPreferences.Editor editor = prefs.edit();

        // Lưu chosenDishes
        String jsonChosenDishes = gson.toJson(chosenDishes);
        editor.putString(KEY_CHOSEN_DISHES + "_" + maBan, jsonChosenDishes);

        // Lưu soLuongMon
        String jsonSoLuongMon = gson.toJson(soLuongMon);
        editor.putString(KEY_SO_LUONG_MON + "_" + maBan, jsonSoLuongMon);

        editor.apply();
    }

    public void loadCart(String maBan, ArrayList<Class_Mon> chosenDishes, HashMap<String, Integer> soLuongMon) {
        // Lấy chosenDishes
        String jsonChosenDishes = prefs.getString(KEY_CHOSEN_DISHES + "_" + maBan, null);
        if (jsonChosenDishes != null) {
            Type type = new TypeToken<ArrayList<Class_Mon>>() {}.getType();
            ArrayList<Class_Mon> loadedChosenDishes = gson.fromJson(jsonChosenDishes, type);
            chosenDishes.clear();
            chosenDishes.addAll(loadedChosenDishes);
        }

        // Lấy soLuongMon
        String jsonSoLuongMon = prefs.getString(KEY_SO_LUONG_MON + "_" + maBan, null);
        if (jsonSoLuongMon != null) {
            Type type = new TypeToken<HashMap<String, Integer>>() {}.getType();
            HashMap<String, Integer> loadedSoLuongMon = gson.fromJson(jsonSoLuongMon, type);
            soLuongMon.clear();
            soLuongMon.putAll(loadedSoLuongMon);
        }
    }

    public void clearCart(String maBan) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_CHOSEN_DISHES + "_" + maBan);
        editor.remove(KEY_SO_LUONG_MON + "_" + maBan);
        editor.apply();
    }
}
