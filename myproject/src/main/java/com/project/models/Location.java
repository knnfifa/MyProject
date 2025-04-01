package com.project.models;

public class Location {
    private String city;
    private double latitude;
    private double longitude;
    
    public Location(String city, double latitude, double longitude) { //Constuctor
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCity() { return city; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}

        /**เป็น Model Class ใช้เก็บ ชื่อเมืองและพิกัดทางภูมิศาสตร์
        ใช้สำหรับ ดึงข้อมูลจาก API, แสดงบนแผนที่, หรือ คำนวณระยะทางระหว่างเมือง
         มี เมธอด getter สำหรับดึงค่าต่างๆ **/