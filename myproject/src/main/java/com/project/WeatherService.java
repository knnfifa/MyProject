package com.project;

import com.project.models.WeatherInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.models.Location;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.text.DecimalFormat;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WeatherService {

    private static final String GEO_API = "https://geocoding-api.open-meteo.com/v1/search?name=%s&count=1&language=en&format=json";
    private static final String WEATHER_API = "https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&current_weather=true&hourly=relativehumidity_2m&daily=sunrise,sunset&timezone=auto";

    //AQICN API (ใช้ชื่อเมือง)
    private static final String AQICN_API = "https://api.waqi.info/feed/%s/?token=%s";
    private static final String AQICN_API_TOKEN = "1da70d483aacbc39f13f7106ac075a9457cacab8";

    //OpenWeatherMap API (ใช้ Latitude/Longitude)
    private static final String OPENWEATHER_API_KEY = "7c5e217b38389be1fe387de7ac88d9a7";  //เปลี่ยนเป็น API Key ของคุณ
    private static final String PM25_API = "http://api.openweathermap.org/data/2.5/air_pollution?lat=%f&lon=%f&appid=%s";
    
    private static final ObjectMapper mapper = new ObjectMapper();

    public static WeatherInfo getWeatherData(String city) {
        try {
            Location location = getCityCoordinates(city);
            if (location == null) {
                System.err.println("❌ ไม่พบพิกัดของเมือง: " + city);
                return null;
            }
    
            // เรียกข้อมูลสภาพอากาศ
            String weatherResponse = ApiClient.fetchApiResponse(String.format(WEATHER_API, location.getLatitude(), location.getLongitude()));
            JsonNode rootData = mapper.readTree(weatherResponse);
            JsonNode currentWeather = rootData.get("current_weather");

            System.out.println(rootData.toString()); //ลองดูค่าที่ดึง API มา
    
            double temperature = currentWeather.get("temperature").asDouble();
            int weatherCode = currentWeather.get("weathercode").asInt();
            String currentTime = currentWeather.get("time").asText();
            
            // ดึงค่า visibility จาก current_weather หรือ hourly
            double visibility = currentWeather.has("visibility") ? currentWeather.get("visibility").asDouble() : 10.0; // ค่า default = 10.0 km หากไม่พบ
    
            // กำหนดความละเอียดของ visibility (ถ้า visibility มากกว่าค่าที่ได้จาก API)
            visibility = Math.round(visibility * 10.0) / 10.0;  // round ให้มีทศนิยม 1 ตำแหน่ง
    
            JsonNode hourly = rootData.get("hourly");
            JsonNode hourlyTime = hourly.get("time");
            JsonNode hourlyHumidity = hourly.get("relativehumidity_2m");
    
            double humidity = hourlyHumidity.get(0).asDouble();
            long minDiff = Long.MAX_VALUE;
    
            LocalDateTime currentTimeParsed = LocalDateTime.parse(currentTime);
    
            for (int i = 0; i < hourlyTime.size(); i++) {
                LocalDateTime hourlyTimeParsed = LocalDateTime.parse(hourlyTime.get(i).asText());
                long diff = Math.abs(ChronoUnit.MINUTES.between(currentTimeParsed, hourlyTimeParsed));
    
                if (diff < minDiff) {
                    minDiff = diff;
                    humidity = hourlyHumidity.get(i).asDouble();
                }
            }
    
            double windSpeed = currentWeather.get("windspeed").asDouble();
    
            JsonNode daily = rootData.get("daily");
            String sunrise = daily.has("sunrise") ? daily.get("sunrise").get(0).asText() : "--:--";
            String sunset = daily.has("sunset") ? daily.get("sunset").get(0).asText() : "--:--";
    
            String weatherCondition = convertWeatherCode(weatherCode);
    
            double snowfall = 0;
            String timezone = rootData.has("timezone") ? rootData.get("timezone").asText() : "UTC";
    
            double pm2_5 = getPM25(city, location.getLatitude(), location.getLongitude());
    
            // ส่งค่าทั้งหมดมาใน WeatherInfo
            return new WeatherInfo(city, temperature, weatherCondition, snowfall, humidity, windSpeed, pm2_5, sunrise, sunset, visibility, timezone);
    
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            return null;
        }
    }
    
    

    private static Location getCityCoordinates(String city) {
        try {
            String geoResponse = ApiClient.fetchApiResponse(String.format(GEO_API, city.replaceAll(" ", "+")));
            JsonNode geoData = mapper.readTree(geoResponse).get("results");

            if (geoData == null || geoData.isEmpty()) {
                System.err.println("❌ ไม่พบพิกัดของเมือง: " + city);
                return null;
            }

            JsonNode firstResult = geoData.get(0);
            double latitude = firstResult.get("latitude").asDouble();
            double longitude = firstResult.get("longitude").asDouble();

            return new Location(city, latitude, longitude);
        } catch (Exception e) {
            System.err.println("❌ Error while fetching location: " + e.getMessage());
            return null;
        }
    }

    private static final DecimalFormat df = new DecimalFormat("#.##"); // ฟอร์แมตให้มีทศนิยม 2 ตำแหน่ง

    private static double getPM25(String city, double latitude, double longitude) {
        double pm25 = -1;
    
        try {
            // แปลงชื่อเมืองให้เป็น URL Encoding เช่น "Chiang Mai" → "Chiang%20Mai"
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
    
            // 🔹 1. ลองดึงข้อมูลจาก AQICN
            System.out.println("🔍 กำลังดึงข้อมูล PM2.5 จาก AQICN...");
            String response = ApiClient.fetchApiResponse(String.format(AQICN_API, encodedCity, AQICN_API_TOKEN));
            JsonNode rootData = mapper.readTree(response);
    
            if (rootData.has("status") && rootData.get("status").asText().equals("ok")) {
                JsonNode pm25Node = rootData.get("data").get("iaqi").get("pm25").get("v");
                if (pm25Node != null) {
                    double aqiValue = pm25Node.asDouble();
                    pm25 = convertAQIToPM25(aqiValue);
                    pm25 = Double.parseDouble(df.format(pm25));
                    System.out.println("✅ PM2.5 จาก AQICN (แปลงเป็น µg/m³): " + pm25);
                    return pm25;
                }
            }
    
            //ถ้า AQICN ไม่มีข้อมูลใช้ OpenWeatherMap
            System.out.println("❌ AQICN ไม่มีข้อมูล ลองใช้ OpenWeatherMap...");
            response = ApiClient.fetchApiResponse(String.format(PM25_API, latitude, longitude, OPENWEATHER_API_KEY));
            rootData = mapper.readTree(response);
    
            if (rootData.has("list") && rootData.get("list").size() > 0) {
                JsonNode pm25Node = rootData.get("list").get(0).get("components").get("pm2_5");
                if (pm25Node != null) {
                    pm25 = pm25Node.asDouble();
                    pm25 = Double.parseDouble(df.format(pm25));
                    System.out.println("✅ PM2.5 จาก OpenWeatherMap (หน่วย µg/m³): " + pm25);
                }
            }
    
        } catch (Exception e) {
            System.err.println("❌ Error fetching PM2.5: " + e.getMessage());
        }
    
        return pm25;
    }
    
    //ฟังก์ชันแปลง AQI เป็น PM2.5 (µg/m³)

    /*ฟังก์ชันนี้ใช้แปลงค่าดัชนีคุณภาพอากาศ (AQI) เป็นค่าความเข้มข้นของฝุ่น PM2.5 (หน่วย µg/m³) 
    โดยใช้สูตรแปลงตามมาตรฐานของ EPA (Environmental Protection Agency)*/

    private static double convertAQIToPM25(double aqi) {
        double pm25;
        if (aqi <= 50) {
            pm25 = aqi * (12.0 / 50.0);
        } else if (aqi <= 100) {
            pm25 = ((aqi - 50) * (35.4 - 12.0) / 50.0) + 12.0;
        } else if (aqi <= 150) {
            pm25 = ((aqi - 100) * (55.4 - 35.4) / 50.0) + 35.4;
        } else if (aqi <= 200) {
            pm25 = ((aqi - 150) * (150.4 - 55.4) / 50.0) + 55.4;
        } else if (aqi <= 300) {
            pm25 = ((aqi - 200) * (250.4 - 150.4) / 100.0) + 150.4;
        } else if (aqi <= 400) {
            pm25 = ((aqi - 300) * (350.4 - 250.4) / 100.0) + 250.4;
        } else {
            pm25 = ((aqi - 400) * (500.4 - 350.4) / 100.0) + 350.4;
        }
        return Double.parseDouble(df.format(pm25)); //ปัดทศนิยมเหลือ 2 ตำแหน่ง
    }

    
    //แปลงค่า Weather Code ที่ได้จาก Open-Meteo API ให้เป็นข้อความที่อ่านเข้าใจง่าย
    private static String convertWeatherCode(int code) {
        switch (code) {
            case 0: return "Cloudy";
            case 1: case 2: case 3: return "Clear";
            case 45: case 48: return "Fog";
            case 51: case 53: case 55: return "Light Rain";
            case 61: case 63: case 65: return "Rain";
            case 66: case 67: return "Freezing Rain";
            case 71: case 73: case 75: return "Snow";
            case 80: case 81: case 82: return "Rain Showers";
            case 95: return "Thunderstorm";
            case 96: case 99: return "Severe Thunderstorm";
            default: return "Unknown";
        }
    }
}