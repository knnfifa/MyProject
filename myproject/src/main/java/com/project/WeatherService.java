package com.project;

import com.project.models.WeatherInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.models.Location;



public class WeatherService {
    private static final String GEO_API = "https://geocoding-api.open-meteo.com/v1/search?name=%s&count=1&language=en&format=json";
    private static final String WEATHER_API = "https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&hourly=temperature_2m,weathercode,relativehumidity_2m,windspeed_10m";

    private static final ObjectMapper mapper = new ObjectMapper();

    public static WeatherInfo getWeatherData(String city) {
        try {
            Location location = getCityCoordinates(city);
            if (location == null) {
                System.err.println("❌ ไม่พบพิกัดของเมือง: " + city);
                return null;
            }
    
            // ดึงข้อมูลอากาศจาก API
            String weatherResponse = ApiClient.fetchApiResponse(String.format(WEATHER_API, location.getLatitude(), location.getLongitude()));
            JsonNode weatherData = mapper.readTree(weatherResponse).get("hourly");
    
            double temperature = weatherData.get("temperature_2m").get(0).asDouble();
            int weatherCode = weatherData.get("weathercode").get(0).asInt();
            double snowfall = weatherData.has("snowfall") ? weatherData.get("snowfall").get(0).asDouble() : 0;
            double humidity = weatherData.has("relativehumidity_2m") ? weatherData.get("relativehumidity_2m").get(0).asDouble() : 0;
            double windSpeed = weatherData.has("windspeed_10m") ? weatherData.get("windspeed_10m").get(0).asDouble() : 0;
            double pressure = weatherData.has("surface_pressure") ? weatherData.get("surface_pressure").get(0).asDouble() : 0;
            double visibility = weatherData.has("visibility") ? weatherData.get("visibility").get(0).asDouble() : 0;
    

            String geoResponse = ApiClient.fetchApiResponse(String.format(GEO_API, city.replaceAll(" ", "+")));
            JsonNode geoData = mapper.readTree(geoResponse).get("results");
    
            String timezone = "UTC"; // ค่าเริ่มต้น
            if (geoData != null && geoData.size() > 0) {
                JsonNode firstResult = geoData.get(0);
                if (firstResult.has("timezone")) {
                    timezone = firstResult.get("timezone").asText();
                }
            }
            System.out.println("✅ Timezone from API: " + timezone); 
    
            // ดึงเวลาพระอาทิตย์ขึ้นและตก
            String sunrise = weatherData.has("sunrise") ? weatherData.get("sunrise").get(0).asText() : "--:--";
            String sunset = weatherData.has("sunset") ? weatherData.get("sunset").get(0).asText() : "--:--";
    
    
            // ✅ ปรับให้แสดง Snow เมื่ออุณหภูมิต่ำและมีเมฆมาก
            String weatherCondition;
            if (snowfall > 0) {
                weatherCondition = "Snow";
            } else if (temperature <= -5 && convertWeatherCode(weatherCode).equals("Cloudy")) {
                weatherCondition = "Snow";
            } else {
                weatherCondition = convertWeatherCode(weatherCode);
            }
    
            return new WeatherInfo(city, temperature, weatherCondition, snowfall, humidity, windSpeed, pressure, sunrise, sunset, visibility, timezone);
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

            double latitude = geoData.get(0).get("latitude").asDouble();
            double longitude = geoData.get(0).get("longitude").asDouble();

            return new Location(city, latitude, longitude);
        } catch (Exception e) {
            System.err.println("❌ Error while fetching location: " + e.getMessage());
            return null;
        }
    }

    private static String convertWeatherCode(int code) {
        switch (code) {
            case 0: return "Clear";
            case 1: case 2: case 3: return "Cloudy";
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
