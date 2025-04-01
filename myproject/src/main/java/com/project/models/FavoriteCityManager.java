package com.project.models;

import java.io.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FavoriteCityManager {
    private static final String FILE_PATH = "favorites.json"; //ใช้เก็บชื่อเมืองโปรด
    private List<String> favorites = new ArrayList<>();

    public FavoriteCityManager() {
        load();
    }

    public void addCity(String city) {
        if (!favorites.contains(city)) {
            favorites.add(city);
            save();
        }
    }

    public void removeCity(String city) {
        favorites.remove(city);
        save();
    }

    public List<String> getFavorites() {
        return favorites;
    }

    private void save() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            Gson gson = new Gson();
            gson.toJson(favorites, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            Gson gson = new Gson();
            favorites = gson.fromJson(reader, new TypeToken<List<String>>() {}.getType());
            if (favorites == null) favorites = new ArrayList<>();
        } catch (IOException e) {
            favorites = new ArrayList<>(); // ไฟล์ยังไม่มี → สร้างใหม่
        }
    }
}


/*จัดการรายชื่อเมืองโปรด (เพิ่ม, ลบ, ดึงข้อมูล)
    ใช้ ไฟล์ JSON (favorites.json) ในการบันทึกและโหลดข้อมูล
    ใช้ Gson ช่วยแปลงข้อมูลเป็น JSON */